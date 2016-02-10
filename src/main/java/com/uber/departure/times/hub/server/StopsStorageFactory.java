package com.uber.departure.times.hub.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.uber.departure.times.common.map.AsyncMultiMap;
import com.uber.departure.times.common.map.hazelcast.HazelcastMultiMap;
import com.uber.departure.times.common.pojo.Cell;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.common.pojo.serializer.Cell2StringSerializer;
import com.uber.departure.times.common.pojo.serializer.Stop2StringSerializer;

import io.vertx.core.Vertx;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class StopsStorageFactory {
    public static final String BEAN_NAME = "stopsStorage";

    @Autowired
    private Vertx vertx;

    @Bean(name = BEAN_NAME)
    public AsyncMultiMap<Cell, Stop> create() {
        final HazelcastInstance hazelcast = Hazelcast.getAllHazelcastInstances().iterator().next();
        return new HazelcastMultiMap<>(
                "hub.locations",
                vertx,
                hazelcast,
                Cell2StringSerializer.SERIALIZER,
                Stop2StringSerializer.SERIALIZER
        );
    }
}
