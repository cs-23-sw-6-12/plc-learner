package org.cs23sw612.Util;

import de.learnlib.api.SUL;
import net.automatalib.words.Word;
import org.cs23sw612.HashCacheStorage;
import org.cs23sw612.SUL.BooleanWordCacheSUL;

import java.io.File;

public class IntegerWordCacheSULFactory extends CacheSULFactory {

    public static SUL<Word<Boolean>, Word<Boolean>> create(String cachePath, SUL<Word<Boolean>, Word<Boolean>> sul) {
        return new BooleanWordCacheSUL(new HashCacheStorage(new File(cachePath)), sul);
    }

    public String getName() {
        return "cache";
    }
}
