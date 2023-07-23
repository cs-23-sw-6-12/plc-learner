package org.cs23sw612.Experiments;

import de.learnlib.api.SUL;
import de.learnlib.oracle.membership.SULOracle;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.cs23sw612.HashCacheStorage;
import org.cs23sw612.Interfaces.MealyLearnerFactory;
import org.cs23sw612.Interfaces.OracleFactory;
import org.cs23sw612.OracleConfig;
import org.cs23sw612.SUL.BitWordCacheSUL;
import org.cs23sw612.Util.Bit;
import org.cs23sw612.Util.LearnerFactoryRepository;
import org.cs23sw612.Util.OracleFactoryRepository;

import java.io.File;

public class ExperimentBuilder {
    SUL<Word<Bit>, Word<Bit>> sul;
    Alphabet<Word<Bit>> alphabet;
    OracleConfig EqOracleConfig;
    MealyLearnerFactory<Word<Bit>, Word<Bit>> learnerFactory;
    OracleFactory<Word<Bit>, Word<Bit>> oracleFactory;
    String dotOutputLocation;
    boolean visualizeMachine, visualizeLadder;

    public ExperimentBuilder(SUL<Word<Bit>, Word<Bit>> sul, Alphabet<Word<Bit>> alphabet) {
        this.sul = sul;
        this.alphabet = alphabet;
    }

    public BenchmarkExperimentBuilder intoBenchmark() {
        return new BenchmarkExperimentBuilder(this);
    }

    public ExperimentBuilder.EQOracleBuilder withOracle(OracleFactory<Word<Bit>, Word<Bit>> oracleFactory) {
        this.oracleFactory = oracleFactory;
        return new ExperimentBuilder.EQOracleBuilder();
    }

    public ExperimentBuilder.EQOracleBuilder withOracle(OracleFactoryRepository<Word<Bit>, Word<Bit>> oracleRepository,
            String oracleName) {
        return withOracle(oracleRepository.getOracleFactory(oracleName));
    }

    public ExperimentBuilder withLearner(MealyLearnerFactory<Word<Bit>, Word<Bit>> learnerFactory) {
        this.learnerFactory = learnerFactory;
        return this;
    }

    public ExperimentBuilder withLearner(LearnerFactoryRepository<Word<Bit>, Word<Bit>> learnerRepository,
            String learnerName) {
        return withLearner(learnerRepository.getLearnerFactory(learnerName));
    }

    public ExperimentBuilder withCache(String cachePath) {
        this.sul = new BitWordCacheSUL(new HashCacheStorage(new File(cachePath)), sul);
        return this;
    }

    public ExperimentBuilder outputDOT(String dotOutputLocation) {
        this.dotOutputLocation = dotOutputLocation;
        return this;
    }

    public ExperimentBuilder withMachineVisualization() {
        this.visualizeMachine = true;
        return this;
    }

    public ExperimentBuilder withLadderVisualization() {
        this.visualizeLadder = true;
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
                oracleFactory.createOracle(sul, EqOracleConfig), alphabet, dotOutputLocation, visualizeMachine,
                visualizeLadder);
    }
}
