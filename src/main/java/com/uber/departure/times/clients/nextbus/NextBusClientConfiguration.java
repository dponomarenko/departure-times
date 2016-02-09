package com.uber.departure.times.clients.nextbus;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.common.VerticleConfiguration;

import io.vertx.core.Context;

/**
 * @author Danila Ponomarenko
 */
public final class NextBusClientConfiguration extends VerticleConfiguration {
    public NextBusClientConfiguration(@NotNull Context context) {
        super(context);
    }

    @NotNull
    public String getNextBusFeedURI() {
        return config().getString("client.nextbus.host", "http://webservices.nextbus.com/service/publicJSONFeed");
    }
}
