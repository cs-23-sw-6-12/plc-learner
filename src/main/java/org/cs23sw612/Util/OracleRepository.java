package org.cs23sw612.Util;

import org.cs23sw612.Interfaces.OracleFactory;
import org.cs23sw612.OracleFactories.CompleteExplorationOracleFactory;
import org.cs23sw612.OracleFactories.RandomWalkOracleFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class OracleRepository {
    private static final Logger LOGGER = Logger.getGlobal();
    private final Map<String, OracleFactory> oracleFactories;
    public OracleRepository() {
        oracleFactories = new HashMap<>();
    }

    public void addDefaultFactories() {
        addFactory(new RandomWalkOracleFactory());
        addFactory(new CompleteExplorationOracleFactory());
    }

    public void addFactory(OracleFactory factory) {
        oracleFactories.put(factory.getName().toLowerCase(), factory);
    }

    public OracleFactory getOracleFactory(String name) {
        return oracleFactories.get(name.toLowerCase());
    }

    public Stream<String> getLearnerNames() {
        return oracleFactories.values().stream().map(OracleFactory::getName);
    }
}
