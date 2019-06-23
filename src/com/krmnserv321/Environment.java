package com.krmnserv321;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Environment {
    private Map<String, Double> constantMap = new HashMap<>();
    private Map<String, CalcFunction> functionMap = new HashMap<>();

    private List<Token> tokenList = new ArrayList<>();

    public void addConstant(@NotNull String name, double constant) {
        tokenList.add(new Token(TokenType.Constant, name));
        constantMap.put(name, constant);
    }

    public void addFunction(@NotNull String name, @NotNull CalcFunction function) {
        functionMap.put(name, function);
        tokenList.add(new Token(TokenType.Function, name));
    }

    public boolean remove(@NotNull String name) {
        boolean remove = false;
        Iterator<Token> iterator = tokenList.iterator();
        while (iterator.hasNext()) {
            Token token = iterator.next();
            if (token.toString().equals(name)) {
                if (token.getType() == TokenType.Constant) {
                    constantMap.remove(name);
                } else if (token.getType() == TokenType.Function) {
                    functionMap.remove(name);
                } else {
                    continue;
                }
                iterator.remove();
                remove = true;
                break;
            }
        }
        return remove;
    }

    public Map<String, CalcFunction> getFunctionMap() {
        return functionMap;
    }

    public Map<String, Double> getConstantMap() {
        return constantMap;
    }

    public List<Token> getTokenList() {
        return tokenList;
    }
}
