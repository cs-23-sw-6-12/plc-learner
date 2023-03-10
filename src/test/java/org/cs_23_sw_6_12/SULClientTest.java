package org.cs_23_sw_6_12;

import de.learnlib.algorithms.dhc.mealy.MealyDHC;
import de.learnlib.filter.cache.sul.SULCache;
import de.learnlib.oracle.equivalence.CompleteExplorationEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import net.automatalib.words.Word;
import org.cs_23_sw_6_12.Adapters.Input.BooleanWordInputAdapter;
import org.cs_23_sw_6_12.Adapters.Output.BooleanWordOutputAdapter;
import org.cs_23_sw_6_12.Adapters.InputAdapter;
import org.cs_23_sw_6_12.Adapters.OutputAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

public class SULClientTest {
    String address = "localhost";
    int port = 1337;

    @Test
    public void testBooleanArraySentUsingBAjER() throws IOException {
        byte[] inBytes = new byte[]{0,1};
        byte[] outBytes = new byte[]{1,0};
        ServerThread serverThread = new ServerThread(port, inBytes, outBytes);
        serverThread.start();

        var connectionConfig = new SULClientConfiguration(address, port);
        SULClient<Boolean[], InputAdapter<Boolean[]>, Boolean[], OutputAdapter<Boolean[]>> sul = null;

        sul = SULClient.createBooleanArrayClient(connectionConfig);

        Boolean[] input = new Boolean[]{true, false,};
        var output = sul.step(input);

        Assertions.assertArrayEquals(output, new Boolean[]{true, false});
    }

    @Test
    public void testSULClient() throws IOException {
        var ts = new TestServer<Word<Boolean>, Object>();
        ts.sul = ExampleSUL.createExample();
        ts.inputAdapter = new BooleanWordInputAdapter();
        ts.outputAdapter = new BooleanWordOutputAdapter();
        ts.port=1337;
        ts.start();

        var client = SULClient.createClient(new SULClientConfiguration("localhost", ts.port),
                new BooleanWordInputAdapter(), new BooleanWordOutputAdapter());

        var wrapper = new SULWrapper<>(client);
        // Standard mealy membership oracle.
        var cache = SULCache.createTreeCache(ExampleSUL.alphabet, wrapper);
        var membershipOracle = new SULOracle<>(cache);

        var equivalenceOracle = new CompleteExplorationEQOracle<>(membershipOracle, 3);

        var learner = new MealyDHC<>(ExampleSUL.alphabet, membershipOracle);

        var experiment =
                new Experiment.MealyExperiment<>(learner, equivalenceOracle,ExampleSUL.alphabet);

        experiment.run();

        Assertions.assertEquals(84, wrapper.getCounter());
    }

}

