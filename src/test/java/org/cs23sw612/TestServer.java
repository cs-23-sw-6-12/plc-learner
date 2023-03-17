package org.cs23sw612;

import de.learnlib.api.SUL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.cs23sw612.Adapters.InputAdapter;
import org.cs23sw612.Adapters.OutputAdapter;

public class TestServer<I, O> extends Thread {

    public SUL<I, O> sul;
    public InputAdapter inputAdapter;
    public OutputAdapter<I> outputAdapter;
    public int port;

    @Override
    public void run() {
        try {
            runTestServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void runTestServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket = serverSocket.accept();

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        int value;
        int inputSize = -1;
        int outputSize = -1;
        while ((value = in.read()) != -1) {
            switch (value) {
                case 0 -> { // Step
                    if (inputSize < 0 || outputSize < 0) throw new IOException("Step was run before Pre");
                    byte[] concreteInput = new byte[inputSize];
                    for (int i = 0; i < inputSize; i++) {
                        concreteInput[i] = (byte) in.read();
                    }
                    O abstractOutput = sul.step(outputAdapter.fromBytes(concreteInput));
                    byte[] concreteOutput = inputAdapter.toBytes(abstractOutput);
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
