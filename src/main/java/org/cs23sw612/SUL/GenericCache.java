package org.cs23sw612.SUL;

import de.learnlib.api.SUL;
import net.automatalib.words.Word;
import org.cs23sw612.Interfaces.CacheStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GenericCache implements SUL<Word<Integer>, Word<Integer>> {
    private final SUL<Word<Integer>, Word<Integer>> underlyingSul;
    private final CacheStorage cacheStorage;

    List<Word<Integer>> currentInput;

    boolean isUnderlyingSULSynced;

    Integer lastInputCacheRecordId;

    final Logger logger = LoggerFactory.getLogger(GenericCache.class);

    public GenericCache(CacheStorage cacheStorage, SUL<Word<Integer>, Word<Integer>> underlyingSul)
            throws SQLException {
        this.cacheStorage = cacheStorage;
        this.underlyingSul = underlyingSul;
        lastInputCacheRecordId = null;
        currentInput = new ArrayList<>();
        isUnderlyingSULSynced = false;
    }

    @Override
    public void pre() {
        isUnderlyingSULSynced = false;
    }

    @Override
    public void post() {
        if (!isUnderlyingSULSynced) {
            logger.info("Cached");
        }
        lastInputCacheRecordId = null;
        currentInput.clear();
    }

    @Override
    public Word<Integer> step(Word<Integer> in) {
        currentInput.add(in);
        var input = in.stream().map(Object::toString).collect(Collectors.joining(""));

        var cachedResponse = cacheStorage.LookupCacheEntry(lastInputCacheRecordId, input);

        Word<Integer> response;

        if (cachedResponse == null) {
            if (!isUnderlyingSULSynced) {
                underlyingSul.post();
                underlyingSul.pre();
                for (var i = 0; i + 1 < currentInput.size(); i++) {
                    underlyingSul.step(currentInput.get(i));
                }
                isUnderlyingSULSynced = true;
            }
            response = underlyingSul.step(in);
            var responseString = response.stream().map(c -> c == 0 ? "0" : "1").collect(Collectors.joining());
            cachedResponse = cacheStorage.InsertCacheEntry(lastInputCacheRecordId, input, responseString);
        }

        var array = cachedResponse.response().chars().boxed().map(c -> (Integer) (c == '0' ? 0 : 1))
                .collect(Collectors.toList());
        response = Word.fromList(array);
        lastInputCacheRecordId = cachedResponse.id();

        return response;
    }
}
