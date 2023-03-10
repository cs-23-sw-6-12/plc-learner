package org.cs_23_sw_6_12;

import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.Alphabets;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class ArraySymbolAlphabet <I extends Object> {
    @SafeVarargs
    public final Alphabet<I[]> fromValues(int symbolSize, Class<I> c, I... values){
        ArrayList<I[]> alphabet = new ArrayList();


        for (int i = 0; i < symbolSize; i++) {
            try {

                I symbol = c.getConstructor().newInstance();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            for (int j = 0; j < symbolSize; j++) {
                for (I value:
                     values) {

                }

            }
        }
        return Alphabets.fromCollection(alphabet);
    }
}
