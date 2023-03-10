package org.cs_23_sw_6_12;

import org.cs_23_sw_6_12.Interfaces.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Pattern;

public class StreamLoggerTest {
    @Test
    public void writeFilter() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Logger logger = new StreamLogger(new PrintStream(stream));

        logger.setLogFilter(LogEntrySeverity.DEBUG);
        logger.log(LogEntrySeverity.DEBUG,"bing bong");
        logger.log(LogEntrySeverity.INFO,"bing bong2");
        logger.log(LogEntrySeverity.NOTICE,"bing bong3");
        logger.log(LogEntrySeverity.WARNING,"bing bong4");
        logger.log(LogEntrySeverity.ERROR,"bing bong5");

        Pattern pattern = Pattern.compile("\\[[^\\]]*\\]: bing bong\n\\[[^\\]]*\\]: bing bong2\n\\[[^\\]]*\\]: bing bong3\n\\[[^\\]]*\\]: bing bong4\n\\[[^\\]]*\\]: bing bong5\n");
        Assertions.assertTrue(pattern.matcher(stream.toString()).matches(), "Should have all 5 messages");
    }

    @Test
    public void writeFilter_notice() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Logger logger = new StreamLogger(new PrintStream(stream));

        logger.setLogFilter(LogEntrySeverity.NOTICE);
        logger.log(LogEntrySeverity.DEBUG,"bing bong");
        logger.log(LogEntrySeverity.INFO,"bing bong2");
        logger.log(LogEntrySeverity.NOTICE,"bing bong3");
        logger.log(LogEntrySeverity.WARNING,"bing bong4");
        logger.log(LogEntrySeverity.ERROR,"bing bong5");

        Pattern pattern = Pattern.compile("\\[[^\\]]*\\]: bing bong3\n\\[[^\\]]*\\]: bing bong4\n\\[[^\\]]*\\]: bing bong5\n");

        Assertions.assertTrue(pattern.matcher(stream.toString()).matches(), "Should have all 3 messages");
    }

    @Test
    public void writeFilter_error() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Logger logger = new StreamLogger(new PrintStream(stream));

        logger.setLogFilter(LogEntrySeverity.ERROR);
        logger.log(LogEntrySeverity.DEBUG,"bing bong");
        logger.log(LogEntrySeverity.INFO,"bing bong2");
        logger.log(LogEntrySeverity.NOTICE,"bing bong3");
        logger.log(LogEntrySeverity.WARNING,"bing bong4");
        logger.log(LogEntrySeverity.ERROR,"bing bong5");

        Pattern pattern = Pattern.compile("\\[[^\\]]*\\]: bing bong5\n");

        Assertions.assertTrue(pattern.matcher(stream.toString()).matches(), "Should have all 1 messages");
    }
}
