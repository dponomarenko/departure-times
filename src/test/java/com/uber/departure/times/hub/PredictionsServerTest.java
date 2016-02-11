package com.uber.departure.times.hub;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.uber.departure.times.AVertxTestClass;
import com.uber.departure.times.common.pojo.Prediction;
import com.uber.departure.times.common.pojo.Predictions;
import com.uber.departure.times.common.pojo.ProvidedPredictions;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.hub.client.PredictionsClient;
import com.uber.departure.times.mock.EntityHelper;
import com.uber.departure.times.mock.FutureHelper;
import com.uber.departure.times.mock.TestStorage;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author Danila Ponomarenko
 */
public final class PredictionsServerTest extends AVertxTestClass {
    @Autowired
    private TestStorage storage;
    @Autowired
    private PredictionsClient client;

    @Test
    public void testNoData() {
        final Predictions result = FutureHelper.wait(client.get(EntityHelper.randomLocation()));
        Assert.assertThat(result.isEmpty(), is(equalTo(true)));
    }

    @Test
    public void testPredictedData() {
        final Stop stop = EntityHelper.randomStop();
        final ProvidedPredictions predictions = new ProvidedPredictions("agency", "route", "stop", "direction", new long[]{System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)});
        storage.addSync(stop, predictions);

        final Predictions result = FutureHelper.wait(client.get(stop.getLocation()));
        Assert.assertThat(result.size(), is(equalTo(1)));
        final Prediction p = result.iterator().next();
        Assert.assertThat(p.getPredictions()[0], is(equalTo(5)));
        }
    }
