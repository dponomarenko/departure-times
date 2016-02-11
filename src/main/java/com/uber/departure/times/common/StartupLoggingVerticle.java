package com.uber.departure.times.common;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
public abstract class StartupLoggingVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(StartupLoggingVerticle.class);

    @Override
    public final void start(Future<Void> startFuture) throws Exception {
        final long startMillis = System.currentTimeMillis();
        startFuture.setHandler(r -> {
            logger.info("VERTICLE " + this.getClass().getSimpleName() + " started in " + (System.currentTimeMillis() - startMillis) + "ms.");
        });
        startOverride(startFuture);
    }

    @Override
    public final void stop(Future<Void> stopFuture) throws Exception {
        final long startMillis = System.currentTimeMillis();
        stopFuture.setHandler(r -> {
            logger.info("VERTICLE " + this.getClass().getSimpleName() + " stopped in " + (System.currentTimeMillis() - startMillis) + "ms.");
        });
        stopOverride(stopFuture);
    }

    protected void startOverride(Future<Void> startFuture) throws Exception {
        start();
        startFuture.complete();
    }

    protected void stopOverride(Future<Void> stopFuture) throws Exception {
        stop();
        stopFuture.complete();
    }
}
