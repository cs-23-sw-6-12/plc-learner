package org.cs_23_sw_6_12;

import de.learnlib.api.SUL;
import org.cs_23_sw_6_12.Adapters.InputAdapter;
import org.cs_23_sw_6_12.Adapters.OutputAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer<I, O> extends Thread{

    public SUL<I,O> sul;
    public InputAdapter inputAdapter;
    public OutputAdapter<I> outputAdapter;
    public int port;

    @Override
    public void run() {
        try {
            runTestServer();
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void runTestServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket = serverSocket.accept();

        var out = new PrintWriter(clientSocket.getOutputStream());
        var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        int value;
        int inputSize = -1;
        int outputSize = -1;
        while ((value = in.read()) != -1){
            switch (value) {
                case 0 -> { // Step
                    if (inputSize < 0 || outputSize < 0)
                        throw new IOException("Step was run before Pre");
                    var concreteInput = new byte[inputSize];
                    for (int i = 0; i < inputSize; i++) {
                        concreteInput[i] = (byte) in.read();
                    }
                    var abstractOutput = sul.step(outputAdapter.fromBytes(concreteInput));
                    var concreteOutput = inputAdapter.toBytes(abstractOutput);
                    out.write(BAjER.step(concreteOutput));
                    out.flush();
                }
                case 1 -> { // Reset
                    sul.pre();
                    out.write(0);
                    out.flush();
                }
                case 2 -> { // Setup
                    inputSize = in.read();
                    outputSize = in.read();
                    out.write(0);
                    out.flush();
                }
                default -> throw new IOException("Unexpected response from client");
            }
        }
    }
}
