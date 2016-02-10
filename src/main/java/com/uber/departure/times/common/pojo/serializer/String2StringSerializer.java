package com.uber.departure.times.common.pojo.serializer;

import com.uber.departure.times.common.map.hazelcast.Serializer;

/**
 * @author Danila Ponomarenko
 */
public final class String2StringSerializer implements Serializer<String, String> {
    public static final Serializer<String, String> SERIALIZER = new String2StringSerializer();

    @Override
    public String to(String value) {
        return value;
    }

    @Override
    public String from(String value) {
        return value;
    }
}