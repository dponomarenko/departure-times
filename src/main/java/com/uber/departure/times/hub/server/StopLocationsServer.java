package com.uber.departure.times.hub.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import com.uber.departure.times.common.map.AsyncMultiMap;
import com.uber.departure.times.common.pojo.Cell;
import com.uber.departure.times.common.pojo.Cells;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.common.pojo.Stops;
import com.uber.departure.times.hub.client.StopLocationClient;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class StopLocationsServer extends StopLocationClient {
    private static final Logger logger = LoggerFactory.getLogger(StopLocationsServer.class);

    @Resource(name = StopsStorageFactory.BEAN_NAME)
    private AsyncMultiMap<Cell, Stop> map;

    @PostConstruct
    protected void init() {
        super.init();
        eventBus.consumer(GET_ADDRESS, this::getHandler);
        eventBus.consumer(GET_MANY_ADDRESS, this::getManyHandler);
        eventBus.consumer(ADD_ADDRESS, this::addHandler);
    }

    private void getHandler(@NotNull Message<Cell> m) {
        map.get(m.body()).setHandler(r -> {
            if (r.succeeded()) {
                m.reply(new Stops(r.result()));
            } else {
                m.fail(1, "failed to get from multimap");
                logger.error("failed to getMany from multimap", new Exception());
            }
        });
    }

    private void getManyHandler(@NotNull Message<Cells> m) {
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


    private void addHandler(@NotNull Message<Stop> m) {
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
