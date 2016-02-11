package com.uber.departure.times.clients.nextbus;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.vertx.core.Context;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class NextBusClientConfiguration {
    @Autowired
    private Context context;

    @NotNull
    public String getNextBusFeedURI() {
        return context.config().getString("client.nextbus.uri", "/service/publicJSONFeed");
    }

    @NotNull
    public String getNextBusHost() {
        return context.config().getString("client.nextbus.host", "webservices.nextbus.com");
    }

    public int getNextBusPort() {
        return context.config().getInteger("client.nextbus.port", 80);
    }
}
