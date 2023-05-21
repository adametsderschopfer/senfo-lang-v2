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

    public List<IStatement> parse() {
        final List<IStatement> result = new ArrayList<>();

        while (!match(TokenType.EOF)) {
            result.add(statement());
        }

        return result;
    }

    private IStatement statement() {
        if (match(TokenType.PRINT)) {
            return new PrintStatement(expression());
        }

        return assignmentStatement();
    }

    private IStatement assignmentStatement() {
        // WORD EQ
        final Token current = peek(0);

        if (match(TokenType.WORD) && peek(0).getType() == TokenType.EQ) {
            final String variable = current.getText();
            required(TokenType.EQ);

            return new AssignmentStatement(variable, expression());
        }

        throw new RuntimeException("Unknown statement");
    }

    private IExpression expression() {
        return additive();
    }

    private IExpression additive() {
        IExpression expr = multiplicative();

        while (true) {
            if (match(TokenType.PLUS)) {
                expr = new BinaryExpression('+', expr, multiplicative());
                continue;
            }

            if (match(TokenType.MINUS)) {
                expr = new BinaryExpression('-', expr, multiplicative());
                continue;
            }

            break;
        }

        return expr;
    }

    private IExpression multiplicative() {
        IExpression expr = unary();

        while (true) {
            if (match(TokenType.STAR)) {
                expr = new BinaryExpression('*', expr, unary());
                continue;
            }

            if (match(TokenType.SLASH)) {
                expr = new BinaryExpression('/', expr, unary());
                continue;
            }

            break;
        }

        return expr;
    }

    private IExpression unary() {
        if (match(TokenType.MINUS)) {
            return new UnaryExpression('-', primary());
        }

        return primary();
    }

    private IExpression primary() {
        final Token token = peek(0);

        if (match(TokenType.NUMBER)) {
            return new ValueExpression(Double.parseDouble(token.getText()));
        }

        if (match(TokenType.HEX_NUMBER)) {
            return new ValueExpression(Long.parseLong(token.getText(), 16));
        }

        if (match(TokenType.WORD)) {
            return new VariableExpression(token.getText());
        }

        if (match(TokenType.TEXT)) {
            return new ValueExpression(token.getText());
        }

        if (match(TokenType.LEFT_PAREN)) {
            IExpression result = expression();
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

    private void required(TokenType type) {
        final Token current = peek(0);

        if (type != current.getType()) {
            throw new RuntimeException("Token " + current.toString().trim() + " doesn't match " + type);
        }

        position++;
    }

    private Token peek(int relativePosition) {
        final int pos = position + relativePosition;

        if (pos >= size) {
            return EOF;
        }

        return tokens.get(pos);
    }
}
