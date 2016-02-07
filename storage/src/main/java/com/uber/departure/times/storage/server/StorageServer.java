package com.uber.departure.times.storage.server;

import java.rmi.RemoteException;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import com.uber.departure.times.storage.client.Location;
import com.uber.departure.times.storage.client.Stop;
import com.uber.departure.times.storage.client.StorageRemote;
import com.uber.departure.times.storage.client.grid.GridCoordinates;
import com.uber.departure.times.storage.server.storage.stops.StopsByLocationService;

/**
 * @author Danila Ponomarenko
 */
public final class StorageServer implements StorageRemote {

    @Autowired
    private StopsByLocationService stopsByLocation;

    @NotNull
    @Override
    public List<DepartureTime> getDepartureTimes(@NotNull List<StopId> stops) throws RemoteException {
        //TODO IMPLEMENT ME
    }

    @NotNull
    @Override
    public List<Stop> nearestStops(@NotNull GridCoordinates key, @NotNull Location location) throws RemoteException {
        return stopsByLocation.get(key, location);
    }
}
