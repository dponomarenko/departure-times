package com.uber.departure.times.common.pojo.codec;

import com.uber.departure.times.common.JsonConvertible;
import com.uber.departure.times.common.pojo.Prediction;
import com.uber.departure.times.common.pojo.Predictions;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class PredictionsMessageCodec implements MessageCodec<Predictions, Predictions> {
    @Override
    public void encodeToWire(Buffer buffer, Predictions s) {
        s.toJsonArray().writeToBuffer(buffer);
    }

    @Override
    public Predictions decodeFromWire(int pos, Buffer buffer) {
        return new Predictions(JsonConvertible.fromJsonArray(buffer.toJsonArray(), o -> new Prediction((JsonObject) o)));
    }

    @Override
    public Predictions transform(Predictions s) {
        return s;
    }

    @Override
    public String name() {
        return "predictions";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}