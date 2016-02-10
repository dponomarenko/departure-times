package com.uber.departure.times.crawler.common;

import java.util.Collection;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uber.departure.times.clients.DataProviderClient;
import com.uber.departure.times.common.Publisher;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
@Component(RootCrawler.BEAN_NAME)
public final class RootCrawler implements Publisher<String> {
    private static final Logger logger = LoggerFactory.getLogger(RootCrawler.class);

    public static final String BEAN_NAME = "rootCrawler";

    @Autowired
    private DataProviderClient client;
    @Autowired
    private EventBus eventBus;

    public void crawl() {
        client.getAgencyTags().setHandler(r -> {
            if (r.succeeded()) {
                process(r.result());
            }
        });
    }

    private static final String AGENCIES_ADDRESS = "crawler.agencies";

    @Override
    public void subscribe(@NotNull Consumer<String> agencyConsumer) {
        eventBus.consumer(RootCrawler.AGENCIES_ADDRESS, r -> {
            agencyConsumer.accept((String) r.body());
        });
    }

    private void process(@NotNull Collection<String> tags) {
        logger.error(tags.size() + " agencies loaded");
        for (String tag : tags) {
            eventBus.send(AGENCIES_ADDRESS, tag);
        }
    }
}
