package org.cs_23_sw_6_12;

import java.net.SocketAddress;

public class SULClientConfiguration {
    public final String address;
    public final int port;
    public SULClientConfiguration(String address, int port) {
        this.address = address;
        this.port = port;
    }
}
