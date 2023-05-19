package com.senfo.parser;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private static final String OPERATOR_CHARS = "+-*/()";
    private static final TokenType[] OPERATOR_TOKENS = {
            TokenType.PLUS,
            TokenType.MINUS,
            TokenType.SLASH,
            TokenType.STAR,
    };
    private final String input;
    private final int length;
    private final List<Token> tokens;
    private int position = 0;

    public Lexer(String input) {
        this.input = input;
        length = input.length();
        tokens = new ArrayList<Token>();
    }

    public List<Token> tokenize() {
        while (position < length) {
            final char current = peek(0);

            if (Character.isDigit(current)) {
                tokenizeNumber();
            } else if (OPERATOR_CHARS.indexOf(current) != -1) {
                tokenizeOperator();
            } else {
                next();
            }
        }

        return tokens;
    }

    private void tokenizeNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);

        while (Character.isDigit(current)) {
            buffer.append(current);
            current = next();
        }

        addToken(TokenType.NUMBER, buffer.toString());
    }

    private void tokenizeOperator() {
        final int pos = OPERATOR_CHARS.indexOf(peek(0));

        addToken(OPERATOR_TOKENS[pos]);
        next();
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, String text) {
        tokens.add(new Token(type, text));
    }

    private char next() {
        position++;
        return peek(0);
    }

    private char peek(int relativePosition) {
        final int pos = position + relativePosition;

        if (pos >= length) {
            return '\0';
        }

        return input.charAt(pos);
    }
}
