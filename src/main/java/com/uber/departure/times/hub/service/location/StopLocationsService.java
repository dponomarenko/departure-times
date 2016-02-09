package com.uber.departure.times.hub.service.location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.common.EventBusListener;
import com.uber.departure.times.common.VerticleBean;
import com.uber.departure.times.common.map.AsyncMultiMap;
import com.uber.departure.times.hub.HubConfiguration;
import com.uber.departure.times.pojo.Cell;
import com.uber.departure.times.pojo.Cells;
import com.uber.departure.times.pojo.Stop;
import com.uber.departure.times.pojo.Stops;
import com.uber.departure.times.pojo.codec.CellMessageCodec;
import com.uber.departure.times.pojo.codec.CellsMessageCodec;
import com.uber.departure.times.pojo.codec.StopMessageCodec;
import com.uber.departure.times.pojo.codec.StopsMessageCodec;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
public final class StopLocationsService extends VerticleBean<HubConfiguration> implements EventBusListener {
    private static final Logger logger = LoggerFactory.getLogger(StopLocationsService.class);

    protected static final String GET_MANY_ADDRESS = "hub.locations.dao.get.many";
    protected static final String GET_ADDRESS = "hub.locations.dao.get";
    protected static final String ADD_ADDRESS = "hub.locations.dao.add";

    private final AsyncMultiMap<Cell, Stop> map;

    public StopLocationsService(@NotNull Vertx vertx, @NotNull AsyncMultiMap<Cell, Stop> map, @NotNull HubConfiguration conf) {
        super(vertx, conf);
        this.map = Objects.requireNonNull(map, "map");
    }

    @Override
    public void listen(@NotNull EventBus eventBus) {
        initCodecs(eventBus);

        eventBus.consumer(GET_ADDRESS, this::get);
        eventBus.consumer(GET_MANY_ADDRESS, this::getMany);
        eventBus.consumer(ADD_ADDRESS, this::add);
    }

    protected static void initCodecs(@NotNull EventBus eventBus){
        eventBus.registerCodec(new StopMessageCodec());
        eventBus.registerCodec(new StopsMessageCodec());
        eventBus.registerCodec(new CellMessageCodec());
        eventBus.registerCodec(new CellsMessageCodec());
    }

    private void get(@NotNull Message<Cell> m) {
        map.get(m.body()).setHandler(r -> {
            if (r.succeeded()) {
                m.reply(new Stops(r.result()));
            } else {
                m.fail(1, "failed to get from multimap");
                logger.error("failed to getMany from multimap", new Exception());
            }
        });
    }

    private void getMany(@NotNull Message<Cells> m) {
        final List<Future<Collection<Stop>>> futures = getManyFutures(m.body());
        //noinspection unchecked
        final CompositeFuture all = CompositeFuture.all((List) futures);

        all.setHandler(r -> {
            if (r.succeeded()) {
                m.reply(toStops(futures));
            } else {
                m.fail(2, "failed to getMany from multimap");
                logger.error("failed to getMany from multimap", new Exception());
            }
        });
    }

    @NotNull
    private Stops toStops(@NotNull List<Future<Collection<Stop>>> futures) {
        final List<Stop> result = new ArrayList<>();
        for (Future<Collection<Stop>> f : futures) {
            result.addAll(f.result());
        }
        return new Stops(result);
    }

    @NotNull
    private List<Future<Collection<Stop>>> getManyFutures(@NotNull Cells keys) {
        final List<Future<Collection<Stop>>> result = new ArrayList<>();
        for (Cell key : keys) {
            result.add(map.get(key));
        }
        return result;
    }


    private void add(@NotNull Message<Stop> m) {
        final Stop stop = m.body();
        map.add(Cell.from(stop.getLocation()), stop).setHandler(r -> {
            if (r.succeeded()) {
                m.reply(r.result());
            } else {
                m.fail(3, "failed to add");
            }
        });
    }
}
