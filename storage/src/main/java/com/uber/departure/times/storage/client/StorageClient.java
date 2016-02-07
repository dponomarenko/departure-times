package com.uber.departure.times.storage.client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uber.departure.times.storage.client.grid.GridCoordinates;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class StorageClient {
    private static final Logger logger = LoggerFactory.getLogger(StorageClient.class);

    @Autowired
    private StorageRemote remote;

    @NotNull
    public List<Stop> nearestStops(@NotNull Location location) {
        Objects.requireNonNull(location, "location");

        final List<Stop> result = new ArrayList<>();
        final GridCoordinates coordinates = GridCoordinates.from(location);
        result.addAll(nearestStops(coordinates, location));

        for (GridCoordinates.Neighbour n : GridCoordinates.Neighbour.values()) {
            final GridCoordinates key = n.apply(coordinates);
            if (key != null) {
                result.addAll(nearestStops(key, location));
            }
        }

        return result;
    }

    @NotNull
    private List<Stop> nearestStops(@NotNull GridCoordinates key, @NotNull Location location) {
        try {
            return remote.nearestStops(key, location);
        } catch (RemoteException e) {
            logger.error("failed to get nearest stops", e);
            return Collections.emptyList();
        }
    }


}
