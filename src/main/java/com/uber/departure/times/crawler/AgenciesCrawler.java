package com.uber.departure.times.crawler;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.clients.ProviderClient;
import com.uber.departure.times.common.VerticleBean;
import com.uber.departure.times.pojo.Route;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
public final class AgenciesCrawler extends VerticleBean<CrawlerConfiguration> implements Consumer<String> {
    private static final Logger logger = LoggerFactory.getLogger(RootCrawler.class);

    private final ProviderClient client;

    public AgenciesCrawler(@NotNull ProviderClient client, @NotNull Vertx vertx, @NotNull CrawlerConfiguration conf) {
        super(vertx, conf);
        this.client = Objects.requireNonNull(client, "client");
    }

    @Override
    public void accept(@NotNull String agencyTag) {
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

    public void subscribe(@NotNull Consumer<Route> routeConsumer) {
        vertx.eventBus().consumer(ROUTES_ADDRESS, r -> {
            routeConsumer.accept((Route) r.body());
        });
    }
}
