package org.cs23sw612;

import de.learnlib.api.SUL;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompareSULWrapper<I, O> implements SUL<I, O> {

  private final SUL<I, O> sul1;
  private final SUL sul2;
  private final Logger logger = LoggerFactory.getLogger(CompareSULWrapper.class);

  public CompareSULWrapper(SUL<I, O> sul1, SUL sul2) {
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
    O output1 = sul1.step(i);
    Object output2 = sul2.step(i);

    if (Objects.equals(output1.toString(), output2.toString())) {
      logger.info(String.format("{%s} is equal to {%s}", output1, output2));
      return output1;
    } else {
      logger.info(String.format("{%s} was not equal to {%s}", output1, output2));
      throw new RuntimeException("Outputs were not equal");
    }
  }
}
