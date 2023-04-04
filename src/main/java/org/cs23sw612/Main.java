package org.cs23sw612;

import java.io.IOException;
import org.cs23sw612.commands.LearnCommand;
import picocli.CommandLine;

public class Main {
    public static void main(String[] args) throws IOException {
        int exitCode = new CommandLine(new LearnCommand())
                .execute(args);
        System.exit(exitCode);
    }
}
