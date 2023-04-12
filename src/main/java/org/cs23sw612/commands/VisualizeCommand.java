package org.cs23sw612.commands;

import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.serialization.dot.DOTParsers;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Alphabet;
import picocli.CommandLine;

import java.io.FileInputStream;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "visualize", mixinStandardHelpOptions = true, version = "0.1.0", description = "Visualizes a PLC loaded from a DOT file")
public class VisualizeCommand implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "File path to the DOT file")
    private String filePath;

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

        CompactMealy<String, String> model;
        Alphabet<String> alphabet;

        try {
            var parsed = DOTParsers.mealy().readModel(file);
            model = parsed.model;
            alphabet = parsed.alphabet;
        } catch (Exception ex) {
            System.err.println("Could not parse the given file");
            System.err.println(ex.getMessage());
            return 1;
        }
        try {
            Visualization.visualize(model, alphabet);
        } catch (Exception ex) {
            System.err.println("Could not visualize the given model");
            System.err.println(ex.getMessage());
            return 1;
        }

        return 0;
    }
}
