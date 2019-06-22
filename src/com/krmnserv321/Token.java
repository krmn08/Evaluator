package com.krmnserv321;

import org.jetbrains.annotations.NotNull;

public class Token {
    private TokenType type;
    private String literal;

    public Token(@NotNull TokenType type, @NotNull String literal) {
        this.type = type;
        this.literal = literal;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return literal;
    }

    public int length() {
        return toString().length();
    }
}
