package com.uber.departure.times.hub.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uber.departure.times.clients.DataProviderClient;
import com.uber.departure.times.common.map.AsyncMap;
import com.uber.departure.times.common.pojo.ProvidedPredictions;
import com.uber.departure.times.common.pojo.StopId;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class PredictionsLoaderService {
    @Autowired
    private DataProviderClient dataProviderClient;
    @Resource(name = PredictionsCacheFactory.BEAN_NAME)
    private AsyncMap<StopId, ProvidedPredictions> map;
    @Autowired
    private HubConfiguration conf;

    @NotNull
    public Future<Map<StopId, ProvidedPredictions>> load(@NotNull Set<StopId> stops) {
        final Map<StopId, Future<ProvidedPredictions>> futures = new HashMap<>();
        for (StopId s : stops) {
            futures.put(s, map.computeIfAbsent(s, id -> dataProviderClient.predict(s), conf.getCacheTTLMs()));
        }

        if(futures.isEmpty()){
            return Future.succeededFuture(Collections.emptyMap());
        }

        //noinspection unchecked
        final CompositeFuture all = CompositeFuture.all(new ArrayList(futures.values()));
        final Future<Map<StopId, ProvidedPredictions>> result = Future.future();
        all.compose(r -> {
            final Map<StopId, ProvidedPredictions> predictions = new HashMap<>();
            for (Map.Entry<StopId, Future<ProvidedPredictions>> f : futures.entrySet()) {
                predictions.put(f.getKey(), f.getValue().result());
            }
            result.complete(predictions);
        }, result);
        return result;
    }
}
