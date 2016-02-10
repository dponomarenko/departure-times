package com.uber.departure.times.mock;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uber.departure.times.clients.DataProviderClient;
import com.uber.departure.times.common.pojo.ProvidedPredictions;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.hub.client.StopLocationClient;

import io.vertx.core.Future;

import static org.mockito.Mockito.when;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class TestStorage {

    @Autowired
    private StopLocationClient storage;
    @Autowired
    private DataProviderClient dataProviderClient;

    public void addSync(@NotNull Stop stop, @NotNull ProvidedPredictions predictions) {
        FutureHelper.wait(storage.add(stop));

        when(dataProviderClient.predict(stop.getStopId())).then(i -> Future.succeededFuture(predictions));
    }
}
