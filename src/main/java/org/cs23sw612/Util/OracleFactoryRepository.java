package org.cs23sw612.Util;

import org.cs23sw612.Interfaces.OracleFactory;
import org.cs23sw612.OracleFactories.RandomWalkOracleFactory;

import java.nio.file.ProviderNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class OracleFactoryRepository<I, O> {
    private static final Logger LOGGER = Logger.getGlobal();
    private final Map<String, OracleFactory<I, O>> oracleFactories;
    public OracleFactoryRepository() {
        oracleFactories = new HashMap<>();
    }

    public void addDefaultFactories() {
        addFactory(new RandomWalkOracleFactory());
    }

    public void addFactory(OracleFactory<I, O> factory) {
        oracleFactories.put(factory.getName().toLowerCase(), factory);
    }

    public OracleFactory<I, O> getOracleFactory(String name) throws ProviderNotFoundException {
        var factory = oracleFactories.get(name.toLowerCase());

        if (factory == null)
            throw new ProviderNotFoundException("Oracle " + name + " was not present in the repository");

        return factory;
    }

    public Stream<String> getLearnerNames() {
        return oracleFactories.values().stream().map(OracleFactory::getName);
    }
}
