package com.uber.departure.times.common.pojo.codec;

import com.uber.departure.times.common.pojo.StopPredictions;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * @author Danila Ponomarenko
 */
public final class StopPredictionsMessageCodec implements MessageCodec<StopPredictions, StopPredictions> {
    @Override
    public void encodeToWire(Buffer buffer, StopPredictions s) {
        s.toJson().writeToBuffer(buffer);
    }

    @Override
    public StopPredictions decodeFromWire(int pos, Buffer buffer) {
        return new StopPredictions(buffer.toJsonObject());
    }

    @Override
    public StopPredictions transform(StopPredictions s) {
        return s;
    }

    @Override
    public String name() {
        return "stop-predictions";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}