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

    public static final String VERTX_BEAN = "vertx";
    public static final String VERTX_CONTEXT_BEAN = "vertx-context";
    public static final String EVENT_BUS_BEAN = "event-bus";
    public static final String CONNECTOR_BEAN = "connector";

    @NotNull
    private AbstractApplicationContext createVertxContext(@NotNull SpringVerticleConnector connector) {
        final StaticApplicationContext context = new StaticApplicationContext();
        context.refresh();
        context.getBeanFactory().registerSingleton(VERTX_BEAN, vertx);
        context.getBeanFactory().registerSingleton(VERTX_CONTEXT_BEAN, context);
        context.getBeanFactory().registerSingleton(EVENT_BUS_BEAN, vertx.eventBus());
        context.getBeanFactory().registerSingleton(CONNECTOR_BEAN, connector);
        return context;
    }

    @Override
    protected void stopOverride(Future<Void> stopFuture) throws Exception {
        context.destroy();
        connector.stopFuture().compose(e -> stopFuture.complete(), stopFuture);
    }
}
