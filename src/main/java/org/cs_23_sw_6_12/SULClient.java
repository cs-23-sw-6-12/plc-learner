package org.cs_23_sw_6_12;

import de.learnlib.api.exception.SULException;
import net.automatalib.words.Word;
import org.cs_23_sw_6_12.Adapters.Input.BooleanArrayInputAdapter;
import org.cs_23_sw_6_12.Adapters.Input.BooleanWordInputAdapter;
import org.cs_23_sw_6_12.Adapters.Input.ByteArrayInputAdapter;
import org.cs_23_sw_6_12.Adapters.Input.StringInputAdapter;
import org.cs_23_sw_6_12.Adapters.Output.BooleanArrayOutputAdapter;
import org.cs_23_sw_6_12.Adapters.Output.BooleanWordOutputAdapter;
import org.cs_23_sw_6_12.Adapters.Output.ByteArrayOutputAdapter;
import org.cs_23_sw_6_12.Adapters.Output.StringOutputAdapter;
import org.cs_23_sw_6_12.Adapters.InputAdapter;
import org.cs_23_sw_6_12.Adapters.OutputAdapter;
import org.cs_23_sw_6_12.Interfaces.Logger;
import org.cs_23_sw_6_12.Interfaces.SULTimed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SULClient<I, IA extends InputAdapter<I>, O, OA extends OutputAdapter<O>> implements SULTimed<I, O> {

    private int count = 0;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private IA inputAdapter;
    private OA outputAdapter;
    public int numberofoutputs = 2;
    public int numberofinputs = 2;
    private Logger logger;

    private SULClient(){
        logger = new StreamLogger(System.out);
    }

    public static <I, IA extends InputAdapter<I>, O, OA extends OutputAdapter<O>> SULClient<I, IA, O, OA> createClient(Socket socket, IA inputAdapter, OA outputAdapter) throws IOException {
        var client = new SULClient<I, IA, O, OA>();
        client.socket = socket;
        client.out = new PrintWriter(socket.getOutputStream());
        client.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        client.inputAdapter = inputAdapter;
        client.outputAdapter = outputAdapter;

        return client;
    }

    /// Creates a SULClient without a socket, that will just write to any PrintWriter and read from any BufferedReader.
    public static <I, IA extends InputAdapter<I>, O, OA extends OutputAdapter<O>> SULClient<I, IA, O, OA> createClient(PrintWriter writer, BufferedReader reader, IA inputAdapter, OA outputAdapter) throws IOException {
        var client = new SULClient<I, IA, O, OA>();
        client.out = writer;
        client.in = reader;
        client.inputAdapter = inputAdapter;
        client.outputAdapter = outputAdapter;

        return client;
    }

    public static <I, IA extends InputAdapter<I>, O, OA extends OutputAdapter<O>> SULClient<I, IA, O, OA> createClient(SULClientConfiguration configuration, IA inputAdapter, OA outputAdapter) throws IOException {
        // Configure socket.
        Socket socket = new Socket(configuration.address(), configuration.port());
        return createClient(socket,inputAdapter,outputAdapter);
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

    public static SULClient<Boolean[],InputAdapter<Boolean[]>, Boolean[], OutputAdapter<Boolean[]>> createBooleanArrayClient(SULClientConfiguration configuration) throws IOException {
        var inputAdapter = new BooleanArrayInputAdapter();
        var outputAdapter = new BooleanArrayOutputAdapter();

        return createClient(configuration, inputAdapter, outputAdapter);
    }

    public static SULClient<Word<Boolean>,InputAdapter<Word<Boolean>>, Word<Boolean>, OutputAdapter<Word<Boolean>>> createBooleanWordClient(SULClientConfiguration configuration) throws IOException {
        var inputAdapter = new BooleanWordInputAdapter();
        var outputAdapter = new BooleanWordOutputAdapter();

        return createClient(configuration, inputAdapter, outputAdapter);
    }


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
        logger.log(LogEntrySeverity.INFO, "Starting experiment {%d}", count++);
        // Reset SUL
        var resetCode = BAjER.resetCode;
        out.write(resetCode);
        out.flush();
        logger.log(LogEntrySeverity.INFO, "Sent: {%s}", charArrayToString(resetCode));

        try {
            int response = in.read();
            if (response != 0) {
                throw new RuntimeException("AAAAAAH, COULD NOT RESET");
            }
            logger.log(LogEntrySeverity.INFO, "SUL Reset, response from server: {%d}", response);

            // Send [2] Setup, number of inputs, number of outputs.
            var setupCode = BAjER.setup(numberofinputs, numberofoutputs);
            out.write(setupCode);
            out.flush();

            logger.log(LogEntrySeverity.INFO, "Sent: {%s}", charArrayToString(setupCode));

            response = in.read();

            logger.log(LogEntrySeverity.INFO, "SUL Setup, response from server: {%d}", response);
            if (response != 0) {
                throw new RuntimeException("AAAAAAH, COULD NOT SETUP");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void post() {

    }

    private String byteArrayToString(byte ... bytes){
        StringBuilder result = new StringBuilder("[ ");
        for (byte b:
             bytes) {
            result.append(b);
            result.append(", ");
        }
        return result + "]";
    }
    private String charArrayToString(char[] chars){
        StringBuilder result = new StringBuilder("[ ");
        for (char c:
                chars) {
            result.append((int) c);
            result.append(", ");
        }
        return result + "]";
    }
    @Override
    public O step(I input) {
        // Send input.
        var stepCode = BAjER.step(inputAdapter.toBytes(input));
        out.print(stepCode);
        out.flush();

        logger.log(LogEntrySeverity.INFO, "Sent: {%s}", charArrayToString(stepCode));

        // Return output.
        try {
            byte[] bytes = new byte[numberofoutputs];
            int code = in.read();
            if (code != 0)
                throw new RuntimeException("AAAAAAAAAAAAAAAH. STEP RETURNED: " + code);

            var logbytes = new byte[numberofoutputs+1];
            logbytes[0] = (byte) code;
            for (int i = 0; i < bytes.length; i++) {
                int value = in.read(); // Maybe check that the stream has not ended?
                bytes[i] = (byte) value;

                logbytes[i+1] = (byte) value;
            }
            logger.log(LogEntrySeverity.INFO, "SUL Step, response from server: {%s}" , byteArrayToString(logbytes));

            return outputAdapter.fromBytes(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

