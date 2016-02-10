package com.uber.departure.times.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class HttpClientFactory {
    @Autowired
    private Vertx vertx;

    @Bean
    public HttpClient create() {
        return vertx.createHttpClient();
    }
}
