package org.cs23sw612;

import de.learnlib.driver.util.MealySimulatorSUL;
import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.util.automata.builders.AutomatonBuilders;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.cs23sw612.Util.AlphabetUtil;

public class ExampleSUL {

    public static final Alphabet<Word<Boolean>> alphabet = AlphabetUtil.createAlphabet(2);

    public static CompactMealy<Word<Boolean>, Object> createExample() {
        return AutomatonBuilders.newMealy(alphabet).withInitial("s0").from("s0").on(Word.fromSymbols(false, false))
                .withOutput(Word.fromSymbols(true, false)).to("s0").from("s0").on(Word.fromSymbols(false, true))
                .withOutput(Word.fromSymbols(true, false)).to("s0").from("s0").on(Word.fromSymbols(true, true))
                .withOutput(Word.fromSymbols(false, false)).to("s0").from("s0").on(Word.fromSymbols(true, false))

                .withOutput(Word.fromSymbols(false, true)).to("s1").from("s1").on(Word.fromSymbols(false, true))
                .withOutput(Word.fromSymbols(true, false)).to("s0").from("s1").on(Word.fromSymbols(true, true))
                .withOutput(Word.fromSymbols(false, false)).to("s0").from("s1").on(Word.fromSymbols(false, false))
                .withOutput(Word.fromSymbols(true, true)).to("s1").from("s1").on(Word.fromSymbols(true, false))
                .withOutput(Word.fromSymbols(false, true)).to("s2") // TODO: to s1

                .from("s2").on(Word.fromSymbols(false, false)).withOutput(Word.fromSymbols(true, false)).to("s0")
                .from("s2").on(Word.fromSymbols(false, true)).withOutput(Word.fromSymbols(true, false)).to("s0")
                .from("s2").on(Word.fromSymbols(true, true)).withOutput(Word.fromSymbols(true, false)).to("s0")
                .from("s2").on(Word.fromSymbols(true, false)).withOutput(Word.fromSymbols(false, false)).to("s3")

                .from("s3").on(Word.fromSymbols(false, false)).withOutput(Word.fromSymbols(true, false)).to("s3")
                .from("s3").on(Word.fromSymbols(false, true)).withOutput(Word.fromSymbols(true, false)).to("s0")
                .from("s3").on(Word.fromSymbols(true, true)).withOutput(Word.fromSymbols(true, false)).to("s3")
                .from("s3").on(Word.fromSymbols(true, false)).withOutput(Word.fromSymbols(false, false)).to("s4")

                .from("s4").on(Word.fromSymbols(false, false)).withOutput(Word.fromSymbols(true, false)).to("s3")
                .from("s4").on(Word.fromSymbols(false, true)).withOutput(Word.fromSymbols(true, false)).to("s0")
                .from("s4").on(Word.fromSymbols(true, true)).withOutput(Word.fromSymbols(true, false)).to("s4")
                .from("s4").on(Word.fromSymbols(true, false)).withOutput(Word.fromSymbols(false, false)).to("s5")

                .from("s5").on(Word.fromSymbols(false, false)).withOutput(Word.fromSymbols(true, false)).to("s4")
                .from("s5").on(Word.fromSymbols(false, true)).withOutput(Word.fromSymbols(true, false)).to("s0")
                .from("s5").on(Word.fromSymbols(true, true)).withOutput(Word.fromSymbols(true, false)).to("s2")
                .from("s5").on(Word.fromSymbols(true, false)).withOutput(Word.fromSymbols(false, false)).to("s5")
                .create();
    }

    public static MealySimulatorSUL<Word<Boolean>, Object> createExampleSUL() {
        return new MealySimulatorSUL<>(createExample());
    }
}
