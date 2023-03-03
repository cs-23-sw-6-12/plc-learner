package org.cs_23_sw_6_12;

import org.cs_23_sw_6_12.InputAdapters.ByteArrayInputAdapter;
import org.cs_23_sw_6_12.InputAdapters.ByteArrayOutputAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SULClientTest {
    String address = "localhost";
    int port = 1337;

    @Test
    public void testClientConnection() throws IOException {
        ServerThread serverThread = new ServerThread(port);
        serverThread.start();

        var connectionConfig = new SULClientConfiguration(address, port);
        SULClient<byte[], ByteArrayInputAdapter, byte[], ByteArrayOutputAdapter> sul = null;

        sul = SULClient.createByteArrayClient(connectionConfig);

        byte[] input = new byte[]{0,1,0};
        var output = sul.step(input);

        Assertions.assertArrayEquals(output, new byte[]{0,0,1});
    }

}

