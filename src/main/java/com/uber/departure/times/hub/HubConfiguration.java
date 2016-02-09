package com.uber.departure.times.hub;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.common.VerticleConfiguration;

import io.vertx.core.Context;

/**
 * @author Danila Ponomarenko
 */
public final class HubConfiguration extends VerticleConfiguration {
    public HubConfiguration(@NotNull Context context) {
        super(context);
    }

    public int getSearchRadius() {
        return Math.min(config().getInteger("hub.search.radius.meters", 300), 500);
    }
}
