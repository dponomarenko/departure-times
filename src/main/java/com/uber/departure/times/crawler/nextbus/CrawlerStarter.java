package com.uber.departure.times.crawler.nextbus;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uber.departure.times.common.SpringVerticleConnector;
import com.uber.departure.times.crawler.common.RootCrawler;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class CrawlerStarter {
    private static final Logger logger = LoggerFactory.getLogger(CrawlerStarter.class);

    @Autowired
    private SpringVerticleConnector connector;
    @Autowired
    private RootCrawler crawler;

    @PostConstruct
    private void init() {
        connector.startFuture().setHandler(r -> {
            logger.info("Crawler started");
            crawler.crawl();
        });
    }
}
