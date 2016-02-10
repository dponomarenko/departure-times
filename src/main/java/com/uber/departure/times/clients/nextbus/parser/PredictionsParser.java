package com.uber.departure.times.clients.nextbus.parser;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import com.uber.departure.times.common.pojo.ProvidedPredictions;
import com.uber.departure.times.common.pojo.StopId;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class PredictionsParser {
    private static final long[] EMPTY = new long[0];

    public ProvidedPredictions parse(@NotNull StopId stopId, @NotNull Buffer buffer) {
        final JsonObject json = buffer.toJsonObject();
        final JsonObject predictions = json.getJsonObject("predictions");
        final String agencyTitle = predictions.getString("agencyTitle");
        final String routeTitle = predictions.getString("routeTitle");
        final String stopTitle = predictions.getString("stopTitle");

        final String dirTitleBecauseNoPredictions = predictions.getString("dirTitleBecauseNoPredictions");
        if (dirTitleBecauseNoPredictions != null) {
            return new ProvidedPredictions(agencyTitle, routeTitle, stopTitle, dirTitleBecauseNoPredictions, EMPTY);
        }

        final JsonObject direction = predictions.getJsonObject("direction");
        final String directionTitle = direction.getString("title");

        final JsonArray prediction = direction.getJsonArray("prediction");
        final long[] times = new long[prediction.size()];
        for (int i = 0; i < times.length; i++) {
            final JsonObject jObject = prediction.getJsonObject(i);
            times[i] = Long.valueOf(jObject.getString("epochTime"));
        }
        return new ProvidedPredictions(agencyTitle, routeTitle, stopTitle, directionTitle, times);
    }


}
