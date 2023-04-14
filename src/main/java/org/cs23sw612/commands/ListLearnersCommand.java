package org.cs23sw612.commands;

import net.automatalib.words.Word;
import org.cs23sw612.Util.LearnerFactoryRepository;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "list-learners", mixinStandardHelpOptions = true, version = "0.1.0", description = "Lists available learners")
public class ListLearnersCommand implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        var learnerRepo = new LearnerFactoryRepository<Word<Integer>, Word<Integer>>();
        learnerRepo.addDefaultFactories();

        System.err.println("Available learners:");

        learnerRepo.getLearnerNames().forEach(learnerName -> {
            System.err.format("- %s\n", learnerName);
        });
        return 0;
    }
}
