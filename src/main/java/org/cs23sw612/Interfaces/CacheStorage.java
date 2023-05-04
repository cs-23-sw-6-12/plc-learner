package org.cs23sw612.Interfaces;

import org.cs23sw612.CacheStorageRecord;

public interface CacheStorage {
    CacheStorageRecord LookupCacheEntry(Integer previousInputId, String input);
    CacheStorageRecord InsertCacheEntry(Integer previousInputId, String input, String output);
}
