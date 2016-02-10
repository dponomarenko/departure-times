package com.uber.departure.times.mock;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.common.pojo.Location;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.common.pojo.StopId;

/**
 * @author Danila Ponomarenko
 */
public final class EntityHelper {
    private static final AtomicLong counter = new AtomicLong();

    public static long nextId() {
        return counter.incrementAndGet();
    }

    @NotNull
    public static Location randomLocation() {
        final ThreadLocalRandom rnd = ThreadLocalRandom.current();
        return new Location(
                rnd.nextDouble(-90, 90),
                rnd.nextDouble(-180, 180)
        );
    }

    @NotNull
    public static StopId randomStopId() {
        return new StopId("ag-" + nextId(), "rt-" + nextId(), "st-" + nextId());
    }

    @NotNull
    public static Stop randomStop() {
        return new Stop(randomStopId(), randomLocation());
    }

    private EntityHelper() {
    }
}
