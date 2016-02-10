package com.uber.departure.times.mock;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.uber.departure.times.common.SpringVerticle;
import com.uber.departure.times.common.SpringVerticleConnector;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class VertxProvider {
    private final Vertx vertx;

    public VertxProvider() throws InterruptedException {
        final Future<Vertx> future = Future.future();
        Vertx.clusteredVertx(
                new VertxOptions(),
                future.completer()
        );
        while (!future.isComplete()){
            Thread.sleep(100);
        }
        vertx = future.result();
    }

    @Bean(name = SpringVerticle.VERTX_BEAN)
    @NotNull
    public Vertx getVertx() {
        return vertx;
    }

    @Bean(name = SpringVerticle.VERTX_CONTEXT_BEAN)
    @NotNull
    public Context getContext() {
        return vertx.getOrCreateContext();
    }

    @Bean(name = SpringVerticle.EVENT_BUS_BEAN)
    @NotNull
    public EventBus getEventBus() {
        return vertx.eventBus();
    }

    @Bean(name = SpringVerticle.CONNECTOR_BEAN)
    @NotNull
    public SpringVerticleConnector getConnector() {
        return new SpringVerticleConnector();
    }
}
