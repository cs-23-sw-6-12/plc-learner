package org.cs23sw612.Util;

import org.cs23sw612.Interfaces.MealyLearnerFactory;
import org.cs23sw612.LearnerFactories.DHCLearnerFactory;
import org.cs23sw612.LearnerFactories.LStarLearnerFactory;
import org.cs23sw612.LearnerFactories.TTTLearnerFactory;

import java.nio.file.ProviderNotFoundException;
import java.util.HashMap;
import java.util.stream.Stream;

public class LearnerFactoryRepository<I, O> {
    HashMap<String, MealyLearnerFactory<I, O>> factories;

    public LearnerFactoryRepository() {
        factories = new HashMap<>();
    }

    public void addDefaultFactories() {
        addFactory(new DHCLearnerFactory<>());
        addFactory(new TTTLearnerFactory<>());
        addFactory(new LStarLearnerFactory<>());
    }

    public void addFactory(MealyLearnerFactory<I, O> factory) {
        factories.put(factory.getName().toLowerCase(), factory);
    }

    public MealyLearnerFactory<I, O> getLearnerFactory(String name) throws ProviderNotFoundException {
        var factory = factories.get(name.toLowerCase());

        if (factory == null)
            throw new ProviderNotFoundException("Learner " + name + " was not present in the repository");

        return factory;
    }

    public Stream<String> getLearnerNames() {
        return factories.values().stream().map(MealyLearnerFactory::getName);
    }
}
