package com.uber.departure.times.hub.client;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uber.departure.times.common.component.MessageCodecRegistrar;
import com.uber.departure.times.common.pojo.Cell;
import com.uber.departure.times.common.pojo.Cells;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.common.pojo.Stops;
import com.uber.departure.times.common.pojo.codec.CellsMessageCodec;
import com.uber.departure.times.common.pojo.codec.StopMessageCodec;
import com.uber.departure.times.common.pojo.codec.StopsMessageCodec;

import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;

/**
 * @author Danila Ponomarenko
 */
@Component
public class StopLocationClient {
    protected static final String GET_MANY_ADDRESS = "hub.locations.dao.get.many";
    protected static final String ADD_ADDRESS = "hub.locations.dao.add";

    @Autowired
    protected EventBus eventBus;
    @Autowired
    private MessageCodecRegistrar registrar;

    @PostConstruct
    protected void init() {
        registrar.register(Stop.class, new StopMessageCodec());
        registrar.register(Stops.class, new StopsMessageCodec());
        registrar.register(Cells.class, new CellsMessageCodec());
    }

    @NotNull
    public Future<Stops> getMany(@NotNull Set<Cell> cells) {
        final Future<Stops> result = Future.future();
        eventBus.send(GET_MANY_ADDRESS, new Cells(cells), r -> {
            if (r.succeeded()) {
                result.complete((Stops) r.result().body());
            } else {
                result.fail(r.cause());
            }
        });
        return result;
    }

    @NotNull
    public Future<Boolean> add(@NotNull Stop stop) {
        final Future<Boolean> result = Future.future();
        eventBus.send(ADD_ADDRESS, stop, r -> {
            if (r.succeeded()) {
                result.complete((Boolean) r.result().body());
            } else {
                result.fail(r.cause());
            }
        });
        return result;
    }
}
