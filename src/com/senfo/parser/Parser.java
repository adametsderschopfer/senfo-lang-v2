package com.senfo.parser;

import com.senfo.parser.ast.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
    private static final Token EOF = new Token(TokenType.EOF, null);

    private final List<Token> tokens;
    private final int size;
    private int position;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        size = tokens.size();
    }

    public BlockStatement parse() {
        final BlockStatement result = new BlockStatement();

        while (!match(TokenType.EOF)) {
            result.add(statement());
        }

        return result;
    }

    private IStatement block() {
        final BlockStatement block = new BlockStatement();

        required(TokenType.LEFT_BRACE);
        while (!match(TokenType.RIGHT_BRACE)) {
            block.add(statement());
        }

        return block;
    }

    private IStatement statementOrBlock() {
        if (peek(0).getType() == TokenType.LEFT_BRACE) {
            return block();
        }

        return statement();
    }

    private IStatement statement() {
        if (match(TokenType.PRINT)) {
            return new PrintStatement(expression());
        }

        if (match(TokenType.IF)) {
            return ifElse();
        }

        if (match(TokenType.WHILE)) {
            return whileStatement();
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

    private IStatement ifElse() {
        final IExpression condition = expression();
        final IStatement ifStatement = statementOrBlock();
        final IStatement elseStatement;

        if (match(TokenType.ELSE)) {
            elseStatement = statementOrBlock();
        } else {
            elseStatement = null;
        }

        return new IfStatement(condition, ifStatement, elseStatement);
    }

    private IStatement whileStatement() {
        final IExpression condition = expression();
        final IStatement statement = statementOrBlock();

        return new WhileStatement(condition, statement);
    }

    private IExpression expression() {
        return logicalOr();
    }

    private IExpression logicalOr() {
        IExpression result = logicalAnd();

        while (true) {
            if (match(TokenType.BARBAR)) {
                new ConditionalExpression(ConditionalExpression.Operator.OR, result, logicalAnd());
                continue;
            }

            break;
        }

        return result;
    }

    private IExpression logicalAnd() {
        IExpression result = equality();

        while (true) {
            if (match(TokenType.AMPAMP)) {
                new ConditionalExpression(ConditionalExpression.Operator.AND, result, equality());
                continue;
            }

            break;
        }

        return result;
    }

    private IExpression equality() {
        IExpression result = conditional();

        if (match(TokenType.EQEQ)) {
            return new ConditionalExpression(ConditionalExpression.Operator.EQUALS, result, conditional());
        }

        if (match(TokenType.EXCLEQ)) {
            return new ConditionalExpression(ConditionalExpression.Operator.NOT_EQUALS, result, conditional());
        }

        return result;
    }

    private IExpression conditional() {
        IExpression expr = additive();

        while (true) {
            if (match(TokenType.LT)) {
                expr = new ConditionalExpression(ConditionalExpression.Operator.LT, expr, additive());
                continue;
            }

            if (match(TokenType.LTEQ)) {
                expr = new ConditionalExpression(ConditionalExpression.Operator.LTEQ, expr, additive());
                continue;
            }

            if (match(TokenType.GT)) {
                expr = new ConditionalExpression(ConditionalExpression.Operator.GT, expr, additive());
                continue;
            }

            if (match(TokenType.GTEQ)) {
                expr = new ConditionalExpression(ConditionalExpression.Operator.GTEQ, expr, additive());
                continue;
            }

            break;
        }

        return expr;
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
