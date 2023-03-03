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
    int bytesToRead = 3;
    // The inputBytes that will be sent if inbytes are received
    byte[] outbytes = new byte[]{0,0,1};
    byte[] inbytes = new byte[]{0,1,0};

    public ServerThread(int port){
        this.port = port;
    }

    public void run(){
        ServerSocket ss = null;
        Socket s = null;
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            s = ss.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            var out = new PrintWriter(s.getOutputStream());
            var in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            byte[] inputBytes = new byte[bytesToRead];

            for (int i = 0; i < inputBytes.length; i++) {
                int value = in.read();
                inputBytes[i] = (byte) value;
            }


            if (Arrays.equals(inputBytes, inbytes)){

                for (int x : outbytes) {
                    out.write(x);
                }
                out.flush();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            ss.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
