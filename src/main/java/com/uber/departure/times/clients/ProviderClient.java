package com.uber.departure.times.clients;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.pojo.Route;
import com.uber.departure.times.pojo.StopPredictions;
import com.uber.departure.times.pojo.Stops;

import io.vertx.core.Future;


/**
 * @author Danila Ponomarenko
 */
public interface ProviderClient {
    @NotNull
    Future<Collection<String>> getAgencyTags();

    @NotNull
    Future<Collection<String>> getRouteTags(@NotNull String agencyTag);

    @NotNull
    Future<Stops> getStops(@NotNull Route route);

    @NotNull
    Future<StopPredictions> predict(@NotNull String agencyTag, @NotNull String routeTag, @NotNull String stopTag);
}
