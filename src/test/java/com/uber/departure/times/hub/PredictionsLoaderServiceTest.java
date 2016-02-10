package com.uber.departure.times.hub;

import java.util.Collections;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.uber.departure.times.AVertxTestClass;
import com.uber.departure.times.clients.DataProviderClient;
import com.uber.departure.times.common.pojo.ProvidedPredictions;
import com.uber.departure.times.common.pojo.StopId;
import com.uber.departure.times.hub.server.PredictionsLoaderService;
import com.uber.departure.times.mock.DataProviderMockFactory;
import com.uber.departure.times.mock.FutureHelper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author Danila Ponomarenko
 */
public final class PredictionsLoaderServiceTest extends AVertxTestClass {
    @Autowired
    private PredictionsLoaderService predictor;
    @Autowired
    private DataProviderClient client;

    @Test
    public void testNoData() {
        final StopId stopId = DataProviderMockFactory.EMPTY_STOP_ID;
        final Map<StopId, ProvidedPredictions> map = FutureHelper.wait(predictor.load(Collections.singleton(stopId)));
        Mockito.verify(client, Mockito.times(1)).predict(stopId);
        Assert.assertThat(map.get(stopId), is(equalTo(null)));
    }

    @Test
    public void testStopIdCached() {
        final StopId stopId = DataProviderMockFactory.AC_TRANSIT_39_5194_STOP_ID;
        final Map<StopId, ProvidedPredictions> map = FutureHelper.wait(predictor.load(Collections.singleton(stopId)));
        Mockito.verify(client, Mockito.times(1)).predict(stopId);
        Assert.assertThat(map.get(stopId), is(equalTo(DataProviderMockFactory.AC_TRANSIT_39_5194_STOP_ID_PREDICTIONS)));

        final Map<StopId, ProvidedPredictions> cached = FutureHelper.wait(predictor.load(Collections.singleton(stopId)));
        Mockito.verify(client, Mockito.times(1)).predict(stopId);
        Assert.assertThat(cached.get(stopId), is(equalTo(DataProviderMockFactory.AC_TRANSIT_39_5194_STOP_ID_PREDICTIONS)));
    }
}
