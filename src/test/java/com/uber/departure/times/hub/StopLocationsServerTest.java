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

import io.vertx.core.Future;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author Danila Ponomarenko
 */
public final class StopLocationsServerTest extends AVertxTestClass {
    @Autowired
    private StopLocationClient client;

    @Test
    public void testAdd1GetAll() {
        final Stop stop = EntityHelper.randomStop();
        client.add(stop);
        final Cell cell = Cell.from(stop.getLocation());
        final Future<Stops> f = client.getMany(Collections.singleton(cell));
        f.setHandler(r -> {
            if (r.succeeded()) {
                final Stops stops = r.result();
                Assert.assertThat(stops.size(), is(equalTo(1)));
                Assert.assertThat(stops.iterator().next(), is(equalTo(stop)));
            } else {
                Assert.fail(r.cause().toString());
            }
        });
    }

    @Test
    public void testAdd10GetAll() {
        final Set<Stop> stops = new HashSet<>();
        final Set<Cell> cells = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            final Stop stop = EntityHelper.randomStop();
            client.add(stop);
            cells.add(Cell.from(stop.getLocation()));
            stops.add(stop);
        }


        final Future<Stops> f = client.getMany(cells);
        f.setHandler(r -> {
            if (r.succeeded()) {
                final Stops s = r.result();
                Assert.assertThat(s.size(), is(equalTo(stops.size())));
            } else {
                Assert.fail(r.cause().toString());
            }
        });
    }

}
