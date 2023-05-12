package org.cs23sw612.commands;

import net.automatalib.words.Word;
import org.cs23sw612.Util.LearnerFactoryRepository;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "list-learners", mixinStandardHelpOptions = true, version = "0.1.0", description = "Lists available learners")
public class ListLearnersCommand implements Callable<Integer> {
    private final LearnerFactoryRepository<Word<Boolean>, Word<Boolean>> learnerRepository;

    public ListLearnersCommand(LearnerFactoryRepository<Word<Boolean>, Word<Boolean>> learnerRepository) {
        this.learnerRepository = learnerRepository;
    }

    @Override
    public Integer call() {
        System.err.println("Available learners:");

        learnerRepository.getLearnerNames().forEach(learnerName ->
            System.err.format("- %s\n", learnerName)
        );
        return 0;
    }
}
