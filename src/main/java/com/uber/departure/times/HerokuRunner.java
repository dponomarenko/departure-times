package com.uber.departure.times;

import org.jetbrains.annotations.NotNull;

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
        Vertx.clusteredVertx(new VertxOptions(), r -> {
            if (r.succeeded()) {
                final Vertx vertx = r.result();
                vertx.deployVerticle(new HubVerticle());
                vertx.deployVerticle(new RestVerticle(), new DeploymentOptions().setConfig(restConfig()));
                vertx.deployVerticle(new NextBusCrawlerVerticle());
            } else {
                logger.error("Failed to start vertx", r.cause());
            }
        });
    }

    @NotNull
    private static JsonObject restConfig() {
        final JsonObject result = new JsonObject();
        final String port = System.getenv("PORT");
        if (port != null && !port.isEmpty()) {
            result.put("rest.http.port", Integer.parseInt(port));
        }
        return result;
    }
}
