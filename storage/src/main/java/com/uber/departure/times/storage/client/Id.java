package com.uber.departure.times.storage.client;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * @author Danila Ponomarenko
 */
public final class Id implements Serializable {
    public final byte[] bytes;

    public Id(byte[] bytes) {
        this.bytes = Objects.requireNonNull(bytes, "bytes");
        if (bytes.length == 0) {
            throw new IllegalArgumentException("bytes are empty");
        }
    }

    @NotNull
    public String toHexString() {
        return new String(Hex.encodeHex(bytes, true));
    }

    @NotNull
    public static Id formHexString(@NotNull String hex) throws DecoderException {
        Objects.requireNonNull(hex, "hex");
        return new Id(Hex.decodeHex(hex.toCharArray()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Id other = (Id) obj;
        return new EqualsBuilder()
                .append(bytes, other.bytes)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(37, 31)
                .append(bytes)
                .toHashCode();
    }

    @Override
    public String toString() {
        return toHexString();
    }
}
