package org.cs23sw612.BAjER;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BAjERClient implements IBAjERClient {
    private int inBitsCount;
    private int outBitsCount;
    private Socket socket;
    private final Logger logger = LoggerFactory.getLogger(BAjERClient.class);

    public BAjERClient() {
        inBitsCount = 0;
        outBitsCount = 0;
        socket = null;
    }

    public void Connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }

    @Override
    public Boolean[] Step(Boolean[] bits) throws IOException, BAjERException {
        if (bits.length != inBitsCount) {
            throw new BAjERException("Step received too many bits");
        }
        logger.trace(String.format("Bajer: sending step %s",
                Arrays.stream(bits).map(b -> b ? "0" : "1").collect(Collectors.joining())));

        socket.getOutputStream().write(streamToByteArray(
                Stream.concat(Stream.of((Byte) (byte) 0), Arrays.stream(bits).map(b -> (Byte) (byte) (b ? 1 : 0)))));
        socket.getOutputStream().flush();
        if (socket.getInputStream().readNBytes(1)[0] != 0)
            throw new BAjERException("Expected to get a 0 in return for step command");

        var received = Arrays.stream(byteArrayToByteArray(socket.getInputStream().readNBytes(outBitsCount)))
                .map(b -> (Boolean) (b != 0)).toList().toArray(new Boolean[]{});

        logger.trace(String.format("Bajer: received outputs %s", Arrays.stream(received).map(b -> b ? "1" : "0").collect(Collectors.joining())));
        return received;
    }

    @Override
    public void Setup(byte inBits, byte outBits) throws IOException, BAjERException {
        if (inBits != inBitsCount && outBits != outBitsCount) {
            logger.trace(String.format("Bajer: sending setup, input count: %b, output count: %b", inBits, outBits));

            inBitsCount = inBits;
            outBitsCount = outBits;

            socket.getOutputStream().write(new byte[]{2, inBits, outBits});
            socket.getOutputStream().flush();

            if (socket.getInputStream().readNBytes(1)[0] != 0)
                throw new BAjERException("Expected to get a 0 in return for reset command");

            logger.trace("Bajer: setup done");
        }
    }

    @Override
    public void Reset() throws IOException, BAjERException {
        logger.trace("BAjER: sending reset");

        socket.getOutputStream().write(new byte[]{1});
        socket.getOutputStream().flush();

        if (socket.getInputStream().readNBytes(1)[0] != 0)
            throw new BAjERException("Expected to get a 0 in return for reset command");

        logger.trace("BAjER: reset done");
    }

    private byte[] streamToByteArray(Stream<Byte> stream) {
        var bytes = stream.toList().toArray(new Byte[]{});
        var buf = new byte[bytes.length];
        for (var i = 0; i < bytes.length; i++) {
            buf[i] = bytes[i];
        }
        return buf;
    }

    private Byte[] byteArrayToByteArray(byte[] byteArray) {
        var ByteArray = new Byte[byteArray.length];
        for (var i = 0; i < byteArray.length; i++) {
            ByteArray[i] = byteArray[i];
        }
        return ByteArray;
    }
}
