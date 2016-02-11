package com.uber.departure.times;

import com.uber.departure.times.crawler.nextbus.NextBusCrawlerVerticle;
import com.uber.departure.times.hub.server.HubVerticle;
import com.uber.departure.times.rest.RestVerticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
public final class HerokuRunner {
    private static final Logger logger = LoggerFactory.getLogger(HerokuRunner.class);

    public static void main(String[] args) {
        new DeploymentOptions(new JsonObject().put("rest.http.port", System.getenv("PORT")));

        Vertx.clusteredVertx(new VertxOptions(), r -> {
            if (r.succeeded()) {
                final Vertx vertx = r.result();
                vertx.deployVerticle(new HubVerticle());
                vertx.deployVerticle(new RestVerticle());
                vertx.deployVerticle(new NextBusCrawlerVerticle());
            } else {
                logger.error("Failed to start vertx", r.cause());
            }
        });
    }
}
