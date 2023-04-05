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
import org.cs23sw612.BAjER.BAjERClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SULClientTest {
    String address = "localhost";
    int port = 1337;

    @Test
    public void testBoleanArraySentUsingBAjER() throws IOException {
        byte[] inBytes = new byte[]{0, 1};
        byte[] outBytes = new byte[]{1, 0};
        ServerThread serverThread = new ServerThread(port, inBytes, outBytes);
        serverThread.start();

        BAjERClient bAjERClient = new BAjERClient();
        bAjERClient.Connect("127.0.0.1", port);

        var inputAdapter = new BooleanWordInputAdapter();
        var outputAdapter = new BooleanWordOutputAdapter();
        var sul = new SULClient<>(bAjERClient, inputAdapter, outputAdapter, (byte) 2, (byte) 2);

        var input = outputAdapter.fromBits(new Boolean[] {true, false});
        var output = sul.step(input);

        Assertions.assertArrayEquals(inputAdapter.getBits(output), new Boolean[]{true, false});
    }
}
