package com.uber.departure.times;

import com.uber.departure.times.crawler.nextbus.NextBusCrawlerVerticle;
import com.uber.departure.times.hub.server.HubVerticle;
import com.uber.departure.times.rest.RestVerticle;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
public final class HerokuRunner {
    private static final Logger logger = LoggerFactory.getLogger(HerokuRunner.class);

    public static void main(String[] args) {
        Vertx.clusteredVertx(new VertxOptions(), r -> {
            if (r.succeeded()) {
                final Vertx vertx = r.result();
                final String port = System.getenv("PORT");
                if (port != null && !port.isEmpty()) {
                    final Context ctx = vertx.getOrCreateContext();
                    ctx.config().put("rest.http.port", Integer.parseInt(port));
                }

                vertx.deployVerticle(new HubVerticle());
                vertx.deployVerticle(new RestVerticle());
                vertx.deployVerticle(new NextBusCrawlerVerticle());
            } else {
                logger.error("Failed to start vertx", r.cause());
            }
        });
    }
}
