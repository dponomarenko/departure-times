package com.uber.departure.times.common;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.vertx.core.Future;

/**
 * @author Danila Ponomarenko
 */
public abstract class SpringVerticle extends StartupLoggingVerticle {
    private AbstractApplicationContext spring;
    private String contextName;
    private SpringVerticleConnector connector;

    public SpringVerticle(@NotNull String contextName) {
        this.contextName = Objects.requireNonNull(contextName, "contextName");
    }

    @Override
    protected void startOverride(Future<Void> startFuture) throws Exception {
        connector = new SpringVerticleConnector();
        final ClassPathXmlApplicationContext spring = new ClassPathXmlApplicationContext(createVertxContext(connector));
        spring.setId(contextName);
        spring.getEnvironment().setActiveProfiles("production");
        spring.setConfigLocation(contextName);

        spring.refresh();

        spring.start();
        this.spring = spring;
        connector.startFuture().compose(e -> startFuture.complete(), startFuture);
    }

    public static final String VERTX_BEAN = "vertx";
    public static final String VERTX_CONTEXT_BEAN = "vertx-context";
    public static final String EVENT_BUS_BEAN = "event-bus";
    public static final String CONNECTOR_BEAN = "connector";

    @NotNull
    private AbstractApplicationContext createVertxContext(@NotNull SpringVerticleConnector connector) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getBeanFactory().registerSingleton(VERTX_BEAN, vertx);
        ctx.getBeanFactory().registerSingleton(VERTX_CONTEXT_BEAN, context);
        ctx.getBeanFactory().registerSingleton(EVENT_BUS_BEAN, vertx.eventBus());
        ctx.getBeanFactory().registerSingleton(CONNECTOR_BEAN, connector);
        ctx.refresh();

        return ctx;
    }

    @Override
    protected void stopOverride(Future<Void> stopFuture) throws Exception {
        spring.destroy();
        connector.stopFuture().compose(e -> stopFuture.complete(), stopFuture);
    }
}
