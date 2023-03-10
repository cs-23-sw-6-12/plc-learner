package org.cs_23_sw_6_12.Interfaces;
import org.cs_23_sw_6_12.LogEntrySeverity;
public interface Logger {
    void log(LogEntrySeverity severity, String format, Object ... args);

    void setLogFilter(LogEntrySeverity maximumSeverity);
}
