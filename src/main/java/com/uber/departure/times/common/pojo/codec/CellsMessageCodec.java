package com.uber.departure.times.common.pojo.codec;

import com.uber.departure.times.common.JsonConvertible;
import com.uber.departure.times.common.pojo.Cell;
import com.uber.departure.times.common.pojo.Cells;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class CellsMessageCodec implements MessageCodec<Cells, Cells> {
    @Override
    public void encodeToWire(Buffer buffer, Cells s) {
        s.toJsonArray().writeToBuffer(buffer);
    }

    @Override
    public Cells decodeFromWire(int pos, Buffer buffer) {
        return new Cells(JsonConvertible.fromJsonArray(buffer.toJsonArray(), o -> new Cell((JsonObject) o)));
    }

    @Override
    public Cells transform(Cells s) {
        return s;
    }

    @Override
    public String name() {
        return "cells";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}