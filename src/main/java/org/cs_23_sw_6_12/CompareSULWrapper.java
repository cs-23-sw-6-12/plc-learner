package org.cs_23_sw_6_12;

import de.learnlib.api.SUL;
import de.learnlib.driver.util.MealySimulatorSUL;
import net.automatalib.words.Word;
import org.cs_23_sw_6_12.Adapters.InputAdapter;
import org.cs_23_sw_6_12.Adapters.OutputAdapter;

public class CompareSULWrapper<I, O> implements SUL<I,O> {

    private SUL<I,O> sul1;
    private SUL<I,Object> sul2;

    public CompareSULWrapper(SUL<I,O> sul1, SUL<I,Object> sul2){
        this.sul1 = sul1;
        this.sul2 = sul2;
    }
    @Override
    public void pre() {
        sul1.pre();
        sul2.pre();
    }

    @Override
    public void post() {
        sul1.post();
        sul2.post();
    }

    @Override
    public O step(I i) {
        var output1 = sul1.step(i);
        var output2 = sul2.step(i);

        if (output1 == output2){
            return output1;
        }else throw new RuntimeException("AAAAAH, NOT THE SAME");
    }
}
