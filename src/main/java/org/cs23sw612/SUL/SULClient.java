package org.cs23sw612.SUL;

import de.learnlib.api.SUL;

import org.cs23sw612.Adapters.InputAdapter;
import org.cs23sw612.Adapters.OutputAdapter;
import org.cs23sw612.BAjER.IBAjERClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SULClient<I, IA extends InputAdapter<I>, O, OA extends OutputAdapter<O>> implements SUL<I, O> {
    private final Logger logger = LoggerFactory.getLogger(SULClient.class);
    private byte inputCount;
    private byte outputCount;
    private int experimentCount = 0;
    private IA inputAdapter;
    private OA outputAdapter;
    private IBAjERClient bajerClient;

    public SULClient(IBAjERClient bajerClient, IA inputAdapter, OA outputAdapter, byte inputCount, byte outputCount) {
        this.bajerClient = bajerClient;

        this.inputAdapter = inputAdapter;
        this.outputAdapter = outputAdapter;

        this.inputCount = inputCount;
        this.outputCount = outputCount;
    }

    @Override
    public void pre() {
        System.out.printf("PLC Experiment %d%n", experimentCount++);

        try {
            bajerClient.Reset();
            bajerClient.Setup(inputCount, outputCount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void post() {
    }
    @Override
    public O step(I input) {
        try {
            var bits = inputAdapter.getBits(input);
            var output = bajerClient.Step(bits);
            return outputAdapter.fromBits(output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
