package com.uber.departure.times.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.uber.departure.times.common.map.AsyncMap;
import com.uber.departure.times.common.map.hazelcast.HazelcastMap;
import com.uber.departure.times.common.pojo.serializer.String2StringSerializer;

import io.vertx.core.Vertx;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class HazelcastMapFactory {
    public static final String BEAN_NAME = "testMap";

    @Autowired
    private Vertx vertx;

    @Bean(name = BEAN_NAME)
    public AsyncMap<String, String> create() {
        final HazelcastInstance hazelcast = Hazelcast.getAllHazelcastInstances().iterator().next();
        return new HazelcastMap<>(
                vertx,
                hazelcast.getMap("hazelcast.test.map"),
                String2StringSerializer.SERIALIZER,
                String2StringSerializer.SERIALIZER
        );
    }
}
