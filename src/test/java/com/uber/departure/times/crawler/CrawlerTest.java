package com.uber.departure.times.crawler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.uber.departure.times.AVertxTestClass;
import com.uber.departure.times.common.pojo.Cell;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.common.pojo.Stops;
import com.uber.departure.times.crawler.common.RootCrawler;
import com.uber.departure.times.hub.client.StopLocationClient;
import com.uber.departure.times.mock.DataProviderMockFactory;
import com.uber.departure.times.mock.FutureHelper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author Danila Ponomarenko
 */
public final class CrawlerTest extends AVertxTestClass {

    @Autowired
    private RootCrawler crawler;
    @Autowired
    private StopLocationClient storage;

    @Test
    public void testCrawler() throws InterruptedException {
        crawler.crawl();
        Thread.sleep(TimeUnit.SECONDS.toMillis(3));
        final Set<Stop> stops = DataProviderMockFactory.AC_TRANSIT_39_ROUTE_STOPS;
        final Stop stop = stops.iterator().next();
        final Stops result = FutureHelper.wait(storage.getMany(Collections.singleton(Cell.from(stop.getLocation()))));
        if (result == null){
            Assert.fail();
        }
        Assert.assertThat(new HashSet<>(result.getValues()).contains(stop), is(equalTo(true)));
    }
}
