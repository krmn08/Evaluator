package com.krmnserv321;

public class TokenParseException extends RuntimeException {
    private String message;
    private int pos;

    public TokenParseException(String message, int pos) {
        this.message = message;
        this.pos = pos;
    }

    @Override
    public String getMessage() {
        return message + "(Pos:" + pos + ")";
    }
}
