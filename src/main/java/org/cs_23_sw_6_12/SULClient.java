package org.cs_23_sw_6_12;

import de.learnlib.api.exception.SULException;
import org.cs_23_sw_6_12.InputAdapters.ByteArrayInputAdapter;
import org.cs_23_sw_6_12.InputAdapters.ByteArrayOutputAdapter;
import org.cs_23_sw_6_12.InputAdapters.StringInputAdapter;
import org.cs_23_sw_6_12.InputAdapters.StringOutputAdapter;
import org.cs_23_sw_6_12.Interfaces.InputAdapter;
import org.cs_23_sw_6_12.Interfaces.OutputAdapter;
import org.cs_23_sw_6_12.Interfaces.SULTimed;

import java.io.*;
import java.net.Socket;

public class SULClient<I, IA extends InputAdapter<I>, O, OA extends OutputAdapter<O>> implements SULTimed<I, O> {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private IA inputAdapter;
    private OA outputAdapter;
    private int bytesToRead = 3;

    @Override
    public O step(I input, long stepClockLimit) throws SULException {
        return null;
    }

    @Override
    public long getClockLimit() {
        return 0;
    }

    @Override
    public void pre() {
        // Reset SUL
    }

    @Override
    public void post() {

    }

    @Override
    public O step(I input) {
        // Send input.
        out.println(new String(inputAdapter.toBytes(input)));
        out.flush();

        // Return output.
        try {
            byte[] bytes = new byte[bytesToRead];

            for (int i = 0; i < bytes.length; i++) {
                int value = in.read(); // Maybe check that the stream has not ended?
                bytes[i] = (byte) value;
            }

            return outputAdapter.fromBytes(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static <I, IA extends InputAdapter<I>, O, OA extends OutputAdapter<O>> SULClient<I, IA, O, OA> createClient(SULClientConfiguration configuration, IA inputAdapter, OA outputAdapter) throws IOException {
        var client = new SULClient<I,IA,O,OA>();

        // Configure socket.
        Socket socket = new Socket(configuration.address(), configuration.port());
        client.out = new PrintWriter(socket.getOutputStream());
        client.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        client.socket = socket;
        client.inputAdapter = inputAdapter;
        client.outputAdapter = outputAdapter;

        return client;
    }

    public static SULClient<String, StringInputAdapter, String, StringOutputAdapter> createStringClient(SULClientConfiguration configuration) throws IOException {
        var inputAdapter = new StringInputAdapter();
        var outputAdapter = new StringOutputAdapter();

        return createClient(configuration, inputAdapter, outputAdapter);
    }

    public static SULClient<byte[], ByteArrayInputAdapter, byte[], ByteArrayOutputAdapter> createByteArrayClient(SULClientConfiguration configuration) throws IOException {
        var inputAdapter = new ByteArrayInputAdapter();
        var outputAdapter = new ByteArrayOutputAdapter();

        return createClient(configuration, inputAdapter, outputAdapter);
    }
}

