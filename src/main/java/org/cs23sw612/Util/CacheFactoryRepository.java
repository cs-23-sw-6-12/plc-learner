package org.cs23sw612.Util;

import java.nio.file.ProviderNotFoundException;
import java.util.HashMap;

public class CacheFactoryRepository {
    static HashMap<String, CacheSULFactory> factories;

    public CacheFactoryRepository() {
        factories = new HashMap<>();
    }

    public void addDefaultFactories() {
        factories.put("hash", new IntegerWordCacheSULFactory());
    }

    public static CacheSULFactory getCacheSULFactory() throws ProviderNotFoundException {
        var factory = factories.get("hash");

        if (factory == null)
            throw new ProviderNotFoundException("Cache of specified type was not present in the repository");

        return factory;
    }

}
