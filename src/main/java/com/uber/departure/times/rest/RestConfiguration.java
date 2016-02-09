package com.uber.departure.times.rest;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.common.VerticleConfiguration;

import io.vertx.core.Context;

/**
 * @author Danila Ponomarenko
 */
public final class RestConfiguration extends VerticleConfiguration {
    public RestConfiguration(@NotNull Context context) {
        super(context);
    }

    public int getHttpPort() {
        return config().getInteger("rest.http.port", 8080);
    }
}
