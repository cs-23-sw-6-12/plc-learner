package org.cs23sw612.Experiments;

import de.learnlib.api.SUL;
import de.learnlib.oracle.membership.SULOracle;
import net.automatalib.words.Alphabet;
import org.cs23sw612.Interfaces.MealyLearnerFactory;
import org.cs23sw612.Interfaces.OracleFactory;
import org.cs23sw612.OracleConfig;
import org.cs23sw612.Util.CacheFactoryRepository;
import org.cs23sw612.Util.LearnerFactoryRepository;
import org.cs23sw612.Util.OracleFactoryRepository;

public class ExperimentBuilder<I, O> {
    SUL<I, O> sul;
    Alphabet<I> alphabet;
    OracleConfig EqOracleConfig;
    MealyLearnerFactory<I, O> learnerFactory;
    OracleFactory<I, O> oracleFactory;
    String dotOutputLocation;
    boolean visualize;

    public ExperimentBuilder(SUL<I, O> sul, Alphabet<I> alphabet) {
        this.sul = sul;
        this.alphabet = alphabet;
    }

    public BenchmarkExperimentBuilder<I, O> intoBenchmark() {
        return new BenchmarkExperimentBuilder<>(this);
    }

    public ExperimentBuilder<I, O>.EQOracleBuilder withOracle(OracleFactory<I, O> oracleFactory) {
        this.oracleFactory = oracleFactory;
        return new ExperimentBuilder<I, O>.EQOracleBuilder();
    }

    public ExperimentBuilder<I, O>.EQOracleBuilder withOracle(OracleFactoryRepository<I, O> oracleRepository,
            String oracleName) {
        return withOracle(oracleRepository.getOracleFactory(oracleName));
    }

    public ExperimentBuilder<I, O> withLearner(MealyLearnerFactory<I, O> learnerFactory) {
        this.learnerFactory = learnerFactory;
        return this;
    }

    public ExperimentBuilder<I, O> withLearner(LearnerFactoryRepository<I, O> learnerRepository, String learnerName) {
        return withLearner(learnerRepository.getLearnerFactory(learnerName));
    }

    public ExperimentBuilder<I, O> withCache(String cachePath) {
        this.sul = CacheFactoryRepository.getCacheSULFactory().create(cachePath, sul);
        return this;
    }

    public ExperimentBuilder<I, O> outputDOT(String dotOutputLocation) {
        this.dotOutputLocation = dotOutputLocation;
        return this;
    }

    public ExperimentBuilder<I, O> withVisualization() {
        this.visualize = true;
        return this;
    }

    public class EQOracleBuilder {
        public ExperimentBuilder<I, O> withConfiguration(Integer maxSteps, Double restartProbability, Integer depth) {
            ExperimentBuilder.this.EqOracleConfig = new OracleConfig(maxSteps, restartProbability, depth);
            return ExperimentBuilder.this;
        }
    }

    public IPLCExperiment<I, O> build() {
        return new PLCExperimentImpl<>(learnerFactory.createLearner(alphabet, new SULOracle<>(sul)),
                oracleFactory.createOracle(sul, EqOracleConfig), alphabet, dotOutputLocation, visualize);
    }
}
