package org.cs23sw612;

import de.learnlib.api.exception.SULException;

import org.cs23sw612.Adapters.InputAdapter;
import org.cs23sw612.Adapters.OutputAdapter;
import org.cs23sw612.BAjER.IBAjERClient;
import org.cs23sw612.Interfaces.SULTimed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SULClient<I, IA extends InputAdapter<I>, O, OA extends OutputAdapter<O>> implements SULTimed<I, O> {
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
    public O step(I input, long stepClockLimit) throws SULException {
        return null;
    }

    @Override
    public long getClockLimit() {
        return 0;
    }

    @Override
    public void pre() {
        logger.info(String.format("Starting experiment %d", experimentCount++));

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
            var output = bajerClient.Step(inputAdapter.getBits(input));
            return outputAdapter.fromBits(output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
