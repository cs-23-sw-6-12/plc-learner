package org.cs_23_sw_6_12;

import java.net.SocketAddress;

public record SULClientConfiguration(
    String address,
    int port
) {}