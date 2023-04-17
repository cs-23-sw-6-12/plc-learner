package org.cs23sw612;

public class OracleConfig {
    public OracleConfig(int maxSteps, double restartProbability, int depth) {
        this.maxSteps = maxSteps;
        this.restartProbability = restartProbability;
        this.depth = depth;
    }
    public int maxSteps;
    public double restartProbability;
    public int depth;
}
