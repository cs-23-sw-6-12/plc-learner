package org.cs23sw612.Util;

import de.learnlib.api.SUL;
import net.automatalib.words.Word;
import org.cs23sw612.HashCacheStorage;
import org.cs23sw612.SUL.IntegerWordCacheSUL;

import java.io.File;

public class IntegerWordCacheSULFactory extends CacheSULFactory {

    public static SUL<Word<Integer>, Word<Integer>> create(String cachePath, SUL<Word<Integer>, Word<Integer>> sul) {
        return new IntegerWordCacheSUL(new HashCacheStorage(new File(cachePath)), sul);
    }

    public String getName() {
        return "cache";
    }
}
