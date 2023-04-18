package org.cs23sw612.commands;

import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.commons.util.Pair;
import net.automatalib.serialization.dot.DOTParsers;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.cs23sw612.Ladder.EquationCollection;
import org.cs23sw612.Ladder.Ladder;
import org.cs23sw612.Ladder.Visualization.Visualizer;
import picocli.CommandLine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@CommandLine.Command(name = "visualize", mixinStandardHelpOptions = true, version = "0.1.0", description = "Visualizes a PLC loaded from a DOT file")
public class VisualizeCommand implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "File path to the DOT file")
    private String filePath;
    @CommandLine.Option(names = "--save-svg", description = "Save the generated svg file.")
    private String svgOutputPath;

    @Override
    public Integer call() throws Exception {
        FileInputStream file;
        try {
            file = new FileInputStream(filePath);
        } catch (Exception ex) {
            System.err.println("Could not open the given file");
            System.err.println(ex.getMessage());
            return 1;
        }

        CompactMealy<Word<Boolean>, Word<Boolean>> model;
        Alphabet<Word<Boolean>> alphabet;

        try {
            var parsed = DOTParsers.mealy(this::ParseBool).readModel(file);
            model = parsed.model;
            alphabet = parsed.alphabet;
        } catch (Exception ex) {
            System.err.println("Could not parse the given file");
            System.err.println(ex.getMessage());
            return 1;
        }
        try {
            var equationCollection = new EquationCollection<>(model, alphabet);
            var ladder = new Ladder(equationCollection);
            var ladderSvg = Visualizer.layoutSVG(ladder);

            if (svgOutputPath != null) {
                var f = new File(svgOutputPath);
                f.getParentFile().mkdirs();

                try {
                    Visualizer.saveSVG(ladderSvg, new FileWriter(f));
                } catch (IOException exception) {
                    System.err.println("Could not save SVG file");
                    System.err.println(exception.getMessage());
                    return 1;
                }
            }
            Visualizer.showSVG(ladderSvg);
        } catch (Exception ex) {
            System.err.println("Could not visualize the given model");
            System.err.println(ex.getMessage());
            return 1;
        }

        return 0;
    }

    Pair<@Nullable Word<Boolean>, @Nullable Word<Boolean>> ParseBool(Map<String, String> attr) {
        String label = attr.get("label");
        if (label == null) {
            return Pair.of(null, null);
        } else {
            String[] tokens = label.split("/");
            return tokens.length != 2 ? Pair.of(null, null) : Pair.of(getWord(tokens[0]), getWord(tokens[1]));
        }
    }

    private static Word<Boolean> getWord(String token) {
        return Word.fromList(
                Arrays.stream(token.trim().split(" "))
                        .map(s -> s.equals("1") || Boolean.parseBoolean(s))
                        .collect(Collectors.toList()));
    }

}
