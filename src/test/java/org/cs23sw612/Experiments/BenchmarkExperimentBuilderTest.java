package org.cs23sw612.Experiments;

import org.cs23sw612.SUL.ExampleSUL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class BenchmarkExperimentBuilderTest {

    @Test
    public void allFieldsSetWhenConvertingToBenchmarkExperimentBuilder() {
        var sul = ExampleSUL.createExampleSUL();
        var alphabet = ExampleSUL.alphabet;
        var builder = new ExperimentBuilder<>(sul, alphabet);

        var bbuilder = new BenchmarkExperimentBuilder<>(sul, alphabet);
        Arrays.stream(bbuilder.getClass().getFields()).forEach(x -> {
            Assertions.assertDoesNotThrow(() -> {
                assert builder.getClass().getField(x.getName()).equals(x.get(bbuilder));
            });
        });

    }
}
