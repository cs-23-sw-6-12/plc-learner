package org.cs23sw612;

import java.io.IOException;

import net.automatalib.words.Word;
import org.cs23sw612.Util.LearnerFactoryRepository;
import org.cs23sw612.commands.LearnCommand;
import org.cs23sw612.commands.ListLearnersCommand;
import org.cs23sw612.commands.PlcLearnerCommand;
import org.cs23sw612.commands.VisualizeCommand;
import picocli.CommandLine;

public class Main {
    public static void main(String[] args) throws IOException {
        var learnerRepository = new LearnerFactoryRepository<Word<Integer>, Word<Integer>>();
        learnerRepository.addDefaultFactories();

        int exitCode = new CommandLine(new PlcLearnerCommand()).addSubcommand(new LearnCommand(learnerRepository))
                .addSubcommand(new ListLearnersCommand(learnerRepository)).addSubcommand(new VisualizeCommand())
                .execute(args);
        System.exit(exitCode);
    }
}
