package com.uber.departure.times.common.server.main;

/**
 * @author Danila Ponomarenko
 */
public interface ContextStartStopListener {
    void afterStarted();

    void beforeStopped();
}
