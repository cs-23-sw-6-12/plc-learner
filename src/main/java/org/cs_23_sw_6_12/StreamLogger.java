package org.cs_23_sw_6_12;

import org.cs_23_sw_6_12.Interfaces.Logger;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StreamLogger implements Logger {
    private LogEntrySeverity currentMaximumSeverity;
    private final PrintStream printStream;
    StreamLogger(PrintStream printStream) {
        this.currentMaximumSeverity = LogEntrySeverity.INFO;
        this.printStream = printStream;
    }

    @Override
    public void log(LogEntrySeverity severity, String format, Object... args) {
        if (severity.level < currentMaximumSeverity.level)
            return;

        String messageFormatted = String.format(format, args);
        printStream.printf("[%s]: %s\n", new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date()), messageFormatted);
        printStream.flush();
    }

    @Override
    public void setLogFilter(LogEntrySeverity maximumSeverity) {
        this.currentMaximumSeverity = maximumSeverity;
    }
}
