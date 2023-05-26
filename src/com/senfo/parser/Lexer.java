package com.senfo.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    private static final String OPERATOR_CHARS = "+-*/(){}=<>!&|,[]";

    private static final Map<String, TokenType> OPERATORS;

    static {
        OPERATORS = new HashMap<>();

        OPERATORS.put("+", TokenType.PLUS);
        OPERATORS.put("-", TokenType.MINUS);
        OPERATORS.put("*", TokenType.STAR);
        OPERATORS.put("/", TokenType.SLASH);
        OPERATORS.put("(", TokenType.LEFT_PAREN);
        OPERATORS.put(")", TokenType.RIGHT_PAREN);
        OPERATORS.put("{", TokenType.LEFT_BRACE);
        OPERATORS.put("}", TokenType.RIGHT_BRACE);
        OPERATORS.put("[", TokenType.LEFT_BRACKET);
        OPERATORS.put("]", TokenType.RIGHT_BRACKET);
        OPERATORS.put(",", TokenType.COMMA);
        OPERATORS.put("=", TokenType.EQ);
        OPERATORS.put("<", TokenType.LT);
        OPERATORS.put(">", TokenType.GT);

        OPERATORS.put("!", TokenType.EXCL);
        OPERATORS.put("&", TokenType.AMP);
        OPERATORS.put("|", TokenType.BAR);

        OPERATORS.put("==", TokenType.EQEQ);
        OPERATORS.put("!=", TokenType.EXCLEQ);
        OPERATORS.put("<=", TokenType.LTEQ);
        OPERATORS.put(">=", TokenType.GTEQ);

        OPERATORS.put("&&", TokenType.AMPAMP);
        OPERATORS.put("||", TokenType.BARBAR);
    }

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
            } else if (Character.isLetter(current)) {
                tokenizeWord();
            } else if (current == '"') {
                tokenizeText();
            } else if (current == '#') {
                next();
                tokenizeHexNumber();
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

        while (true) {
            if (current == '.') {
                if (buffer.indexOf(".") != -1) {
                    throw new RuntimeException("Invalid float number");
                }
            } else if (!Character.isDigit(current)) {
                break;
            }

            buffer.append(current);
            current = next();
        }

        addToken(TokenType.NUMBER, buffer.toString());
    }

    private void tokenizeHexNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);

        while (Character.isDigit(current) || isHexNumber(current)) {
            buffer.append(current);
            current = next();
        }

        addToken(TokenType.HEX_NUMBER, buffer.toString());
    }

    private static boolean isHexNumber(char current) {
        return ("abcdef".indexOf(Character.toLowerCase(current))) != -1;
    }

    private void tokenizeOperator() {
        char current = peek(0);
        if (current == '/') {
            if (peek(1) == '/') {
                next();
                next();
                tokenizeComment();

                return;
            } else if (peek(1) == '*') {
                next();
                next();
                tokenizeMultilineComment();

                return;
            }
        }

        final StringBuilder buffer = new StringBuilder();
        while (true) {
            final String text = buffer.toString();
            if (!OPERATORS.containsKey(text + current) && !text.isEmpty()) {
                addToken(OPERATORS.get(text));
                return;
            }

            buffer.append(current);
            current = next();
        }
    }

    private void tokenizeWord() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);

        while (true) {
            // todo to write regular expression instead of _ and $
            if (!Character.isLetterOrDigit(current) && (current != '_') && (current != '$')) {
                break;
            }

            buffer.append(current);
            current = next();
        }

        final String bufferString = buffer.toString();

        switch (bufferString) {
            case "print" -> addToken(TokenType.PRINT, bufferString);
            case "if" -> addToken(TokenType.IF);
            case "else" -> addToken(TokenType.ELSE);
            case "while" -> addToken(TokenType.WHILE);
            case "for" -> addToken(TokenType.FOR);
            case "do" -> addToken(TokenType.DO);
            case "continue" -> addToken(TokenType.CONTINUE);
            case "break" -> addToken(TokenType.BREAK);
            case "def" -> addToken(TokenType.DEF);
            case "return" -> addToken(TokenType.RETURN);
            default -> addToken(TokenType.WORD, bufferString);
        }
    }

    private void tokenizeText() {
        next(); // skip opening double quote
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);

        while (true) {
            if (current == '\\') {
                current = next();

                switch (current) {
                    case '"' -> {
                        current = next();
                        buffer.append('"');
                        continue;
                    }

                    case 'n' -> {
                        current = next();
                        buffer.append('\n');
                        continue;
                    }

                    case 't' -> {
                        current = next();
                        buffer.append('\t');
                        continue;
                    }
                }

                buffer.append('\\');
                continue;
            }

            if (current == '"') {
                break;
            }

            buffer.append(current);
            current = next();
        }

        next(); // skip closing double quote
        addToken(TokenType.TEXT, buffer.toString());
    }

    private void tokenizeComment() {
        char current = peek(0);
        while ("\r\n\0".indexOf(current) == -1) {
            current = next();
        }
    }

    private void tokenizeMultilineComment() {
        char current = peek(0);

        while (true) {
            if (current == '\0') {
                throw new RuntimeException("Missing close tag");
            }

            if (current == '*' && peek(1) == '/') {
                break;
            }

            current = next();
        }

        next(); // skip *
        next(); // skip /
    }

    private void addToken(TokenType type) {
        addToken(type, "");
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
