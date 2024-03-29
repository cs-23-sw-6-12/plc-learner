package org.cs23sw612.commands;

import org.cs23sw612.Util.OracleFactoryRepository;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "list-oracles", mixinStandardHelpOptions = true, version = "0.1.0", description = "Lists available oracles")
public class ListOraclesCommand implements Callable<Integer> {
    private final OracleFactoryRepository oracleRepository;

    public ListOraclesCommand(OracleFactoryRepository oracleRepository) {
        this.oracleRepository = oracleRepository;
    }

    @Override
    public Integer call() {
        System.err.println("Available oracles:");

        oracleRepository.getLearnerNames().forEach(oracleName -> System.err.format("- %s\n", oracleName));
        return 0;
    }

}
