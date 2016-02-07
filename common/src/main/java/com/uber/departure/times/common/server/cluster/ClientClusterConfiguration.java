package com.uber.departure.times.common.server.cluster;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.common.server.UnrecoverableException;

/**
 * @author Danila Ponomarenko
 */
public class ClientClusterConfiguration {
    private static final String PREFIX = "cluster.";

    private static final int PARTITIONS_COUNT = 256;
    private static final int REPLICATION_FACTOR = 3;

    protected final Configuration conf;
    protected final String name;
    protected final InetAddress[][] partitions;
    protected final Set<InetAddress> nodes;

    public ClientClusterConfiguration(@NotNull Configuration conf, @NotNull String name) {
        this.conf = conf;
        this.name = name;
        this.nodes = Collections.unmodifiableSet(readNodes(conf));
        this.partitions = definePartitions(nodes);
    }

    public int getPort() {
        return conf.getInt(PREFIX + name + ".port");
    }

    @NotNull
    protected String prefix() {
        return PREFIX + name + ".";
    }

    @NotNull
    private Set<InetAddress> readNodes(@NotNull Configuration conf) {
        final Set<InetAddress> result = new HashSet<>();
        for (String nodesString : conf.getStringArray(prefix() + "nodes")) {
            result.add(toAddress(nodesString));
        }
        return result;
    }

    @NotNull
    private InetAddress[][] definePartitions(@NotNull Set<InetAddress> nodes) {
        final InetAddress[][] result = new InetAddress[PARTITIONS_COUNT][REPLICATION_FACTOR];
        final int partitionSize = PARTITIONS_COUNT / nodes.size();

        int counter = 0;
        for (InetAddress node : nodes) {
            for (int i = 0; i < REPLICATION_FACTOR; i++) {
                final int start = (counter + i) * partitionSize;
                for (int j = start; j < start + partitionSize; j++) {
                    result[j % PARTITIONS_COUNT][i] = node;
                }
            }
            counter++;
        }

        return result;
    }

    @NotNull
    private InetAddress toAddress(@NotNull String node) {
        try {
            return InetAddress.getByName(node);
        } catch (UnknownHostException e) {
            throw new UnrecoverableException(e);
        }
    }

    @NotNull
    public Iterable<InetAddress> getNodes() {
        return nodes;
    }

    @NotNull
    public InetAddress[] getNodes(byte partition) {
        return partitions[partition & 0xff];
    }
}
