package com.uber.departure.times.pojo.codec;

import com.uber.departure.times.common.JsonConvertible;
import com.uber.departure.times.pojo.StopPredictions;
import com.uber.departure.times.pojo.StopsPredictions;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class StopsPredictionsMessageCodec implements MessageCodec<StopsPredictions, StopsPredictions> {
    @Override
    public void encodeToWire(Buffer buffer, StopsPredictions s) {
        s.toJsonArray().writeToBuffer(buffer);
    }

    @Override
    public StopsPredictions decodeFromWire(int pos, Buffer buffer) {
        return new StopsPredictions(JsonConvertible.fromJsonArray(buffer.toJsonArray(), o -> new StopPredictions((JsonObject) o)));
    }

    @Override
    public StopsPredictions transform(StopsPredictions s) {
        return s;
    }

    @Override
    public String name() {
        return "stops-predictions";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}