package com.krmnserv321;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ScriptException {
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
        environment.addFunction("sum", new CalcFunction() {
            @Override
            public double applyAsDouble(List<Double> args) {
                return args.stream().mapToDouble(Double::doubleValue).sum();
            }
        });
        environment.addConstant("x", 2);
        environment.addConstant("y", 8);

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        Evaluator evaluator = new Evaluator(environment);

        long t1 = System.currentTimeMillis();
        System.out.println(engine.eval("Math.pow(2, 8) * Math.sqrt(4) / (1 + 1 + 2) - (1 + 1 + 2)"));
        System.out.println(System.currentTimeMillis() - t1 + "ms");

        t1 = System.currentTimeMillis();
        System.out.println(evaluator.eval("pow(x, y) * sqrt(4) / (1 + 1 + 2) - sum(1, 1, 2)"));
        System.out.println(System.currentTimeMillis() - t1 + "ms");

        t1 = System.currentTimeMillis();
        System.out.println(Math.pow(2, 8) * Math.sqrt(4) / (1 + 1 + 2) - (1 + 1 + 2));
        System.out.println(System.currentTimeMillis() - t1 + "ms");
    }
}