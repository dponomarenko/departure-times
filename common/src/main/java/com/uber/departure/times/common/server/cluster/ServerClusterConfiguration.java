package com.uber.departure.times.common.server.cluster;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.configuration.Configuration;
import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.common.server.UnrecoverableException;

/**
 * @author Danila Ponomarenko
 */
public final class ServerClusterConfiguration extends ClientClusterConfiguration {
    private final InetAddress thisAddress;

    public ServerClusterConfiguration(@NotNull Configuration configuration, @NotNull String name) {
        super(configuration, name);
        thisAddress = thisAddress();
    }

    @NotNull
    private InetAddress thisAddress() {
        for (InetAddress a : nodeAddresses()) {
            if (nodes.contains(a)) {
                return a;
            }
        }
        throw new UnrecoverableException("No cluster configuration was found for current node");
    }

    @NotNull
    private List<InetAddress> nodeAddresses() {
        try {
            return Collections.list(NetworkInterface.getNetworkInterfaces())
                    .stream()
                    .flatMap(i -> Collections.list(i.getInetAddresses()).stream())
                    .collect(Collectors.toList());
        } catch (SocketException e) {
            throw new UnrecoverableException(e);
        }
    }

    public boolean isSupported(byte partition) {
        final InetAddress[] nodes = getNodes(partition);
        for (InetAddress node : nodes) {
            if (Objects.equals(node, thisAddress)) {
                return true;
            }
        }
        return false;
    }

    public void throwIfNotSupported(byte partition) {
        if (!isSupported(partition)) {
            throw new PartitionNotSupportedException(partition);
        }
    }
}
