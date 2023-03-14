package org.cs23sw612;

import de.learnlib.algorithms.dhc.mealy.MealyDHC;
import de.learnlib.filter.cache.sul.SULCache;
import de.learnlib.oracle.equivalence.CompleteExplorationEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import java.io.*;
import net.automatalib.automata.concepts.Output;
import net.automatalib.words.Word;
import org.cs23sw612.Adapters.Input.BooleanWordInputAdapter;
import org.cs23sw612.Adapters.InputAdapter;
import org.cs23sw612.Adapters.Output.BooleanWordOutputAdapter;
import org.cs23sw612.Adapters.OutputAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SULClientTest {
  String address = "localhost";
  int port = 1337;

  @Test
  public void testBooleanArraySentUsingBAjER() throws IOException {
    byte[] inBytes = new byte[] {0, 1};
    byte[] outBytes = new byte[] {1, 0};
    ServerThread serverThread = new ServerThread(port, inBytes, outBytes);
    serverThread.start();

    SULClientConfiguration connectionConfig = new SULClientConfiguration(address, port);
    SULClient<Boolean[], InputAdapter<Boolean[]>, Boolean[], OutputAdapter<Boolean[]>> sul = null;

    sul = SULClient.createBooleanArrayClient(connectionConfig);

    Boolean[] input =
        new Boolean[] {
          true, false,
        };
    Boolean[] output = sul.step(input);

    Assertions.assertArrayEquals(output, new Boolean[] {true, false});
  }

  @Test
  public void testSULClient() throws IOException {
    TestServer<Word<Boolean>, Object> ts = new TestServer<Word<Boolean>, Object>();
    ts.sul = ExampleSUL.createExampleSUL();
    ts.inputAdapter = new BooleanWordInputAdapter();
    ts.outputAdapter = new BooleanWordOutputAdapter();
    ts.port = 1337;
    ts.start();

    SULClient<Word<Boolean>, BooleanWordInputAdapter, Word<Boolean>, BooleanWordOutputAdapter>
        client =
            SULClient.createClient(
                new SULClientConfiguration("localhost", ts.port),
                new BooleanWordInputAdapter(),
                new BooleanWordOutputAdapter());

    SULWrapper<Word<Boolean>, Word<Boolean>> wrapper = new SULWrapper<>(client);
    // Standard mealy membership oracle.
    SULCache<Word<Boolean>, Word<Boolean>> cache =
        SULCache.createTreeCache(ExampleSUL.alphabet, wrapper);
    SULOracle<Word<Boolean>, Word<Boolean>> membershipOracle = new SULOracle<>(cache);

    CompleteExplorationEQOracle<
            Output<Word<Boolean>, Word<Word<Boolean>>>, Word<Boolean>, Word<Word<Boolean>>>
        equivalenceOracle = new CompleteExplorationEQOracle<>(membershipOracle, 3);

    MealyDHC<Word<Boolean>, Word<Boolean>> learner =
        new MealyDHC<>(ExampleSUL.alphabet, membershipOracle);

    Experiment.MealyExperiment<Word<Boolean>, Word<Boolean>> experiment =
        new Experiment.MealyExperiment<>(learner, equivalenceOracle, ExampleSUL.alphabet);

    experiment.run();

    Assertions.assertEquals(84, wrapper.getCounter());
  }
}
