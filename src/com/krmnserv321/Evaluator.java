package com.krmnserv321;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Evaluator {
    private Parser parser;

    private Environment environment;

    public Evaluator(@NotNull Environment environment) {
        setEnvironment(environment);
        parser = new Parser(environment.getTokenList());
    }

    public void setEnvironment(@NotNull Environment environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public double eval(@NotNull String exp) {
        parser.parse(exp);
        return eval();
    }

    private double eval() {
        double num = term();
        Token token = peekToken();
        if (token.getType() == TokenType.Comma) {
            next();
            if (token.getType() == TokenType.RightParen) {
                throw new EvalException("Unexpected comma");
            }
            return num;
        }

        while (true) {
            token = peekToken();

            switch (token.toString()) {
                case "+":
                    next();
                    num += term();
                    continue;
                case "-":
                    next();
                    num -= term();
                    continue;
            }
            break;
        }

        if (peekToken().getType() != TokenType.End) {
            if (peekToken().getType() == TokenType.RightParen) {
                int p1 = 0;
                int p2 = 0;
                for (char c : parser.getExp().toCharArray()) {
                    if (c == '(') {
                        p1++;
                    } else if (c == ')') {
                        p2++;
                    }
                }
                if (p1 != p2) {
                    assertToken(TokenType.Operator);
                }
            } else {
                assertToken(TokenType.Operator);
            }
        }
        return num;
    }

    private double term() {
        double num = omittedTerm();
        while (true) {
            Token token = peekToken();
            switch (token.toString()) {
                case "*":
                    next();
                    num *= omittedTerm();
                    continue;
                case "/":
                    next();
                    num /= omittedTerm();
                    continue;
            }
            break;
        }
        return num;
    }

    private double omittedTerm() {
        double num = factor();
        for (Token token = peekToken(); token.getType() == TokenType.Constant; token = peekToken()) {
            num *= constant();
        }
        return num;
    }

    private double factor() {
        Token token = peekToken();

        double factor;
        switch (token.getType()) {
            case Constant:
                factor = constant();
                break;
            case Function:
                factor = function();
                break;
            case LeftParen:
                next();
                factor = eval();
                assertToken(TokenType.RightParen);
                next();
                break;
            case Prefix:
                if (token.toString().equals("-")) {
                    next();
                    factor = -factor();
                } else {
                    next();
                    factor = factor();
                }
                break;
                default:
                    return number();
        }

        return factor;
    }

    private double number() {
        Token token = peekToken();
        assertToken(TokenType.Number);
        next();
        return Double.parseDouble(token.toString());
    }

    private double constant() {
        Token token = peekToken();
        Double constant = environment.getConstantMap().get(token.toString());
        if (constant == null) {
            throw new EvalException("Constant not found: " + token);
        }
        next();
        return constant;
    }

    private double function() {
        Token token = peekToken();
        CalcFunction function = environment.getFunctionMap().get(token.toString());
        if (function == null) {
            throw new EvalException("Function not found: " + token);
        }

        next();

        assertToken(TokenType.LeftParen);

        next();

        return function.calc(args());
    }

    private List<Double> args() {
        List<Double> args = new ArrayList<>();
        if (peekToken().getType() == TokenType.RightParen) {
            next();
            return args;
        }
        do {
            double arg = eval();
            args.add(arg);
        } while (peekToken().getType() != TokenType.RightParen);

        next();

        return args;
    }

    private void assertToken(TokenType type) {
        if (peekToken().getType() != type) {
            throw new EvalException("expected=" + type + " got=" + peekToken());
        }
    }

    private Token peekToken() {
        return parser.peekToken();
    }

    private void next() {
        parser.next();
    }
}
