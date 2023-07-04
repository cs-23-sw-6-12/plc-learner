package org.cs23sw612.SUL;

import de.learnlib.driver.util.MealySimulatorSUL;
import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.util.automata.builders.AutomatonBuilders;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.cs23sw612.Util.AlphabetUtil;
import org.cs23sw612.Util.Bit;

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
                .withOutput(Word.fromSymbols(false, true)).to("s1").create();
    }
    public static CompactMealy<Word<Bit>, Object> createBitExample() {
        return AutomatonBuilders.newMealy(Bit.createAlphabet(2)).withInitial("s0").from("s0").on(Word.fromSymbols(Bit.LOW(), Bit.LOW()))
                .withOutput(Word.fromSymbols(Bit.HIGH(), Bit.LOW())).to("s0").from("s0").on(Word.fromSymbols(Bit.LOW(), Bit.HIGH()))
                .withOutput(Word.fromSymbols(Bit.HIGH(), Bit.LOW())).to("s0").from("s0").on(Word.fromSymbols(Bit.HIGH(), Bit.HIGH()))
                .withOutput(Word.fromSymbols(Bit.LOW(), Bit.LOW())).to("s0").from("s0").on(Word.fromSymbols(Bit.HIGH(), Bit.LOW()))
                .withOutput(Word.fromSymbols(Bit.LOW(), Bit.HIGH())).to("s1").from("s1").on(Word.fromSymbols(Bit.LOW(), Bit.HIGH()))
                .withOutput(Word.fromSymbols(Bit.HIGH(), Bit.LOW())).to("s0").from("s1").on(Word.fromSymbols(Bit.HIGH(), Bit.HIGH()))
                .withOutput(Word.fromSymbols(Bit.LOW(), Bit.LOW())).to("s0").from("s1").on(Word.fromSymbols(Bit.LOW(), Bit.LOW()))
                .withOutput(Word.fromSymbols(Bit.HIGH(), Bit.HIGH())).to("s1").from("s1").on(Word.fromSymbols(Bit.HIGH(), Bit.LOW()))
                .withOutput(Word.fromSymbols(Bit.LOW(), Bit.HIGH())).to("s1").create();
    }

    public static MealySimulatorSUL<Word<Boolean>, Object> createExampleSUL() {
        return new MealySimulatorSUL<>(createExample());
    }
    public static MealySimulatorSUL<Word<Bit>, Object> createBitExampleSUL() {
        return new MealySimulatorSUL<>(createBitExample());
    }
}
