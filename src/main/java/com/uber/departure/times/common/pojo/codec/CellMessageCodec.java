package com.uber.departure.times.common.pojo.codec;

import com.uber.departure.times.common.pojo.Cell;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * @author Danila Ponomarenko
 */
public final class CellMessageCodec implements MessageCodec<Cell, Cell> {
    @Override
    public void encodeToWire(Buffer buffer, Cell s) {
        s.toJson().writeToBuffer(buffer);
    }

    @Override
    public Cell decodeFromWire(int pos, Buffer buffer) {
        return new Cell(buffer.toJsonObject());
    }

    @Override
    public Cell transform(Cell s) {
        return s;
    }

    @Override
    public String name() {
        return "cell";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
