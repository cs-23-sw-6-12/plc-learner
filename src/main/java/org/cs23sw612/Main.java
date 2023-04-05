package org.cs23sw612;

import de.learnlib.algorithms.dhc.mealy.MealyDHC;
import de.learnlib.filter.cache.sul.SULCache;
import de.learnlib.oracle.equivalence.CompleteExplorationEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import org.cs23sw612.Ladder.EquationCollection;
import org.cs23sw612.Ladder.Ladder;
import org.cs23sw612.Ladder.Visualization.Visualizer;
import org.cs23sw612.Util.AlphabetUtil;
import org.jfree.svg.SVGUtils;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        /*
        var sul = SULClient.createClient(new SULClientConfiguration(args[0], Integer.parseInt(args[1])),
                new IntegerWordInputAdapter(), new IntegerWordOutputAdapter());
        sul.numberofinputs = 3;
        sul.numberofoutputs = 1;
        */
        var sul = ExampleSUL.createExampleSUL();
        var alphabet = AlphabetUtil.createAlphabet(2);

        // This cache reduces the amount of "queries" that are actually executed on the
        // SUL.
        var cache = SULCache.createTreeCache(alphabet, sul);

        // Standard mealy membership oracle.
        var membershipOracle = new SULOracle<>(cache);

        var equivalenceOracle = new CompleteExplorationEQOracle<>(membershipOracle, 3);

        var learner = new MealyDHC<>(alphabet, membershipOracle);

        var experiment = new Experiment.MealyExperiment<>(learner, equivalenceOracle, alphabet);

        experiment.run();

        var e = new EquationCollection(experiment.getFinalHypothesis(), alphabet);
        var l = new Ladder(e);

        var svg = Visualizer.layoutSVG(l);
        SVGUtils.writeToSVG(new File("svg.svg"), svg.getSVGElement());
        System.out.println(l.rungs);

        /*
        var result = experiment.getFinalHypothesis();

        Visualization.visualize(result, alphabet);
        */

    }
}
