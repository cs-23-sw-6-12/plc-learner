package org.cs23sw612;

import java.io.IOException;
import java.util.logging.Logger;

import org.cs23sw612.commands.LearnCommand;
import org.cs23sw612.commands.ListLearnersCommand;
import org.cs23sw612.commands.PlcLearnerCommand;
import org.cs23sw612.commands.VisualizeCommand;
import picocli.CommandLine;


public class Main {
    public static void main(String[] args) throws IOException {

        int exitCode = new CommandLine(new PlcLearnerCommand()).addSubcommand(new LearnCommand())
                .addSubcommand(new ListLearnersCommand()).addSubcommand(new VisualizeCommand()).execute(args);
        System.exit(exitCode);
    }
}
