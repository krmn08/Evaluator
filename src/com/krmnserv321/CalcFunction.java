package com.krmnserv321;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.ToDoubleFunction;

public abstract class CalcFunction implements ToDoubleFunction<List<Double>> {
    private int count;

    public CalcFunction() {
        count = -1;
    }

    public CalcFunction(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count is not positive");
        }
        this.count = count;
    }

    public double calc(@NotNull List<Double> args) {
        if (count != -1 && args.size() != count) {
            throw new IllegalArgumentException("Arguments doesn't match");
        }

        return applyAsDouble(args);
    }

    @Override
    public abstract double applyAsDouble(List<Double> args);
}
