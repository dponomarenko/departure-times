package com.uber.departure.times.rest.server.http;

import java.io.PrintStream;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;

/**
 * @author Danila Ponomarenko
 */
public final class DeparturesContainer implements Container {
    public void handle(Request req, Response resp) {
        try {
            PrintStream body = resp.getPrintStream();
            long time = System.currentTimeMillis();

            resp.setValue("Content-Type", "text/plain");
            resp.setValue("Server", "HelloWorld/1.0 (Simple 4.0)");
            resp.setDate("Date", time);
            resp.setDate("Last-Modified", time);

            body.println("Hello World");
            body.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
