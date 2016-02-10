package com.uber.departure.times.common;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import io.vertx.core.Future;

/**
 * @author Danila Ponomarenko
 */
public abstract class SpringVerticle extends StartupLoggingVerticle {
    private AbstractApplicationContext context;
    private String contextName;
    private SpringVerticleConnector connector;

    public SpringVerticle(@NotNull String contextName) {
        this.contextName = Objects.requireNonNull(contextName, "contextName");
    }

    @Override
    protected void startOverride(Future<Void> startFuture) throws Exception {
        connector = new SpringVerticleConnector();
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(createVertxContext(connector));
        context.setId(contextName);
        context.getEnvironment().setActiveProfiles("production");
        context.setConfigLocation(contextName);

        context.refresh();

        context.start();
        this.context = context;
        connector.startFuture().compose(e -> startFuture.complete(), startFuture);
    }

    @NotNull
    private AbstractApplicationContext createVertxContext(@NotNull SpringVerticleConnector connector) {
        final StaticApplicationContext context = new StaticApplicationContext();
        context.refresh();
        context.getBeanFactory().registerSingleton("vertx", vertx);
        context.getBeanFactory().registerSingleton("vertx-context", context);
        context.getBeanFactory().registerSingleton("event-bus", vertx.eventBus());
        context.getBeanFactory().registerSingleton("connector", connector);
        return context;
    }

    @Override
    protected void stopOverride(Future<Void> stopFuture) throws Exception {
        context.destroy();
        connector.stopFuture().compose(e -> stopFuture.complete(), stopFuture);
    }
}
