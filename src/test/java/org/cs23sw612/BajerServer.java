package org.cs23sw612;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class ServerThread extends Thread {
    int port;

    byte[] outbytes;

    // The inputBytes that will be sent if inbytes are received
    byte[] inbytes;

    public ServerThread(int port, byte[] inBytes, byte[] outBytes) {
        this.port = port;
        this.inbytes = inBytes;
        this.outbytes = outBytes;
    }

    public void run() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();

            var out = clientSocket.getOutputStream();
            var in = clientSocket.getInputStream();

            byte[] inputBytes = new byte[inbytes.length];
            for (int i = 0; i < inputBytes.length; i++) {
                int value = in.read();
                inputBytes[i] = (byte) value;
            }

            if (Arrays.equals(inputBytes, inbytes)) {
                byte[] bytes = new byte[inbytes.length + 1];
                for (var i = 0; i < inbytes.length; i++) {
                    bytes[i + 1] = (byte) inbytes[i];
                }
                out.write(bytes);
                out.flush();
            } else {
                serverSocket.close();
            }

            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}