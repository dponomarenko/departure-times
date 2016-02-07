package com.uber.departure.times.common.server.cluster;

/**
 * @author Danila Ponomarenko
 */
public final class PartitionNotSupportedException extends RuntimeException {
    public PartitionNotSupportedException(byte partition) {
        super((partition & 0xff) + "");
    }
}
