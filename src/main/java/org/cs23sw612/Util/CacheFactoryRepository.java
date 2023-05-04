package org.cs23sw612.Util;

import net.automatalib.commons.util.Pair;
import net.automatalib.words.Word;

import java.nio.file.ProviderNotFoundException;
import java.util.HashMap;

public class CacheFactoryRepository {
    HashMap<Pair<Object, Object>, CacheSULFactory> factories;

    public CacheFactoryRepository() {
        factories = new HashMap<>();
    }

    public void addDefaultFactories() {
        var hej = Word.fromSymbols(1);
        factories.put(Pair.of(hej.getClass(), hej.getClass()), new IntegerWordCacheSULFactory());
    }

    public <I, O> CacheSULFactory<I, O> getCacheSULFactory(Class<I> I, Class<O> O) throws ProviderNotFoundException {
        var factory = factories.get(Pair.of(I, O));

        if (factory == null)
            throw new ProviderNotFoundException("Cache of specified type was not present in the repository");

        return factory;
    }

}
