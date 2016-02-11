package com.uber.departure.times.hub.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.vertx.core.Context;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class HubConfiguration {
    @Autowired
    private Context context;

    public int getSearchRadius() {
        return Math.min(context.config().getInteger("hub.search.radius.meters", 300), 500);
    }

    public long getCacheTTLMs() {
        return context.config().getInteger("hub.predictions.cache.ttl.ms", 60000);
    }
}
