package org.cs23sw612;

import net.automatalib.words.Word;
import org.cs23sw612.Util.LearnerFactoryRepository;
import org.cs23sw612.Util.OracleFactoryRepository;
import org.cs23sw612.commands.LearnCommand;
import org.cs23sw612.commands.ListLearnersCommand;
import org.cs23sw612.commands.PlcLearnerCommand;
import org.cs23sw612.commands.VisualizeCommand;
import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        var oracleRepository = new OracleFactoryRepository();
        oracleRepository.addDefaultFactories();
        var learnerRepository = new LearnerFactoryRepository<Word<Boolean>, Word<Boolean>>();
        learnerRepository.addDefaultFactories();

        int exitCode = new CommandLine(new PlcLearnerCommand())
                .addSubcommand(new LearnCommand(learnerRepository, oracleRepository))
                .addSubcommand(new ListLearnersCommand(learnerRepository)).addSubcommand(new VisualizeCommand())
                .execute(args);
        System.exit(exitCode);
    }
}
