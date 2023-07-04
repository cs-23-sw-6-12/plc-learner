package org.cs23sw612.SUL;

import de.learnlib.api.SUL;
import net.automatalib.words.Word;
import org.cs23sw612.Interfaces.CacheStorage;
import org.cs23sw612.Util.Bit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BitWordCacheSUL implements SUL<Word<Bit>, Word<Bit>> {
    private final SUL<Word<Bit>, Word<Bit>> underlyingSul;
    private final CacheStorage cacheStorage;

    List<Word<Bit>> currentInput;

    boolean isUnderlyingSULSynced;

    Integer lastInputCacheRecordId;

    final Logger logger = LoggerFactory.getLogger(BooleanWordCacheSUL.class);

    public BitWordCacheSUL(CacheStorage cacheStorage, SUL<Word<Bit>, Word<Bit>> underlyingSul) {
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
    public Word<Bit> step(Word<Bit> in) {
        currentInput.add(in);
        var input = in.stream().map(Object::toString).collect(Collectors.joining(""));

        var cachedResponse = cacheStorage.LookupCacheEntry(lastInputCacheRecordId, input);

        Word<Bit> response;

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
            var responseString = response.stream().map(Object::toString).collect(Collectors.joining());
            cachedResponse = cacheStorage.InsertCacheEntry(lastInputCacheRecordId, input, responseString);
        }

        var array = cachedResponse.response().chars().boxed().map(c -> c != '0').toList();
        response = Word.fromList(array.stream().map(Bit::fromBool).toList());
        lastInputCacheRecordId = cachedResponse.id();

        return response;
    }
}