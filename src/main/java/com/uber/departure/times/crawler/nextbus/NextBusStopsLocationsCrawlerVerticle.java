package com.uber.departure.times.crawler.nextbus;

import com.uber.departure.times.clients.ProviderClient;
import com.uber.departure.times.clients.nextbus.NextBusClientConfiguration;
import com.uber.departure.times.clients.nextbus.client.NextBusHttpClient;
import com.uber.departure.times.common.StartupLoggingVerticle;
import com.uber.departure.times.crawler.AgenciesCrawler;
import com.uber.departure.times.crawler.CrawlerConfiguration;
import com.uber.departure.times.crawler.RootCrawler;
import com.uber.departure.times.crawler.RoutesCrawler;
import com.uber.departure.times.hub.service.location.StopLocationClient;

/**
 * @author Danila Ponomarenko
 */
public final class NextBusStopsLocationsCrawlerVerticle extends StartupLoggingVerticle {
    @Override
    public void start() throws Exception {
        final NextBusClientConfiguration providerConf = new NextBusClientConfiguration(context);
        final CrawlerConfiguration conf = new CrawlerConfiguration(context);
        final ProviderClient client = new NextBusHttpClient(vertx, providerConf);

        final RootCrawler rootCrawler = new RootCrawler(client, vertx, conf);
        final AgenciesCrawler agenciesCrawler = new AgenciesCrawler(client, vertx, conf);
        rootCrawler.subscribe(agenciesCrawler);
        final RoutesCrawler routesCrawler = new RoutesCrawler(client, new StopLocationClient(vertx.eventBus()), vertx, conf);
        agenciesCrawler.subscribe(routesCrawler);

        rootCrawler.crawl();
    }
}