package org.cs23sw612.Util;

import org.cs23sw612.Interfaces.MealyLearnerFactory;
import org.cs23sw612.LearnerFactories.DHCLearnerFactory;
import org.cs23sw612.LearnerFactories.LStarLearnerFactory;
import org.cs23sw612.LearnerFactories.TTTLearnerFactory;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class LearnerFactoryRepository<I,O> {
    HashMap<String, MealyLearnerFactory<I,O>> factories;

    public LearnerFactoryRepository() {
        factories = new HashMap<>();
    }

    public void addDefaultFactories() {
        addFactory(new DHCLearnerFactory<>());
        addFactory(new TTTLearnerFactory<>());
        addFactory(new LStarLearnerFactory<>());
    }

    public void addFactory(MealyLearnerFactory<I,O> factory) {
        factories.put(factory.getName().toLowerCase(), factory);
    }

    public MealyLearnerFactory<I,O> getLearnerFactory(String name) {
        return factories.get(name.toLowerCase());
    }

    public Stream<String> getLearnerNames() {
        return factories.values().stream().map(MealyLearnerFactory::getName);
    }
}
