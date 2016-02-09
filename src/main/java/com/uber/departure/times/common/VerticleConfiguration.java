package com.uber.departure.times.common;

import org.jetbrains.annotations.NotNull;

import io.vertx.core.Context;
import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public abstract class VerticleConfiguration {
    protected final Context context;

    public VerticleConfiguration(@NotNull Context context) {
        this.context = context;
    }

    @NotNull
    protected JsonObject config(){
        return context.config();
    }
}
