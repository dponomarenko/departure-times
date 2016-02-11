package com.uber.departure.times.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.vertx.core.Context;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class RestConfiguration {
    @Autowired
    private Context context;

    public int getHttpPort() {
        return context.config().getInteger("rest.http.port", 8081);
    }
}
