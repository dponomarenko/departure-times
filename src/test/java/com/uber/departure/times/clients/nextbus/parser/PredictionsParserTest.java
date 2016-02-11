package com.uber.departure.times.clients.nextbus.parser;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.uber.departure.times.common.pojo.ProvidedPredictions;
import com.uber.departure.times.mock.DataProviderMockFactory;

import io.vertx.core.buffer.Buffer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author Danila Ponomarenko
 */
public final class PredictionsParserTest extends AParserTestClass {
    @Autowired
    private PredictionsParser parser;

    @Test
    public void test() throws IOException {
        final ProvidedPredictions predictions = parser.parse(DataProviderMockFactory.AC_TRANSIT_39_5194_STOP_ID, Buffer.buffer(load("json/predictions.json")));
        Assert.assertThat(predictions, is(equalTo(DataProviderMockFactory.AC_TRANSIT_39_5194_STOP_ID_PREDICTIONS)));
    }

    private static final ProvidedPredictions NO_PREDICTIONS = new ProvidedPredictions(
            "San Francisco Muni",
            "K-Owl",
            "Ocean Ave & Lee St",
            "Inbound to Embarcadero Station",
            new long[0]
    );

    @Test
    public void testNoPredictions() throws IOException {
        final ProvidedPredictions predictions = parser.parse(DataProviderMockFactory.AC_TRANSIT_39_5194_STOP_ID, Buffer.buffer(load("json/no-predictions.json")));
        Assert.assertThat(predictions, is(equalTo(NO_PREDICTIONS)));
    }
}
