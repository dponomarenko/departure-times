package com.uber.departure.times.crawler;

import java.util.Objects;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.clients.ProviderClient;
import com.uber.departure.times.common.VerticleBean;
import com.uber.departure.times.hub.service.location.StopLocationClient;
import com.uber.departure.times.pojo.Route;
import com.uber.departure.times.pojo.Stop;
import com.uber.departure.times.pojo.Stops;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
public final class RoutesCrawler extends VerticleBean<CrawlerConfiguration> implements Consumer<Route> {
    private static final Logger logger = LoggerFactory.getLogger(RootCrawler.class);

    private final ProviderClient client;
    private final StopLocationClient stopLocationClient;

    public RoutesCrawler(@NotNull ProviderClient client, @NotNull StopLocationClient stopLocationClient, @NotNull Vertx vertx, @NotNull CrawlerConfiguration conf) {
        super(vertx, conf);
        this.stopLocationClient = Objects.requireNonNull(stopLocationClient, "stopLocationClient");
        this.client = Objects.requireNonNull(client, "client");
    }

    @Override
    public void accept(@NotNull Route route) {
        client.getStops(route).setHandler(r -> {
            if (r.succeeded()) {
                process(route, r.result());
            }
        });
    }

    private void process(@NotNull Route route, @NotNull Stops stops) {
        logger.error(stops.size() + " stops loaded for agency:" + route.getAgencyTag() + " route:" + route.getRouteTag());
        for (Stop stop : stops) {
            stopLocationClient.add(stop);
        }
    }
}