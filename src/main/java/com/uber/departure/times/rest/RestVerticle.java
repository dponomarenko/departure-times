package com.uber.departure.times.rest;

import com.uber.departure.times.common.SpringVerticle;

/**
 * @author Danila Ponomarenko
 */
public final class RestVerticle extends SpringVerticle {
    public RestVerticle() {
        super("rest-context.xml");
    }
}
