package org.cs_23_sw_6_12;

import org.cs_23_sw_6_12.Adapters.Input.ByteArrayInputAdapter;
import org.cs_23_sw_6_12.Adapters.Output.ByteArrayOutputAdapter;
import org.cs_23_sw_6_12.Adapters.InputAdapter;
import org.cs_23_sw_6_12.Adapters.OutputAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SULClientTest {
    String address = "localhost";
    int port = 1337;

    /*@Test
    public void testClientConnection() throws IOException {
        byte[] inBytes = new byte[]{0,1};
        byte[] outBytes = new byte[]{1,0};

        ServerThread serverThread = new ServerThread(port, inBytes, outBytes);
        serverThread.start();

        SULClientConfiguration connectionConfig = new SULClientConfiguration(address, port);
        SULClient<byte[], ByteArrayInputAdapter, byte[], ByteArrayOutputAdapter> sul = null;

        sul = SULClient.createByteArrayClient(connectionConfig);

        byte[] output = sul.step(inBytes);

        Assertions.assertArrayEquals(output, outBytes);
    }

     */

    @Test
    public void testBooleanArraySentUsingBAjER() throws IOException {
        byte[] inBytes = new byte[]{0,1};
        byte[] outBytes = new byte[]{1,0};
        ServerThread serverThread = new ServerThread(port, inBytes, outBytes);
        serverThread.start();

        SULClientConfiguration connectionConfig = new SULClientConfiguration(address, port);
        SULClient<Boolean[], InputAdapter<Boolean[]>, Boolean[], OutputAdapter<Boolean[]>> sul = null;

        sul = SULClient.createBooleanArrayClient(connectionConfig);

        Boolean[] input = new Boolean[]{true, false,};
        Boolean[] output = sul.step(input);

        Assertions.assertArrayEquals(output, new Boolean[]{true, false});
    }

}

