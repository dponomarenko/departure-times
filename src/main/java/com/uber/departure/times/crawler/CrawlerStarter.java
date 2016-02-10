package com.uber.departure.times.crawler;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uber.departure.times.common.SpringVerticleConnector;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class CrawlerStarter {
    @Autowired
    private SpringVerticleConnector connector;
    @Autowired
    private RootCrawler crawler;

    @PostConstruct
    private void init() {
        connector.startFuture().setHandler(r -> {
            crawler.crawl();
        });
    }
}
