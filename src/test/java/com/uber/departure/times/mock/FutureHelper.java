package com.uber.departure.times.mock;


import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import io.vertx.core.Future;

/**
 * @author Danila Ponomarenko
 */
public final class FutureHelper {
    public static <T> T wait(@NotNull Future<T> f) {
        try {
            while (!f.isComplete()) {
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
