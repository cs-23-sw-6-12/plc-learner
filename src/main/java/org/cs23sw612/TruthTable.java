package org.cs23sw612;

import net.automatalib.automata.concepts.StateIDs;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.words.Alphabet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class TruthTable<S, I, T extends CompactMealyTransition<O>, O, M extends MealyMachine<S, I, T, O>, A extends Alphabet<I>> {
    private final List<String> inputs;
    private final List<String> outputs = new ArrayList<>();
    private final List<S> states;
    private final List<TruthRow<S, I, O>> rows = new ArrayList<>();
    private final A alphabet;
    private final StateIDs<S> stateIds;
    private static final Function<? super Boolean, String> boolToInt = i -> i ? "1" : "0";
    public TruthTable(M machine, A alphabet) {
        this.alphabet = alphabet;
        this.stateIds = machine.stateIDs();
        this.states = machine.getStates().stream().toList();
        this.inputs = alphabet.stream().map(I::toString).toList();

        machine.getStates().forEach((state) -> alphabet.forEach((word) -> {
            T trans = machine.getTransition(state, word);
            assert trans != null;
            O output = trans.getOutput();
            if (!this.outputs.contains(output.toString()))
                this.outputs.add(output.toString());

            rows.add(new TruthRow<>(fromInputValue(word),
                    fromStateValue(stateIds.getStateId(state)),
                    fromStateValue(trans.getSuccId()),
                    trans.getOutput()));
        }));
    }

    private List<Boolean> fromInputValue(I val) {
        return alphabet.stream().map((word) -> word == val).toList();
    }
    private List<Boolean> fromStateValue(int id) {
        return states.stream().map((state) -> stateIds.getStateId(state) == id).toList();
    }

    public void generateTable() {
        //rows.forEach(System.out::println);

    }

    @Override
    public String toString() {
        return asString("|", "||");
    }

    public String toLatexTabularXString() {
        String sep = "\\\\\\hline\n";
        return String.format("\\begin{tabularx}{\\linewidth}{%s}\\hline\n%s%s\\end{tabularx}",
                latexTableHeader(),
                asString("&", "&", sep, "\\hline"), sep);
    }

    private String asString(String sep) {
        return asString(sep, sep, "\n");
    }
    private String asString(String sep, String catSep) {
        return asString(sep, catSep, "\n", "");
    }
    private String asString(String sep, String catSep, String lineSep) {
        return asString(sep, catSep, lineSep, "");
    }
    private String asString(String sep, String catSep, String lineSep, String headerSep) {
        String header = String.format("%s %s %s %s %s %s %s",
                String.join(" " + sep + " ", inputs), catSep, //inputs
                String.join(" " + sep + " ", states.stream().map(S::toString).toList()), catSep, //states
                String.join(" " + sep + " ", states.stream().map((s -> s.toString() + "'")).toList()), catSep, //next states
                //String.join(" " + sep + " ", outputs)); //outputs //TODO: Output variable only
                "Output"); //outputs
        String body = String.join(lineSep, rows.stream().map(row -> row.asString(sep, catSep, boolToInt)).toList());
        return header + lineSep + headerSep + body;
    }

    private String latexTableHeader() {
        return "|" + String.join("|", inputs.stream().map((i) -> "X").toList()) + "||" +
                String.join("|", states.stream().map((i) -> "X").toList()) + "||" +
                String.join("|", states.stream().map((i) -> "X").toList()) + "|||" +
                //String.join("|", outputs.stream().map((i) -> "X").toList()) + "|";
                "X|";
    }

    record TruthRow<S, I, O>(List<Boolean> ins, List<Boolean> states, List<Boolean> nextStates, O out) {
        @Override
        public String toString() {
            return String.format("%s | %s | %s || %s", ins, states, nextStates, out);
        }

        private String asString(String sep) {
            return asString(sep, sep);
        }

        private String asString(String sep, String catSep) {
            return asString(sep, catSep, Object::toString);
        }
        private String asString(String sep, String catSep, Function<? super Boolean, String> conv) {
            return String.format("%s %s %s %s %s %s %s",
                    String.join(" " + sep + " ", ins.stream().map(conv).toList()), catSep, //inputs
                    String.join(" " + sep + " ", states.stream().map(conv).toList()), catSep, //states
                    String.join(" " + sep + " ", nextStates.stream().map(conv).toList()), catSep, //next states
                    out);
        }
    }

}

