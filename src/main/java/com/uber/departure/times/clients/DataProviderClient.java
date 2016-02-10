package com.uber.departure.times.clients;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.common.pojo.Route;
import com.uber.departure.times.common.pojo.ProvidedPredictions;
import com.uber.departure.times.common.pojo.StopId;
import com.uber.departure.times.common.pojo.Stops;

import io.vertx.core.Future;


/**
 * @author Danila Ponomarenko
 */
public interface DataProviderClient {
    @NotNull
    Future<Collection<String>> getAgencyTags();

    @NotNull
    Future<Collection<String>> getRouteTags(@NotNull String agencyTag);

    @NotNull
    Future<Stops> getStops(@NotNull Route route);

    @NotNull
    Future<ProvidedPredictions> predict(@NotNull StopId stopId);
}
