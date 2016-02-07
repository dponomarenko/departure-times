package com.uber.departure.times.storage.client.pojo;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.storage.client.Id;

/**
 * @author Danila Ponomarenko
 */
public abstract class Entity implements Serializable {
    public final Id id;
    public final String title;

    public Entity(@NotNull Id id, @NotNull String title) {
        this.id = Objects.requireNonNull(id, "id");
        this.title = Objects.requireNonNull(title, "title");
    }

    @NotNull
    public Id getId() {
        return id;
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
        Entity other = (Entity) obj;
        return new EqualsBuilder()
                .append(id, other.id)
                .append(title, other.title)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(37, 31)
                .append(id)
                .append(title)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("title", title)
                .toString();
    }
}
