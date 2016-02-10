package com.uber.departure.times.crawler.nextbus;

import com.uber.departure.times.common.SpringVerticle;

/**
 * @author Danila Ponomarenko
 */
public final class NextBusCrawlerVerticle extends SpringVerticle {
    public NextBusCrawlerVerticle() {
        super("crawler-context.xml");
    }
}