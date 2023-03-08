package org.cs_23_sw_6_12;

public enum LogEntrySeverity {
    DEBUG(0),
    INFO(1),
    NOTICE(2),
    WARNING(3),
    ERROR(4);

    public final int level;
    LogEntrySeverity(int level) {
        this.level = level;
    }
}
