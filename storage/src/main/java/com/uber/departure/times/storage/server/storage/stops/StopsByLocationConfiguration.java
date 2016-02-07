package com.uber.departure.times.storage.server.storage.stops;

import org.apache.commons.configuration.Configuration;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Danila Ponomarenko
 */
public final class StopsByLocationConfiguration {
    private static final String PREFIX = "stops.by.location.";

    private Configuration conf;

    @Autowired
    public StopsByLocationConfiguration(@NotNull Configuration configuration) {
        this.conf = configuration;
    }

    public int getRadiusMeters() {
        return Math.min(conf.getInt(PREFIX + "radius.meters"), 500);
    }
}
