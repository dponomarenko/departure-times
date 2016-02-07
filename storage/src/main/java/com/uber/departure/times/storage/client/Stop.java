package com.uber.departure.times.storage.client;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.storage.client.pojo.Entity;

/**
 * @author Danila Ponomarenko
 */
public final class Stop extends Entity {
    private final Location location;

    public Stop(@NotNull Id id, @NotNull String title, @NotNull Location location) {
        super(id, title);
        this.location = location;
    }

    @NotNull
    public Location getLocation() {
        return location;
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
        Stop other = (Stop) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(location, other.location)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(37, 31)
                .appendSuper(super.hashCode())
                .append(location)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("location", location)
                .toString();
    }
}
