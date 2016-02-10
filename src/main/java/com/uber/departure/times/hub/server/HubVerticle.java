package com.uber.departure.times.hub.server;

import com.uber.departure.times.common.SpringVerticle;

/**
 * @author Danila Ponomarenko
 */
public final class HubVerticle extends SpringVerticle {
    public HubVerticle() {
        super("hub-context.xml");
    }
}