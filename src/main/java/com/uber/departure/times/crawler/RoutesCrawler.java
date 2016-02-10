package com.uber.departure.times.crawler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uber.departure.times.clients.DataProviderClient;
import com.uber.departure.times.common.Publisher;
import com.uber.departure.times.common.pojo.Route;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.common.pojo.Stops;
import com.uber.departure.times.hub.client.StopLocationClient;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class RoutesCrawler {
    private static final Logger logger = LoggerFactory.getLogger(RootCrawler.class);

    @Resource(name = AgenciesCrawler.BEAN_NAME)
    private Publisher<Route> routes;
    @Autowired
    private DataProviderClient client;
    @Autowired
    private StopLocationClient storage;

    @PostConstruct
    private void init() {
        routes.subscribe(this::accept);
    }

    private void accept(@NotNull Route route) {
        client.getStops(route).setHandler(r -> {
            if (r.succeeded()) {
                process(route, r.result());
            }
        });
    }

    private void process(@NotNull Route route, @NotNull Stops stops) {
        logger.error(stops.size() + " stops loaded for agency:" + route.getAgencyTag() + " route:" + route.getRouteTag());
        for (Stop stop : stops) {
            storage.add(stop);
        }
    }
}