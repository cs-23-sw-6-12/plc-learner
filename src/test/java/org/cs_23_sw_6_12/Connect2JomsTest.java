package org.cs_23_sw_6_12;

import org.cs_23_sw_6_12.Adapters.InputAdapter;
import org.cs_23_sw_6_12.Adapters.OutputAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Connect2JomsTest {
    int port = 18041;
    String address = "6.tcp.eu.ngrok.io";
    /*

    @Test
    public void testBooleanArraySentUsingBAjER() throws IOException {
        SULClientConfiguration connectionConfig = new SULClientConfiguration(address, port);
        SULClient<Boolean[], InputAdapter<Boolean[]>, Boolean[], OutputAdapter<Boolean[]>> sul;
        sul = SULClient.createBooleanArrayClient(connectionConfig);

        Boolean[] input = new Boolean[]{true, true, false, true, false};
        sul.numberofinputs = input.length;
        sul.numberofoutputs = 0;

        // I have to reset because Joms is cringe.
        sul.pre();

        Boolean[] output = sul.step(input);

        // Joms will send me an empty array.
        Assertions.assertArrayEquals(output, new Boolean[]{});

    }
     */
}
