package com.uber.departure.times.storage.client.pojo;

import java.util.Objects;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;

/**
 * @author Danila Ponomarenko
 */
public final class Route extends Entity {
    public final String title;

    public Route(@NotNull Tag tag, @NotNull String title) {
        super(tag);
        this.title = Objects.requireNonNull(title, "title");
    }

    @NotNull
    public String getTitle() {
        return title;
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
        Agency other = (Agency) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(title, other.title)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(37, 31)
                .appendSuper(super.hashCode())
                .append(title)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("title", title)
                .toString();
    }
}
