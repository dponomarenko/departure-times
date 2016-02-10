package com.uber.departure.times.mock;


import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import io.vertx.core.Future;

/**
 * @author Danila Ponomarenko
 */
public final class FutureHelper {
    public static final long DEFAULT_TIMEOUT = TimeUnit.SECONDS.toMillis(30);

    public static <T> T wait(@NotNull Future<T> f) {
        final long startTime = System.currentTimeMillis();
        try {
            while (!f.isComplete()) {
                if (System.currentTimeMillis() - startTime > DEFAULT_TIMEOUT) {
                    Assert.fail("BUSY-WAIT timeout " + DEFAULT_TIMEOUT + "ms.");
                }
                Thread.sleep(100);
            }
            if (f.failed()) {
                Assert.fail(f.cause().toString());
            }
            return f.result();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assert.fail(e.toString());
            //will never get here
            return null;
        }
    }

    private FutureHelper() {
    }
}
