package org.cs23sw612.commands;

import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.serialization.dot.DOTParsers;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.cs23sw612.Ladder.EquationCollection;
import org.cs23sw612.Ladder.Ladder;
import org.cs23sw612.Ladder.Visualization.Visualizer;
import org.cs23sw612.Util.AlphabetUtil;
import picocli.CommandLine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
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

        CompactMealy<Word<Boolean>, Word<Boolean>> model;
        Alphabet<Word<Boolean>> alphabet;

        try {
            var parsed = DOTParsers.mealy(AlphabetUtil::parseBool).readModel(file);
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
}
