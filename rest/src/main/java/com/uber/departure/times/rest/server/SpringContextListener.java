package com.uber.departure.times.rest.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uber.departure.times.common.server.main.ContextStartStopListener;
import com.uber.departure.times.rest.server.http.HttpServerController;

/**
 * @author Danila Ponomarenko
 */
@Component(SpringContextListener.BEAN_NAME)
public final class SpringContextListener implements ContextStartStopListener {
    public static final String BEAN_NAME = "contextListener";

    @Autowired
    private HttpServerController controller;

    public void afterStarted() {
        controller.start();
    }

    public void beforeStopped() {
        controller.stop();
    }
}
