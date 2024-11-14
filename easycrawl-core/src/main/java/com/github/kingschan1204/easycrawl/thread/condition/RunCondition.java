package com.github.kingschan1204.easycrawl.thread.condition;

import java.util.function.Supplier;

/**
 * schedule task run condition
 * @author kingschan
 * 2024-11-14
 * @param <Boolean>
 */
public class RunCondition<Boolean> implements Supplier {

    private Supplier<Boolean> condition;

    public RunCondition(Supplier<Boolean> condition) {
        this.condition = condition;
    }

    @Override
    public Boolean get() {
        return this.condition.get();
    }
}
