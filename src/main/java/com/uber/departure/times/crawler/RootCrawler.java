package com.uber.departure.times.crawler;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.clients.ProviderClient;
import com.uber.departure.times.common.VerticleBean;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
public final class RootCrawler extends VerticleBean<CrawlerConfiguration> {
    private static final Logger logger = LoggerFactory.getLogger(RootCrawler.class);

    private final ProviderClient client;

    public RootCrawler(@NotNull ProviderClient client, @NotNull Vertx vertx, @NotNull CrawlerConfiguration conf) {
        super(vertx, conf);
        this.client = Objects.requireNonNull(client, "client");
    }

    public void crawl() {
        client.getAgencyTags().setHandler(r -> {
            if (r.succeeded()) {
                process(r.result());
            }
        });
    }

    private static final String AGENCIES_ADDRESS = "crawler.agencies";

    public void subscribe(@NotNull Consumer<String> agencyConsumer) {
        vertx.eventBus().consumer(RootCrawler.AGENCIES_ADDRESS, r -> {
            agencyConsumer.accept((String) r.body());
        });
    }

    private void process(@NotNull Collection<String> tags) {
        logger.error(tags.size() + " agencies loaded");
        for (String tag : tags) {
            vertx.eventBus().send(AGENCIES_ADDRESS, tag);
        }
    }
}
