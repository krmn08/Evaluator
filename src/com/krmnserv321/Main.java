package com.krmnserv321;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Environment environment = new Environment();
        environment.addFunction("sqrt", new CalcFunction(1) {
            @Override
            public double applyAsDouble(List<Double> args) {
                return Math.sqrt(args.get(0));
            }
        });
        environment.addFunction("pow", new CalcFunction(2) {
            @Override
            public double applyAsDouble(List<Double> args) {
                return Math.pow(args.get(0), args.get(1));
            }
        });
        CalcFunction sum = new CalcFunction() {
            @Override
            public double applyAsDouble(List<Double> args) {
                if (args.size() == 0) {
                    return 0;
                }
                return args.stream().reduce((s, v) -> s += v).get();
            }
        };
        environment.addFunction("sum", sum);
        double x = 2;
        double y = 8;
        environment.addConstant("x", x);
        environment.addConstant("y", y);

        Evaluator evaluator = new Evaluator(environment);

        long t1 = System.currentTimeMillis();
        System.out.println(evaluator.eval("sqrt(pow(x,y) / sum(-(- 1 * 5 * -1),+1) * -1)-+-+2"));
        System.out.println(System.currentTimeMillis() - t1 + "ms");

        System.out.println(Math.sqrt(Math.pow(x, y) / sum.calc(Arrays.asList(-(- 1 * 5 * -1d),+1d)) * -1)-(+-+2));
    }
}