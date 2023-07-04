package org.cs23sw612.SUL;

import de.learnlib.driver.util.MealySimulatorSUL;
import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.util.automata.builders.AutomatonBuilders;
import net.automatalib.words.Word;
import org.cs23sw612.Util.Bit;

public class ExampleSUL {
    public static CompactMealy<Word<Bit>, Object> createExample() {
        return AutomatonBuilders.newMealy(Bit.createAlphabet(2)).withInitial("s0").from("s0")
                .on(Word.fromSymbols(Bit.LOW(), Bit.LOW())).withOutput(Word.fromSymbols(Bit.HIGH(), Bit.LOW())).to("s0")
                .from("s0").on(Word.fromSymbols(Bit.LOW(), Bit.HIGH()))
                .withOutput(Word.fromSymbols(Bit.HIGH(), Bit.LOW())).to("s0").from("s0")
                .on(Word.fromSymbols(Bit.HIGH(), Bit.HIGH())).withOutput(Word.fromSymbols(Bit.LOW(), Bit.LOW()))
                .to("s0").from("s0").on(Word.fromSymbols(Bit.HIGH(), Bit.LOW()))
                .withOutput(Word.fromSymbols(Bit.LOW(), Bit.HIGH())).to("s1").from("s1")
                .on(Word.fromSymbols(Bit.LOW(), Bit.HIGH())).withOutput(Word.fromSymbols(Bit.HIGH(), Bit.LOW()))
                .to("s0").from("s1").on(Word.fromSymbols(Bit.HIGH(), Bit.HIGH()))
                .withOutput(Word.fromSymbols(Bit.LOW(), Bit.LOW())).to("s0").from("s1")
                .on(Word.fromSymbols(Bit.LOW(), Bit.LOW())).withOutput(Word.fromSymbols(Bit.HIGH(), Bit.HIGH()))
                .to("s1").from("s1").on(Word.fromSymbols(Bit.HIGH(), Bit.LOW()))
                .withOutput(Word.fromSymbols(Bit.LOW(), Bit.HIGH())).to("s1").create();
    }

    public static MealySimulatorSUL<Word<Bit>, Object> createExampleSUL() {
        return new MealySimulatorSUL<>(createExample());
    }
}
