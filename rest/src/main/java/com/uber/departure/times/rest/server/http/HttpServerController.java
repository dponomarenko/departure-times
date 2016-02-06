package com.uber.departure.times.rest.server.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.jetbrains.annotations.NotNull;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.connect.SocketConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uber.departure.times.common.server.UnrecoverableException;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class HttpServerController {

    @Autowired
    private HttpServerConfiguration conf;

    private final SocketConnection connection;
    private final SocketAddress address;

    @Autowired
    public HttpServerController(@NotNull HttpServerConfiguration conf) {
        try {
            final DeparturesContainer container = new DeparturesContainer();
            final ContainerSocketProcessor processor = new ContainerSocketProcessor(container);
            connection = new SocketConnection(processor);
            address = new InetSocketAddress(conf.getPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        try {
            connection.connect(address);
        } catch (IOException e) {
            throw new UnrecoverableException(e);
        }
    }

    public void stop() {
        try {
            connection.close();
        } catch (IOException e) {
            throw new UnrecoverableException(e);
        }
    }
}
