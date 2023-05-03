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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PLCExperimentImpl<I, O> implements IPLCExperiment<I, O> {

    private final Alphabet<I> alphabet;
    private final String outputFileName;
    private final boolean visualize;
    private final Logger logger;

    public PLCExperimentImpl(LearningAlgorithm<? extends MealyMachine<?, I, ?, O>, I, Word<O>> learningAlgorithm,
            EquivalenceOracle<? super MealyMachine<?, I, ?, O>, I, Word<O>> equivalenceOracle, Alphabet<I> alphabet,
            String outputFileName, boolean visualize) {
        this.alphabet = alphabet;
        this.outputFileName = outputFileName;
        this.visualize = visualize;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.experiment = new Experiment.MealyExperiment<>(learningAlgorithm, equivalenceOracle, alphabet);
    }

    private final Experiment.MealyExperiment<I, O> experiment;

    @Override
    public void run() {
        experiment.run();

        var result = experiment.getFinalHypothesis();

        if (outputFileName != null)
            saveResultAsDot(result);

        if (visualize)
            Visualization.visualize(result, alphabet);
    }

    @SuppressWarnings({"unchecked"})
    private void saveResultAsDot(MealyMachine<?, I, ?, O> result) {
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
