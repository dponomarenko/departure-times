package com.uber.departure.times.hub;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.uber.departure.times.clients.nextbus.NextBusClientConfiguration;
import com.uber.departure.times.clients.nextbus.client.NextBusHttpClient;
import com.uber.departure.times.common.StartupLoggingVerticle;
import com.uber.departure.times.common.map.AsyncMultiMap;
import com.uber.departure.times.common.map.hazelcast.HazelcastMultiMap;
import com.uber.departure.times.hub.service.location.StopLocationClient;
import com.uber.departure.times.hub.service.location.StopLocationsService;
import com.uber.departure.times.hub.service.prediction.PredictionsService;
import com.uber.departure.times.pojo.Cell;
import com.uber.departure.times.pojo.Stop;
import com.uber.departure.times.pojo.serializer.Cell2StringSerializer;
import com.uber.departure.times.pojo.serializer.Stop2StringSerializer;

import io.vertx.core.eventbus.EventBus;

/**
 * @author Danila Ponomarenko
 */
public final class HubVerticle extends StartupLoggingVerticle {
    @Override
    public void start() throws Exception {
        final HubConfiguration conf = new HubConfiguration(context);
        final HazelcastInstance hazelcast = Hazelcast.getAllHazelcastInstances().iterator().next();

        final EventBus eventBus = vertx.eventBus();
        final AsyncMultiMap<Cell, Stop> map = new HazelcastMultiMap<>(
                "hub.locations",
                vertx,
                hazelcast,
                Cell2StringSerializer.SERIALIZER,
                Stop2StringSerializer.SERIALIZER
        );
        new StopLocationsService(vertx, map, conf).listen(eventBus);
        new PredictionsService(vertx,
                new StopLocationClient(eventBus),
                new NextBusHttpClient(vertx, new NextBusClientConfiguration(context)),
                conf
        ).listen(eventBus);
    }
}