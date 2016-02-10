package com.uber.departure.times.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.uber.departure.times.common.SpringVerticleConnector;
import com.uber.departure.times.common.StartupLoggingVerticle;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class HttpServerFactory {
    private static final Logger logger = LoggerFactory.getLogger(StartupLoggingVerticle.class);

    public static final String BEAN_NAME = "httpServer";

    @Autowired
    private Vertx vertx;
    @Autowired
    private RestConfiguration conf;
    @Autowired
    private Router router;
    @Autowired
    private SpringVerticleConnector connector;

    @Bean(name = BEAN_NAME)
    public HttpServer create() {
        final Future<HttpServer> future = Future.future();
        connector.registerStartFuture(future);
        return vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(conf.getHttpPort(),
                        result -> {
                            if (result.succeeded()) {
                                future.complete(result.result());
                                logger.info("Http server started on port:" + conf.getHttpPort());
                            } else {
                                future.fail(result.cause());
                            }
                        }
                );
    }
}
