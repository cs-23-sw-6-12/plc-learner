package org.cs23sw612;

import de.learnlib.api.exception.SULException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import net.automatalib.words.Word;
import org.cs23sw612.Adapters.Input.BooleanArrayInputAdapter;
import org.cs23sw612.Adapters.Input.BooleanWordInputAdapter;
import org.cs23sw612.Adapters.Input.ByteArrayInputAdapter;
import org.cs23sw612.Adapters.Input.StringInputAdapter;
import org.cs23sw612.Adapters.InputAdapter;
import org.cs23sw612.Adapters.Output.BooleanArrayOutputAdapter;
import org.cs23sw612.Adapters.Output.BooleanWordOutputAdapter;
import org.cs23sw612.Adapters.Output.ByteArrayOutputAdapter;
import org.cs23sw612.Adapters.Output.StringOutputAdapter;
import org.cs23sw612.Adapters.OutputAdapter;
import org.cs23sw612.Interfaces.SULTimed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SULClient<I, IA extends InputAdapter<I>, O, OA extends OutputAdapter<O>>
    implements SULTimed<I, O> {

  private final Logger logger = LoggerFactory.getLogger(SULClient.class);
  public int numberofoutputs = 2;
  public int numberofinputs = 2;
  private int count = 0;
  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;
  private IA inputAdapter;
  private OA outputAdapter;

  private SULClient() {}

  public static <I, IA extends InputAdapter<I>, O, OA extends OutputAdapter<O>>
      SULClient<I, IA, O, OA> createClient(Socket socket, IA inputAdapter, OA outputAdapter)
          throws IOException {
    SULClient<I, IA, O, OA> client = new SULClient<I, IA, O, OA>();
    client.socket = socket;
    client.out = new PrintWriter(socket.getOutputStream());
    client.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    client.inputAdapter = inputAdapter;
    client.outputAdapter = outputAdapter;

    return client;
  }

  /// Creates a SULClient without a socket, that will just write to any PrintWriter and read from
  // any BufferedReader.
  public static <I, IA extends InputAdapter<I>, O, OA extends OutputAdapter<O>>
      SULClient<I, IA, O, OA> createClient(
          PrintWriter writer, BufferedReader reader, IA inputAdapter, OA outputAdapter)
          throws IOException {
    SULClient<I, IA, O, OA> client = new SULClient<I, IA, O, OA>();
    client.out = writer;
    client.in = reader;
    client.inputAdapter = inputAdapter;
    client.outputAdapter = outputAdapter;

    return client;
  }

  public static <I, IA extends InputAdapter<I>, O, OA extends OutputAdapter<O>>
      SULClient<I, IA, O, OA> createClient(
          SULClientConfiguration configuration, IA inputAdapter, OA outputAdapter)
          throws IOException {
    // Configure socket.
    Socket socket = new Socket(configuration.address, configuration.port);
    return createClient(socket, inputAdapter, outputAdapter);
  }

  public static SULClient<String, StringInputAdapter, String, StringOutputAdapter>
      createStringClient(SULClientConfiguration configuration) throws IOException {
    StringInputAdapter inputAdapter = new StringInputAdapter();
    StringOutputAdapter outputAdapter = new StringOutputAdapter();

    return createClient(configuration, inputAdapter, outputAdapter);
  }

  public static SULClient<byte[], ByteArrayInputAdapter, byte[], ByteArrayOutputAdapter>
      createByteArrayClient(SULClientConfiguration configuration) throws IOException {
    ByteArrayInputAdapter inputAdapter = new ByteArrayInputAdapter();
    ByteArrayOutputAdapter outputAdapter = new ByteArrayOutputAdapter();

    return createClient(configuration, inputAdapter, outputAdapter);
  }

  public static SULClient<Boolean[], InputAdapter<Boolean[]>, Boolean[], OutputAdapter<Boolean[]>>
      createBooleanArrayClient(SULClientConfiguration configuration) throws IOException {
    BooleanArrayInputAdapter inputAdapter = new BooleanArrayInputAdapter();
    BooleanArrayOutputAdapter outputAdapter = new BooleanArrayOutputAdapter();

    return createClient(configuration, inputAdapter, outputAdapter);
  }

  public static SULClient<
          Word<Boolean>, InputAdapter<Word<Boolean>>, Word<Boolean>, OutputAdapter<Word<Boolean>>>
      createBooleanWordClient(SULClientConfiguration configuration) throws IOException {
    BooleanWordInputAdapter inputAdapter = new BooleanWordInputAdapter();
    BooleanWordOutputAdapter outputAdapter = new BooleanWordOutputAdapter();

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
    logger.info(String.format("Starting experiment {%d}", count++));
    // Reset SUL
    char[] resetCode = BAjER.resetCode;
    out.write(resetCode);
    out.flush();
    logger.info(String.format("Sent: {%s}", charArrayToString(resetCode)));

    try {
      int response = in.read();
      if (response != 0) {
        logger.error(
            String.format("Response from reset was not valid. Expected {0}, got {%d}", response));
        throw new InvalidResponseException(response, 0);
      }
      logger.info(String.format("SUL Reset, response from server: {%d}", response));

      // Send [2] Setup, number of inputs, number of outputs.
      char[] setupCode = BAjER.setup(numberofinputs, numberofoutputs);
      out.write(setupCode);
      out.flush();

      logger.info(String.format("Sent: {%s}", charArrayToString(setupCode)));

      response = in.read();

      logger.info(String.format("SUL Setup, response from server: {%d}", response));
      if (response != 0) {
        logger.error(
            String.format("Response from setup was not valid. Expected {0}, got {%d}", response));
        throw new InvalidResponseException(response, 0);
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void post() {}

  private String byteArrayToString(byte... bytes) {
    StringBuilder result = new StringBuilder("[");
    for (int i = 0; i < bytes.length - 1; i++) {
      result.append(bytes[i]);
      result.append(", ");
    }
    result.append(bytes[bytes.length - 1]);
    return result + "]";
  }

  private String charArrayToString(char[] chars) {
    StringBuilder result = new StringBuilder("[");
    for (int i = 0; i < chars.length - 1; i++) {
      result.append((int) chars[i]);
      result.append(", ");
    }
    result.append((int) chars[chars.length - 1]);
    return result + "]";
  }

  @Override
  public O step(I input) {
    // Send input.
    char[] stepCode = BAjER.step(inputAdapter.toBytes(input));
    out.print(stepCode);
    out.flush();

    logger.info(String.format("Sent: {%s}", charArrayToString(stepCode)));

    // Return output.
    try {
      byte[] bytes = new byte[numberofoutputs];
      int code = in.read();
      if (code != 0) {
        logger.error(
            String.format("Response from server was not valid. Expected {0}, got {%d}", code));
        throw new InvalidResponseException(code, 0);
      }

      byte[] logbytes = new byte[numberofoutputs + 1];
      logbytes[0] = (byte) code;
      for (int i = 0; i < bytes.length; i++) {
        int value = in.read(); // Maybe check that the stream has not ended?
        bytes[i] = (byte) value;

        logbytes[i + 1] = (byte) value;
      }
      logger.info(
          String.format("SUL Step, response from server: {%s}", byteArrayToString(logbytes)));

      return outputAdapter.fromBytes(bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
