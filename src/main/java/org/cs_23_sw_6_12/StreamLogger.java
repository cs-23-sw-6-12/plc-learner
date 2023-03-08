package org.cs_23_sw_6_12;

import org.cs_23_sw_6_12.Interfaces.Logger;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StreamLogger implements Logger {
    private LogEntrySeverity currentMaximumSeverity;
    private PrintStream printStream;
    StreamLogger(PrintStream printStream) {
        this.currentMaximumSeverity = LogEntrySeverity.INFO;
        this.printStream = printStream;
    }

    @Override
    public void log(LogEntrySeverity severity, String format, String... args) {
        if (severity.level < currentMaximumSeverity.level)
            return;

        var messageFormatted = String.format(format, args);
        printStream.print(String.format("[%s]: %s\n", new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date()), messageFormatted));
        printStream.flush();
    }

    @Override
    public void setLogFilter(LogEntrySeverity maximumSeverity) {
        this.currentMaximumSeverity = maximumSeverity;
    }
}
