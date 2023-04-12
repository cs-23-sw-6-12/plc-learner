package SUL;

import de.learnlib.api.exception.SULException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

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
    private String currentInputString;

    private HashSet<String> triedCombinations;

    public SULClient(IBAjERClient bajerClient, IA inputAdapter, OA outputAdapter, byte inputCount, byte outputCount) {
        this.bajerClient = bajerClient;

        this.inputAdapter = inputAdapter;
        this.outputAdapter = outputAdapter;

        this.inputCount = inputCount;
        this.outputCount = outputCount;

        triedCombinations = new HashSet<String>();

        currentInputString = "";
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
        if (triedCombinations.contains(currentInputString)) {
            logger.info(String.format("Tried again %s", currentInputString));
        }
        else {
            triedCombinations.add(currentInputString);
        }
        currentInputString = "";
    }
    @Override
    public O step(I input) {
        try {
            var bits = inputAdapter.getBits(input);
            currentInputString += Arrays.stream(bits).map(b -> b ? "1" : "0").collect(Collectors.joining(""));
            var output = bajerClient.Step(bits);
            return outputAdapter.fromBits(output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
