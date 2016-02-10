package com.uber.departure.times.hub.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.uber.departure.times.common.map.AsyncMap;
import com.uber.departure.times.common.map.hazelcast.HazelcastMap;
import com.uber.departure.times.common.pojo.ProvidedPredictions;
import com.uber.departure.times.common.pojo.StopId;
import com.uber.departure.times.common.pojo.serializer.ProvidedPredictions2StringSerializer;
import com.uber.departure.times.common.pojo.serializer.StopId2StringSerializer;

import io.vertx.core.Vertx;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class PredictionsCacheFactory {
    public static final String BEAN_NAME = "predictionsCache";

    @Autowired
    private Vertx vertx;
    @Autowired
    private HubConfiguration conf;

    @Bean(name = BEAN_NAME)
    public AsyncMap<StopId, ProvidedPredictions> create() {
        final HazelcastInstance hazelcast = Hazelcast.getAllHazelcastInstances().iterator().next();
        return new HazelcastMap<>(
                vertx,
                hazelcast.getMap("hub.predictions.cache"),
                StopId2StringSerializer.SERIALIZER,
                ProvidedPredictions2StringSerializer.SERIALIZER,
                conf.getCacheLockTimeoutMs()
        );
    }
}
