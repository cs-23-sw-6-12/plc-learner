package org.cs23sw612.commands;

import picocli.CommandLine;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "plc-learner", mixinStandardHelpOptions = true, version = "0.1.0", description = "Utility to learn a PLC")
public class PlcLearnerCommand {
    @Spec CommandSpec spec;
}
