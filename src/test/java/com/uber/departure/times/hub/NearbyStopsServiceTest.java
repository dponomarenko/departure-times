package com.uber.departure.times.hub;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.uber.departure.times.AVertxTestClass;
import com.uber.departure.times.common.pojo.Location;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.common.pojo.StopId;
import com.uber.departure.times.hub.client.StopLocationClient;
import com.uber.departure.times.hub.server.HubConfiguration;
import com.uber.departure.times.hub.server.NearbyStopsService;
import com.uber.departure.times.mock.EntityHelper;
import com.uber.departure.times.mock.FutureHelper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author Danila Ponomarenko
 */
public final class NearbyStopsServiceTest extends AVertxTestClass {
    @Autowired
    private NearbyStopsService service;
    @Autowired
    private StopLocationClient storage;
    @Autowired
    private HubConfiguration conf;

    @Test
    public void testNoDataDetect() {
        final Map<StopId, Pair<Stop, Integer>> result = FutureHelper.wait(service.detect(EntityHelper.randomLocation()));
        Assert.assertThat(result.isEmpty(), is(equalTo(true)));
    }

    @Test
    public void testAddStopDetectItsLocation() {
        final Stop stop = EntityHelper.randomStop();
        FutureHelper.wait(storage.add(stop));

        final Map<StopId, Pair<Stop, Integer>> result = FutureHelper.wait(service.detect(stop.getLocation()));
        Assert.assertThat(result.isEmpty(), is(equalTo(false)));
        Assert.assertThat(result.get(stop.getStopId()).getValue(), is(equalTo(0)));
    }

    @Test
    public void testAddStopDetectNearbyLocation() {
        final Stop stop = EntityHelper.randomStop();
        FutureHelper.wait(storage.add(stop));
        final Location stopLocation = stop.getLocation();
        for (int i = 0; i < 100; i++) {
            final ThreadLocalRandom rnd = ThreadLocalRandom.current();
            final Location location = stopLocation.go(rnd.nextInt(conf.getSearchRadius()), rnd.nextDouble());

            final Map<StopId, Pair<Stop, Integer>> result = FutureHelper.wait(service.detect(stop.getLocation()));
            Assert.assertThat(result.isEmpty(), is(equalTo(false)));
            Assert.assertThat(result.get(stop.getStopId()).getValue(), is(equalTo(Location.distance(stopLocation, location))));
        }
    }
}
