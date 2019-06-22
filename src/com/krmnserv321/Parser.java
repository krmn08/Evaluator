package com.krmnserv321;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final Token END = new Token(TokenType.End, "End");

    private List<Token> tokenList;

    private List<Token> parsedExp = new ArrayList<>();
    private String exp = "";
    private int index = 0;

    public Parser(@NotNull List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public String getExp() {
        return exp;
    }

    public void parse(@NotNull String exp) {
        this.exp = exp.trim();
        parsedExp.clear();

        Token token = null;
        for (int cursor = 0;; cursor += token.length()) {
            if (cursor >= exp.length()) {
                parsedExp.add(END);
                break;
            }

            //ignore spaces
            while (exp.charAt(cursor) == ' ' || exp.charAt(cursor) == '　') {
                cursor++;
            }

            //read token from cursor
            loop:
            for (int i = 0; cursor + i < exp.length(); i++) {
                for (Token t : tokenList) {
                    String sub = exp.substring(cursor, cursor + i + 1);
                    if (t.toString().startsWith(sub)) {
                        if (t.toString().equals(sub)) {
                            token = t;
                        }
                        continue loop;
                    }
                }
                StringBuilder sb = new StringBuilder();
                for (int j = 0; cursor + j < exp.length() && (Character.isDigit(exp.charAt(cursor + j)) || exp.charAt(cursor + j) == '.'); j++) {
                    sb.append(exp.charAt(cursor + j));
                }
                if (sb.length() > 0) {
                    token = new Token(TokenType.Number, sb.toString());
                }
                break;
            }
            if (token == null) {
                throw new TokenParseException("Doesn't match any tokens: " + exp.charAt(cursor), cursor);
            }

            parsedExp.add(token);
        }

        index = 0;
    }

    public Token peekToken() {
        return parsedExp.get(index);
    }

    public void next() {
        index++;
    }
}
