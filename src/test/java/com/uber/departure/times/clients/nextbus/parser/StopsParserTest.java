package com.uber.departure.times.clients.nextbus.parser;

import java.io.IOException;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.uber.departure.times.common.pojo.Stops;
import com.uber.departure.times.mock.DataProviderMockFactory;

import io.vertx.core.buffer.Buffer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author Danila Ponomarenko
 */
public final class StopsParserTest extends AParserTestClass {
    @Autowired
    private StopsParser parser;

    @Test
    public void test() throws IOException {
        final Stops stops = parser.parse(DataProviderMockFactory.AC_TRANSIT_39_ROUTE, Buffer.buffer(load("json/routeConfig.json")));
        Assert.assertThat(new HashSet<>(stops.getValues()), is(equalTo(DataProviderMockFactory.AC_TRANSIT_39_ROUTE_STOPS)));
    }
}
