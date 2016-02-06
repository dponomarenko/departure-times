package com.uber.departure.times.common.server.main;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Danila Ponomarenko
 */
public final class CommonMain {
    private static final Logger logger = LoggerFactory.getLogger(CommonMain.class);

    public static AbstractApplicationContext createContext(@NotNull String contextName) {
        Objects.requireNonNull(contextName, "contextName");
        return createContext(contextName, null);
    }

    public static AbstractApplicationContext createContext(@NotNull String contextName, @Nullable String listenerBeanName) {
        Objects.requireNonNull(contextName, "contextName");

        final long startMillis = System.currentTimeMillis();
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setId(contextName);
        context.getEnvironment().setActiveProfiles("production");
        context.setConfigLocation(contextName);

        context.refresh();

        context.start();

        final ContextStartStopListener listener = (ContextStartStopListener) context.getBean(listenerBeanName);

        if (listener != null) {
            listener.afterStarted();
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                final long startMillis = System.currentTimeMillis();
                logger.info("SHUTDOWN signal caught");
                if (listener != null) {
                    listener.beforeStopped();
                }
                context.destroy();
                logger.info("SHUTDOWN finished in " + (System.currentTimeMillis() - startMillis) + "ms.");
            }
        });

        logger.info("STARTED in " + (System.currentTimeMillis() - startMillis) + "ms.");

        return context;
    }
}
