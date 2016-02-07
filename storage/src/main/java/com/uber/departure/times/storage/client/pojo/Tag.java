package com.uber.departure.times.storage.client.pojo;

import java.util.Objects;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * @author Danila Ponomarenko
 */
public final class Tag {
    private final String tag;

    public Tag(@NotNull String tag) {
        this.tag = Objects.requireNonNull(tag, "tag");
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
        Tag other = (Tag) obj;
        return new EqualsBuilder()
                .append(tag, other.tag)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(37, 31)
                .append(tag)
                .toHashCode();
    }

    @Override
    public String toString() {
        return tag;
    }
}
