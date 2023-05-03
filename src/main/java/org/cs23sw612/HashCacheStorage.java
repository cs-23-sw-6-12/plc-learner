package org.cs23sw612;

import org.cs23sw612.Interfaces.CacheStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HashCacheStorage implements CacheStorage {
    private Map<String, CacheStorageRecord> map;
    private BufferedWriter outputStream;
    private final Logger logger = LoggerFactory.getLogger(HashCacheStorage.class);
    private Integer idCounter;
    public HashCacheStorage(File permenantFile) {
        map = new HashMap<>();
        idCounter = 0;
        if (permenantFile.exists()) {
            try {
                var reader = new BufferedReader(new FileReader(permenantFile));
                loadSavedData(reader);
                reader.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            outputStream = new BufferedWriter(new FileWriter(permenantFile, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSavedData(BufferedReader fileReader) {
        try {
            var line = fileReader.readLine();
            while (line != null) {
                try {
                    var values = line.split(",");
                    var id = Integer.parseInt(values[0]);
                    var input = values[1];
                    var response = values[2];
                    Integer parentId;
                    if (values[3].equals("null")) {
                        parentId = null;
                    } else {
                        parentId = Integer.parseInt(values[3]);
                    }
                    var cacheRecord = new CacheStorageRecord(id, response);
                    map.put(createKey(parentId, input), cacheRecord);
                    idCounter = Math.max(id + 1, idCounter);
                } catch (Exception e) {
                    logger.warn("Invalid cache entry '{}' got {}", line, e.toString());
                }
                line = fileReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CacheStorageRecord LookupCacheEntry(Integer previousInputId, String input) {
        return map.get(createKey(previousInputId, input));
    }

    @Override
    public CacheStorageRecord InsertCacheEntry(Integer previousInputId, String input, String output) {
        var record = new CacheStorageRecord(idCounter++, output);
        map.put(createKey(previousInputId, input), record);
        try {
            outputStream.write(String.format("%d,%s,%s,%s\n", record.id(), input, record.response(),
                    previousInputId == null ? "null" : previousInputId.toString()));
            outputStream.flush();
        } catch (IOException e) {
            logger.warn("Could not append to cache file: {}", e.getMessage());
        }
        return record;
    }

    private String createKey(Integer parentId, String input) {
        return input + "-" + parentId;
    }
}
