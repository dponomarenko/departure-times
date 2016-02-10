package com.uber.departure.times.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.uber.departure.times.common.map.AsyncMultiMap;
import com.uber.departure.times.common.map.hazelcast.HazelcastMultiMap;
import com.uber.departure.times.common.pojo.serializer.String2StringSerializer;

import io.vertx.core.Vertx;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class HazelcastMultiMapFactory {
    public static final String BEAN_NAME = "testMultiMap";

    @Autowired
    private Vertx vertx;

    @Bean(name = BEAN_NAME)
    public AsyncMultiMap<String, String> create() {
        final HazelcastInstance hazelcast = Hazelcast.getAllHazelcastInstances().iterator().next();
        return new HazelcastMultiMap<>(
                vertx,
                hazelcast.getMultiMap("hazelcast.test.mmap"),
                String2StringSerializer.SERIALIZER,
                String2StringSerializer.SERIALIZER
        );
    }
}
