package org.cs23sw612;

import net.automatalib.words.Word;
import org.cs23sw612.Util.Bit;
import org.cs23sw612.Util.LearnerFactoryRepository;
import org.cs23sw612.Util.OracleFactoryRepository;
import org.cs23sw612.commands.*;
import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        var oracleRepository = new OracleFactoryRepository();
        oracleRepository.addDefaultFactories();
        var learnerRepository = new LearnerFactoryRepository<Word<Bit>, Word<Bit>>();
        learnerRepository.addDefaultFactories();

        int exitCode = new CommandLine(new PlcLearnerCommand())
                .addSubcommand(new LearnCommand(learnerRepository, oracleRepository))
                .addSubcommand(new ListOraclesCommand(oracleRepository))
                .addSubcommand(new ListLearnersCommand(learnerRepository)).addSubcommand(new VisualizeCommand())
                .execute(args);
        System.exit(exitCode);
    }
}
