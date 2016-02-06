package com.uber.departure.times.rest.server.http;

import org.apache.commons.configuration.Configuration;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class HttpServerConfiguration {
    private static final String PREFIX = "http.server.";

    private Configuration conf;

    @Autowired
    public HttpServerConfiguration(@NotNull Configuration configuration) {
        this.conf = configuration;
    }

    public int getPort() {
        return conf.getInt(PREFIX + "port");
    }
}
