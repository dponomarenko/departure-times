package com.uber.departure.times.storage.client;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

/**
 * @author Danila Ponomarenko
 */
public final class ProviderDependent {
    private final Provider provider;

    public ProviderDependent(@NotNull Provider provider) {
        this.provider = Objects.requireNonNull(provider, "provider");
    }
}
