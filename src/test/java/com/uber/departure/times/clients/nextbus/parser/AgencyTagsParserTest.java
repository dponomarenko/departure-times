package com.uber.departure.times.clients.nextbus.parser;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.uber.departure.times.mock.DataProviderMockFactory;

import io.vertx.core.buffer.Buffer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author Danila Ponomarenko
 */
public final class AgencyTagsParserTest extends AParserTestClass {
    @Autowired
    private AgencyTagsParser parser;

    @Test
    public void test() throws IOException {
        final Collection<String> tags = parser.apply(Buffer.buffer(load("json/agencyList.json")));
        Assert.assertThat(new HashSet<>(tags), is(equalTo(DataProviderMockFactory.AGENCIES)));
    }
}
