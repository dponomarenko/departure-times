package com.uber.departure.times.rest.server;

import org.springframework.context.support.AbstractApplicationContext;

import com.uber.departure.times.common.server.main.CommonMain;


/**
 * @author Danila Ponomarenko
 */
public final class Main {
    private static final String CONTEXT_NAME = "application-context.xml";
    @SuppressWarnings("FieldCanBeLocal")
    private static AbstractApplicationContext context;

    public static void main(String[] args) {
        context = CommonMain.createContext(CONTEXT_NAME, SpringContextListener.BEAN_NAME);
    }
}
