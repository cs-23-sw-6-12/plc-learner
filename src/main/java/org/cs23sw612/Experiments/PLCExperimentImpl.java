package org.cs23sw612.Experiments;

import de.learnlib.api.algorithm.LearningAlgorithm;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.util.Experiment;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.graphs.Graph;
import net.automatalib.serialization.dot.DOTSerializationProvider;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.cs23sw612.Ladder.Ladder;
import org.cs23sw612.Ladder.Visualization.Visualizer;
import org.cs23sw612.Util.Bit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PLCExperimentImpl implements IPLCExperiment {

    private final Alphabet<Word<Bit>> alphabet;
    private final String outputFileName;
    private final boolean visualizeMachine, visualizeLadder;
    private final Logger logger;

    public PLCExperimentImpl(
            LearningAlgorithm<? extends MealyMachine<?, Word<Bit>, ?, Word<Bit>>, Word<Bit>, Word<Word<Bit>>> learningAlgorithm,
            EquivalenceOracle<? super MealyMachine<?, Word<Bit>, ?, Word<Bit>>, Word<Bit>, Word<Word<Bit>>> equivalenceOracle,
            Alphabet<Word<Bit>> alphabet, String outputFileName, boolean visualizeMachine,
            boolean visualizeLadder) {
        this.alphabet = alphabet;
        this.outputFileName = outputFileName;
        this.visualizeMachine = visualizeMachine;
        this.visualizeLadder = visualizeLadder;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.experiment = new Experiment.MealyExperiment<>(learningAlgorithm, equivalenceOracle, alphabet);
    }

    private final Experiment.MealyExperiment<Word<Bit>, Word<Bit>> experiment;

    @Override
    public void run() {
        experiment.run();

        MealyMachine result = experiment.getFinalHypothesis();

        if (outputFileName != null)
            saveResultAsDot(result);

        if (visualizeMachine)
            Visualization.visualize(result, alphabet);

        /*
        if (visualizeLadder) {
            // TODO: Redo with new struct
        }
         */
    }

    @SuppressWarnings({"unchecked"})
    private void saveResultAsDot(MealyMachine<?, Word<Bit>, ?, Word<Bit>> result) {
        FileOutputStream outFile;
        try {
            outFile = new FileOutputStream(outputFileName);

            DOTSerializationProvider.getInstance().writeModel(outFile,
                    (Graph) result.transitionGraphView(alphabet).asNormalGraph());
        } catch (FileNotFoundException ex) {
            logger.error("Could not find file and save DOT: ", ex);
        } catch (IOException ex) {
            logger.error("Could not write to file: ", ex);
        }
    }
}
