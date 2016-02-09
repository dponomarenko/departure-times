package com.uber.departure.times.rest;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.common.StartupLoggingVerticle;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
public final class RestVerticle extends StartupLoggingVerticle {
    private static final Logger logger = LoggerFactory.getLogger(RestVerticle.class);

    @Override
    public void startOverride(@NotNull Future<Void> startFuture) throws Exception {
        final RestConfiguration conf = new RestConfiguration(context);
        vertx.createHttpServer()
                .requestHandler(new RestRouter(vertx, conf))
                .listen(conf.getHttpPort(),
                        result -> {
                            if (result.succeeded()) {
                                startFuture.complete();
                                logger.info("Http server started on port:" + conf.getHttpPort());
                            } else {
                                startFuture.fail(result.cause());
                            }
                        }
                );
    }
}
