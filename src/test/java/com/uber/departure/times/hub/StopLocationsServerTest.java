package com.uber.departure.times.hub;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.uber.departure.times.AVertxTestClass;
import com.uber.departure.times.common.pojo.Cell;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.common.pojo.Stops;
import com.uber.departure.times.hub.client.StopLocationClient;
import com.uber.departure.times.mock.EntityHelper;
import com.uber.departure.times.mock.FutureHelper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author Danila Ponomarenko
 */
public final class StopLocationsServerTest extends AVertxTestClass {
    @Autowired
    private StopLocationClient storage;

    @Test
    public void testNoAddGetMany() {
        final Stop stop = EntityHelper.randomStop();
        final Cell cell = Cell.from(stop.getLocation());
        final Stops stops = FutureHelper.wait(storage.getMany(Collections.singleton(cell)));
        Assert.assertThat(stops.size(), is(equalTo(0)));
    }

    @Test
    public void testAdd1GetMany() {
        final Stop stop = EntityHelper.randomStop();
        FutureHelper.wait(storage.add(stop));
        final Cell cell = Cell.from(stop.getLocation());
        final Stops stops = FutureHelper.wait(storage.getMany(Collections.singleton(cell)));
        Assert.assertThat(stops.size(), is(equalTo(1)));
        Assert.assertThat(stops.iterator().next(), is(equalTo(stop)));

    }

    @Test
    public void testAdd10GetMany() {
        final Set<Stop> stops = new HashSet<>();
        final Set<Cell> cells = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            final Stop stop = EntityHelper.randomStop();
            FutureHelper.wait(storage.add(stop));
            cells.add(Cell.from(stop.getLocation()));
            stops.add(stop);
        }


        final Stops s = FutureHelper.wait(storage.getMany(cells));
        Assert.assertThat(s.size(), is(equalTo(stops.size())));
    }

}
