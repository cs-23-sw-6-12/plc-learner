package org.cs23sw612;

import de.learnlib.algorithms.dhc.mealy.MealyDHC;
import de.learnlib.filter.cache.sul.SULCache;
import de.learnlib.oracle.equivalence.CompleteExplorationEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;

import java.io.File;
import java.io.IOException;
import net.automatalib.visualization.Visualization;
import org.cs23sw612.Adapters.Input.IntegerWordInputAdapter;
import org.cs23sw612.Adapters.Output.IntegerWordOutputAdapter;
import org.cs23sw612.Util.AlphabetUtil;
import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUtils;

import static org.cs23sw612.Ladder.Visualization.svg_components.*;

public class Main {
    public static void main(String[] args) throws IOException {
          SVGGraphics2D g2 = new SVGGraphics2D(110, 150);
          NO(g2,25,30);
          NC(g2,25,65);
          Coil(g2,25,100);
          SVGUtils.writeToSVG(new File("test_image.svg"), g2.getSVGElement());

    //    var sul = SULClient.createClient(new SULClientConfiguration(args[0], Integer.parseInt(args[1])),
    //            new IntegerWordInputAdapter(), new IntegerWordOutputAdapter());
    //    sul.numberofinputs = 3;
    //    sul.numberofoutputs = 1;
    //
    //    var alphabet = AlphabetUtil.createIntAlphabet(3);
    //
    //    // This cache reduces the amount of "queries" that are actually executed on the
    //    // SUL.
    //    var cache = SULCache.createTreeCache(alphabet, sul);
    //
    //    // Standard mealy membership oracle.
    //    var membershipOracle = new SULOracle<>(cache);
    //
    //    var equivalenceOracle = new CompleteExplorationEQOracle<>(membershipOracle, 3);
    //
    //    var learner = new MealyDHC<>(alphabet, membershipOracle);
    //
    //    var experiment = new Experiment.MealyExperiment<>(learner, equivalenceOracle, alphabet);
    //
    //    experiment.run();
    //
    //    var result = experiment.getFinalHypothesis();
    //
    //    Visualization.visualize(result, alphabet);
    }
}
