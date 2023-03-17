package org.cs23sw612;

import de.learnlib.api.SUL;

public class SULWrapper<I, O> implements SUL<I, O> {

    private final SUL<I, O> sul;
    private long counter = 0;

    public SULWrapper(SUL<I, O> sul) {
        this.sul = sul;
    }

    public long getCounter() {
        return counter;
    }

    @Override
    public void pre() {
        counter++;
        sul.pre();
    }

    @Override
    public void post() {
        sul.post();
    }

    @Override
    public O step(I i) {
        return sul.step(i);
    }
}
