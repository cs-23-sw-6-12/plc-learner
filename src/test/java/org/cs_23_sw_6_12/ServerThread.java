package org.cs_23_sw_6_12;

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

    public ServerThread(int port, byte[] inBytes, byte[] outBytes){
        this.port = port;
        this.inbytes = inBytes;
        this.outbytes = outBytes;
    }

    public void run(){
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            byte[] inputBytes = new byte[inbytes.length];
            for (int i = 0; i < inputBytes.length; i++) {
                int value = in.read();
                inputBytes[i] = (byte) value;
            }

            if (Arrays.equals(inputBytes, inbytes)){
                for (int x : BAjER.step(outbytes)) {
                    out.write(x);
                }
                out.flush();
            }
            else{
                serverSocket.close();
            }

            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
