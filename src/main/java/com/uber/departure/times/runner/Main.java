package com.uber.departure.times.runner;

import com.uber.departure.times.hub.HubVerticle;
import com.uber.departure.times.rest.RestVerticle;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 * @author Danila Ponomarenko
 */
public final class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final long startMillis = System.currentTimeMillis();
        new HazelcastClusterManager();
        Vertx.clusteredVertx(
                new VertxOptions(),
                event -> {
                    if (event.succeeded()) {
                        final Vertx vertx = event.result();
                        vertx.deployVerticle(new RestVerticle());
                        vertx.deployVerticle(new HubVerticle());
                        logger.info("VERTX_STARTED in " + (System.currentTimeMillis() - startMillis) + "ms.");
                    } else {
                        logger.info("VERTX_START failed ", event.cause());
                    }
                }
        );
    }
}
