package com.uber.departure.times.clients.nextbus.client.parser;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.pojo.StopPredictions;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class PredictionsParser {
    private static final int[] EMPTY = new int[0];

    public static final PredictionsParser PARSER = new PredictionsParser();

    public StopPredictions parse(@NotNull String agencyTag, @NotNull String routeTag, @NotNull String stopTag, @NotNull Buffer buffer) {
        final JsonObject json = buffer.toJsonObject();
        final JsonObject predictions = json.getJsonObject("predictions");
        final String agencyTitle = predictions.getString("agencyTitle");
        final String routeTitle = predictions.getString("routeTitle");
        final String stopTitle = predictions.getString("stopTitle");

        final String dirTitleBecauseNoPredictions = predictions.getString("dirTitleBecauseNoPredictions");
        if (dirTitleBecauseNoPredictions != null) {
            return new StopPredictions(agencyTitle, routeTitle, stopTitle, dirTitleBecauseNoPredictions, EMPTY);
        }

        final JsonObject direction = predictions.getJsonObject("direction");
        final String directionTitle = direction.getString("title");

        final JsonArray prediction = predictions.getJsonArray("prediction");
        final int[] times = new int[prediction.size()];
        for (int i = 0; i < times.length; i++) {
            final JsonObject jObject = prediction.getJsonObject(i);
            times[i] = jObject.getInteger("minutes");
        }
        return new StopPredictions(agencyTitle, routeTitle, stopTitle, directionTitle, times);
    }


}
