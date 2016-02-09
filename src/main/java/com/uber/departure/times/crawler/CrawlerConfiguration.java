package com.uber.departure.times.crawler;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.common.VerticleConfiguration;

import io.vertx.core.Context;

/**
 * @author Danila Ponomarenko
 */
public final class CrawlerConfiguration extends VerticleConfiguration {
    public CrawlerConfiguration(@NotNull Context context) {
        super(context);
    }

}
