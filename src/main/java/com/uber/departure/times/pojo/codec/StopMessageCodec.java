package com.uber.departure.times.pojo.codec;

import com.uber.departure.times.pojo.Stop;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * @author Danila Ponomarenko
 */
public final class StopMessageCodec implements MessageCodec<Stop, Stop> {
    @Override
    public void encodeToWire(Buffer buffer, Stop s) {
        s.toJson().writeToBuffer(buffer);
    }

    @Override
    public Stop decodeFromWire(int pos, Buffer buffer) {
        return new Stop(buffer.toJsonObject());
    }

    @Override
    public Stop transform(Stop s) {
        return s;
    }

    @Override
    public String name() {
        return "stop";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
