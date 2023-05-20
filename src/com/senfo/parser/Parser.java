package com.senfo.parser;

import com.senfo.parser.ast.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final Token EOF = new Token(TokenType.EOF, null);

    private final List<Token> tokens;
    private final int size;
    private int position;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        size = tokens.size();
    }

    public List<IExpressionNode> parse() {
        final List<IExpressionNode> result = new ArrayList<>();

        while (!match(TokenType.EOF)) {
            result.add(expression());
        }

        return result;
    }

    private IExpressionNode expression() {
        return additive();
    }

    private IExpressionNode additive() {
        IExpressionNode expr = multiplicative();

        while (true) {
            if (match(TokenType.PLUS)) {
                expr = new BinaryExpressionNode('+', expr, multiplicative());
                continue;
            }

            if (match(TokenType.MINUS)) {
                expr = new BinaryExpressionNode('-', expr, multiplicative());
                continue;
            }

            break;
        }

        return expr;
    }

    private IExpressionNode multiplicative() {
        IExpressionNode expr = unary();

        while (true) {
            if (match(TokenType.STAR)) {
                expr = new BinaryExpressionNode('*', expr, unary());
                continue;
            }

            if (match(TokenType.SLASH)) {
                expr = new BinaryExpressionNode('/', expr, unary());
                continue;
            }

            break;
        }

        return expr;
    }

    private IExpressionNode unary() {
        if (match(TokenType.MINUS)) {
            return new UnaryExpressionNode('-', primary());
        }

        return primary();
    }

    private IExpressionNode primary() {
        final Token token = peek(0);

        if (match(TokenType.NUMBER)) {
            return new NumberExpressionNode(Double.parseDouble(token.getText()));
        }

        if (match(TokenType.HEX_NUMBER)) {
            return new NumberExpressionNode(Long.parseLong(token.getText(), 16));
        }

        if (match(TokenType.WORD)) {
            return new VariableExpressionNode(token.getText());
        }

        if (match(TokenType.LEFT_PAREN)) {
            IExpressionNode result = expression();
            match(TokenType.RIGHT_PAREN);

            return result;
        }

        throw new RuntimeException("Unknown expression - " + token.getType());
    }

    private boolean match(TokenType type) {
        final Token current = peek(0);

        if (type != current.getType()) {
            return false;
        }

        position++;
        return true;
    }

    private Token peek(int relativePosition) {
        final int pos = position + relativePosition;

        if (pos >= size) {
            return EOF;
        }

        return tokens.get(pos);
    }
}
