package com.uber.departure.times.storage.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.javadocmd.simplelatlng.LatLng;
import com.uber.departure.times.storage.client.grid.GridCoordinates;

/**
 * @author Danila Ponomarenko
 */
public interface StorageRemote extends Remote {

    @NotNull
    List<DepartureTime> getDepartureTimes(@NotNull List<StopId> stops) throws RemoteException;

    @NotNull
    List<Stop> nearestStops(@NotNull GridCoordinates key, @NotNull Location location) throws RemoteException;
}