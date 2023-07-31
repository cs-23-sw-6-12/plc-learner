package org.cs23sw612.commands;

import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.serialization.dot.DOTParsers;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.cs23sw612.Util.Bit;
import picocli.CommandLine;

import java.io.FileInputStream;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "visualize", mixinStandardHelpOptions = true, version = "0.1.0", description = "Visualizes a PLC loaded from a DOT file")
public class VisualizeCommand implements Callable<Integer> {
    @SuppressWarnings("unused")
    @CommandLine.Parameters(index = "0", description = "File path to the DOT file")
    private String filePath;

    @SuppressWarnings("unused")
    @CommandLine.Option(names = "--save-svg", description = "Save the generated svg file.")
    private String svgOutputPath;

    @Override
    public Integer call() {
        FileInputStream file;
        try {
            file = new FileInputStream(filePath);
        } catch (Exception ex) {
            System.err.println("Could not open the given file");
            System.err.println(ex.getMessage());
            return 1;
        }

        CompactMealy<Word<Bit>, Word<Bit>> model;
        Alphabet<Word<Bit>> alphabet;

        try {
            var parsed = DOTParsers.mealy(Bit::parseBit).readModel(file);
            model = parsed.model;
            alphabet = parsed.alphabet;
        } catch (Exception ex) {
            System.err.println("Could not parse the given file");
            System.err.println(ex.getMessage());
            return 1;
        }

        return 0;
    }
}
