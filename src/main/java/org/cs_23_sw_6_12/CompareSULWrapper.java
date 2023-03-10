package org.cs_23_sw_6_12;

import de.learnlib.api.SUL;
import org.cs_23_sw_6_12.Interfaces.Logger;

import java.util.Objects;

public class CompareSULWrapper<I, O> implements SUL<I,O> {

    private SUL<I,O> sul1;
    private SUL sul2;
    private Logger logger;

    public CompareSULWrapper(SUL<I,O> sul1, SUL sul2){
        logger = new StreamLogger(System.out);
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

        if (Objects.equals(output1.toString(), output2.toString())){
            logger.log(LogEntrySeverity.INFO, "{%s} is equal to {%s}", output1,output2);
            return output1;
        }else{
            logger.log(LogEntrySeverity.ERROR, "{%s} was not equal to {%s}", output1, output2);
            throw new RuntimeException("Outputs were not equal");
        }
    }
}
