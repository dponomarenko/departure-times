package com.uber.departure.times.common.component;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class MessageCodecRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(MessageCodecRegistrar.class);
    @Autowired
    private EventBus bus;

    public void register(@NotNull Map<Class, MessageCodec> map) {
        for (Map.Entry<Class, MessageCodec> entry : map.entrySet()) {
            register(entry.getKey(), entry.getValue());
        }
    }

    public void register(@NotNull Class c, @NotNull MessageCodec mc) {
        try {
            //noinspection unchecked
            bus.registerDefaultCodec(c, mc);
        } catch (Exception e) {
            logger.info("Codec already registered for class:" + c.getSimpleName());
        }
    }
}
