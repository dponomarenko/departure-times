package com.uber.departure.times.crawler;

import java.util.Collection;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uber.departure.times.clients.DataProviderClient;
import com.uber.departure.times.common.Publisher;
import com.uber.departure.times.common.pojo.Route;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
@Component(AgenciesCrawler.BEAN_NAME)
public final class AgenciesCrawler implements Publisher<Route> {
    private static final Logger logger = LoggerFactory.getLogger(RootCrawler.class);

    public static final String BEAN_NAME = "agenciesCrawler";

    @Autowired
    private DataProviderClient client;
    @Resource(name = RootCrawler.BEAN_NAME)
    private Publisher<String> agencies;
    @Autowired
    private Vertx vertx;

    @PostConstruct
    private void init() {
        agencies.subscribe(this::accept);
    }

    private void accept(@NotNull String agencyTag) {
        client.getRouteTags(agencyTag).setHandler(r -> {
            if (r.succeeded()) {
                process(agencyTag, r.result());
            }
        });
    }

    private void process(@NotNull String agencyTag, @NotNull Collection<String> tags) {
        logger.error(tags.size() + " routes loaded for " + agencyTag);
        for (String routeTag : tags) {
            vertx.eventBus().send(ROUTES_ADDRESS, new Route(agencyTag, routeTag));
        }
    }

    private static final String ROUTES_ADDRESS = "crawler.routes";

    @Override
    public void subscribe(@NotNull Consumer<Route> routeConsumer) {
        vertx.eventBus().consumer(ROUTES_ADDRESS, r -> {
            routeConsumer.accept((Route) r.body());
        });
    }
}
