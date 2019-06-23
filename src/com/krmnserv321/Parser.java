package com.krmnserv321;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final Token L_PAREN = new Token(TokenType.LeftParen, "(");
    private static final Token R_PAREN = new Token(TokenType.RightParen, ")");

    private static final Token COMMA = new Token(TokenType.Comma, ",");

    private static final Token PLUS = new Token(TokenType.Operator, "+");
    private static final Token MINUS = new Token(TokenType.Operator, "-");
    private static final Token MULTI = new Token(TokenType.Operator, "*");
    private static final Token DIVIDE = new Token(TokenType.Operator, "/");

    private static final Token POSITIVE = new Token(TokenType.Prefix, "+");
    private static final Token NEGATIVE = new Token(TokenType.Prefix, "-");

    private static final Token END = new Token(TokenType.End, "End");

    private static final Token[] STANDARD_TOKENS = {
            L_PAREN,
            R_PAREN,
            COMMA,
            PLUS,
            MINUS,
            MULTI,
            DIVIDE
    };

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
                String sub = exp.substring(cursor, cursor + i + 1);
                for (Token t : STANDARD_TOKENS) {
                    if (t.toString().startsWith(sub)) {
                        if (t.toString().equals(sub)) {
                            token = t;
                        }
                        continue loop;
                    }
                }
                for (Token t : tokenList) {
                    if (t.toString().startsWith(sub)) {
                        if (t.toString().equals(sub)) {
                            token = t;
                        }
                        continue loop;
                    }
                }
                StringBuilder sb = new StringBuilder();
                for (int j = 0; cursor + j < exp.length(); j++) {
                    char c = exp.charAt(cursor + j);
                    if (!Character.isDigit(c) && c != '.') {
                        if (c == '-' || c == '+') {
                            sb.append(c);
                        }
                        break;
                    }
                    sb.append(c);
                }
                if (sb.length() > 0) {
                    if (sb.charAt(0) == '-') {
                        token = parsePrefix(NEGATIVE, MINUS);
                    } else if (sb.charAt(0) == '+') {
                        token = parsePrefix(POSITIVE, PLUS);
                        break;
                    } else {
                        token = new Token(TokenType.Number, sb.toString());
                    }
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

    private Token parsePrefix(Token prefix, Token operator) {
        Token token;
        if (parsedExp.size() == 0) {
            token = operator;
            return token;
        }
        Token preToken = parsedExp.get(parsedExp.size() - 1);
        switch (preToken.getType()) {
            case LeftParen:
            case Comma:
            case Operator:
            case Prefix:
                token = prefix;
                break;
                default:
                    token = operator;
        }
        return token;
    }

    public Token peekToken() {
        return parsedExp.get(index);
    }

    public void next() {
        index++;
    }
}
