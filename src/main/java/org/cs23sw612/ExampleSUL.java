
package org.cs23sw612;

import de.learnlib.driver.util.MealySimulatorSUL;
import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.util.automata.builders.AutomatonBuilders;
import net.automatalib.words.Word;
import net.automatalib.words.impl.Alphabets;
import net.automatalib.words.Alphabet;

public class ExampleSUL {

    public static final Alphabet<Word<Boolean>> alphabet = Alphabets.fromArray(
            Word.fromSymbols(true, false),
            Word.fromSymbols(true, true),
            Word.fromSymbols(false,true),
            Word.fromSymbols(false, false)
    );

    public static CompactMealy<Word<Boolean>, Object> createExample(){
        return AutomatonBuilders.newMealy(alphabet).withInitial("s0")
                .from("s0").on(Word.fromSymbols(false, false)).withOutput(Word.fromSymbols(true,false)).to("s0")
                .from("s0").on(Word.fromSymbols(false, true)).withOutput(Word.fromSymbols(true,false)).to("s0")
                .from("s0").on(Word.fromSymbols(true, true)).withOutput(Word.fromSymbols(false, false)).to("s0")
                .from("s0").on(Word.fromSymbols(true, false)).withOutput(Word.fromSymbols(false, true)).to("s1")

                .from("s1").on(Word.fromSymbols(false,true)).withOutput(Word.fromSymbols(true, false)).to("s0")
                .from("s1").on(Word.fromSymbols(true, true)).withOutput(Word.fromSymbols(false, false)).to("s0")
                .from("s1").on(Word.fromSymbols(false, false)).withOutput(Word.fromSymbols(true, true)).to("s1")
                .from("s1").on(Word.fromSymbols(true, false)).withOutput(Word.fromSymbols(false,true)).to("s1")
                .create();
    }

    public static MealySimulatorSUL<Word<Boolean>, Object> createExampleSUL(){
        return new MealySimulatorSUL<>(createExample());
    }
}
