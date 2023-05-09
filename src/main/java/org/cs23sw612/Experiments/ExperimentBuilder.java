package org.cs23sw612.Experiments;

import de.learnlib.api.SUL;
import de.learnlib.oracle.membership.SULOracle;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.cs23sw612.Interfaces.MealyLearnerFactory;
import org.cs23sw612.Interfaces.OracleFactory;
import org.cs23sw612.OracleConfig;
import org.cs23sw612.Util.LearnerFactoryRepository;
import org.cs23sw612.Util.OracleFactoryRepository;

public class ExperimentBuilder {
    SUL<Word<Boolean>, Word<Boolean>> sul;
    Alphabet<Word<Boolean>> alphabet;
    OracleConfig EqOracleConfig;
    MealyLearnerFactory<Word<Boolean>, Word<Boolean>> learnerFactory;
    OracleFactory<Word<Boolean>, Word<Boolean>> oracleFactory;
    String dotOutputLocation;
    boolean visualize;

    public ExperimentBuilder(SUL<Word<Boolean>, Word<Boolean>> sul, Alphabet<Word<Boolean>> alphabet) {
        this.sul = sul;
        this.alphabet = alphabet;
    }

    public BenchmarkExperimentBuilder intoBenchmark() {
        return new BenchmarkExperimentBuilder(this);
    }

    public ExperimentBuilder.EQOracleBuilder withOracle(OracleFactory<Word<Boolean>, Word<Boolean>> oracleFactory) {
        this.oracleFactory = oracleFactory;
        return new ExperimentBuilder.EQOracleBuilder();
    }

    public ExperimentBuilder.EQOracleBuilder withOracle(
            OracleFactoryRepository<Word<Boolean>, Word<Boolean>> oracleRepository, String oracleName) {
        return withOracle(oracleRepository.getOracleFactory(oracleName));
    }

    public ExperimentBuilder withLearner(MealyLearnerFactory<Word<Boolean>, Word<Boolean>> learnerFactory) {
        this.learnerFactory = learnerFactory;
        return this;
    }

    public ExperimentBuilder withLearner(LearnerFactoryRepository<Word<Boolean>, Word<Boolean>> learnerRepository,
            String learnerName) {
        return withLearner(learnerRepository.getLearnerFactory(learnerName));
    }

    public ExperimentBuilder withCache(String cachePath) {
        // this.sul = new IntegerWordCacheSUL(new HashCacheStorage(new File(cachePath)),
        // sul);
        return this;
    }

    public ExperimentBuilder outputDOT(String dotOutputLocation) {
        this.dotOutputLocation = dotOutputLocation;
        return this;
    }

    public ExperimentBuilder withVisualization() {
        this.visualize = true;
        return this;
    }

    public class EQOracleBuilder {
        public ExperimentBuilder withConfiguration(Integer maxSteps, Double restartProbability, Integer depth) {
            ExperimentBuilder.this.EqOracleConfig = new OracleConfig(maxSteps, restartProbability, depth);
            return ExperimentBuilder.this;
        }
    }

    public IPLCExperiment build() {
        return new PLCExperimentImpl(learnerFactory.createLearner(alphabet, new SULOracle<>(sul)),
                oracleFactory.createOracle(sul, EqOracleConfig), alphabet, dotOutputLocation, visualize);
    }
}
