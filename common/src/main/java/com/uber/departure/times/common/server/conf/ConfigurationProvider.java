package com.uber.departure.times.common.server.conf;

import java.util.Arrays;
import java.util.stream.Stream;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.uber.departure.times.common.server.UnrecoverableException;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class ConfigurationProvider {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationProvider.class);

    public static final String BEAN_NAME = "configuration";
    private static final String MAIN_CONFIG_NAME = "configuration.properties";
    private static final String PREFIX = "conf.";


    private final FileChangedReloadingStrategy strategy = new FileChangedReloadingStrategy();

    @NotNull
    @Bean(name = ConfigurationProvider.BEAN_NAME)
    public Configuration getConfiguration() throws ConfigurationException {
        final Configuration main = loadMainConfiguration();
        final CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(main);
        load(Arrays.stream(main.getStringArray(PREFIX + "configs"))).forEach(config::addConfiguration);
        return config;
    }

    @NotNull
    private Configuration loadMainConfiguration() {
        try {
            final PropertiesConfiguration config = new PropertiesConfiguration(MAIN_CONFIG_NAME);
            strategy.setRefreshDelay(config.getLong(PREFIX + "refresh.delay", 60000));
            config.setReloadingStrategy(strategy);
            return config;
        } catch (ConfigurationException e) {
            throw new UnrecoverableException(MAIN_CONFIG_NAME + "not found in classpath", e);
        }
    }

    @NotNull
    private Stream<Configuration> load(@NotNull Stream<String> paths) {
        return paths.map(p -> {
            try {
                return (Configuration) new PropertiesConfiguration(p);
            } catch (ConfigurationException e) {
                logger.error("Failed to load configuration from " + p, e);
                return null;
            }
        }).filter(c -> c != null);
    }

}
