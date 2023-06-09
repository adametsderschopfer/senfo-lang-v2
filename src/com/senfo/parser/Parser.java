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

        if (match(TokenType.DO)) {
            return doWhileStatement();
        }

        if (match(TokenType.FOR)) {
            return forStatement();
        }

        if (match(TokenType.BREAK)) {
            return new BreakStatement();
        }

        if (match(TokenType.CONTINUE)) {
            return new ContinueStatement();
        }

        if (match(TokenType.RETURN)) {
            return new ReturnStatement(expression());
        }

        if (match(TokenType.DEF)) {
            return functionDefine();
        }

        if (peek(0).getType() == TokenType.WORD && peek(1).getType() == TokenType.LEFT_PAREN) {
            return new FunctionStatement(function());
        }

        return assignmentStatement();
    }

    private IStatement assignmentStatement() {
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.EQ)) {
            final String variable = required(TokenType.WORD).getText();
            required(TokenType.EQ);
            return new AssignmentStatement(variable, expression());
        }

        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LEFT_BRACKET)) {
            final ArrayAccessExpression array = element();
            required(TokenType.EQ);
            return new ArrayAssignmentStatement(array, expression());
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

    private FunctionDefineStatement functionDefine() {
        final String name = required(TokenType.WORD).getText();
        required(TokenType.LEFT_PAREN);

        final List<String> argNames = new ArrayList<>();

        while (!match(TokenType.RIGHT_PAREN)) {
            argNames.add(required(TokenType.WORD).getText());
            match(TokenType.COMMA);
        }

        final IStatement body = statementOrBlock();

        return new FunctionDefineStatement(name, argNames, body);
    }

    private FunctionalExpression function() {
        final String name = required(TokenType.WORD).getText();
        required(TokenType.LEFT_PAREN);

        final FunctionalExpression function = new FunctionalExpression(name);

        while (!match(TokenType.RIGHT_PAREN)) {
            function.addArgument(expression());
            match(TokenType.COMMA);
        }

        return function;
    }

    private IStatement whileStatement() {
        final IExpression condition = expression();
        final IStatement statement = statementOrBlock();

        return new WhileStatement(condition, statement);
    }

    private IStatement doWhileStatement() {
        final IStatement statement = statementOrBlock();
        required(TokenType.WHILE);
        final IExpression condition = expression();

        return new DoWhileStatement(condition, statement);
    }

    private IStatement forStatement() {
        final IStatement initialization = assignmentStatement();
        required(TokenType.COMMA);

        final IExpression termination = expression();
        required(TokenType.COMMA);

        final IStatement increment = assignmentStatement();
        final IStatement statement = statementOrBlock();

        return new ForStatement(
                initialization,
                termination,
                increment,
                statement
        );
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

        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LEFT_PAREN)) {
            return function();
        }

        if (lookMatch(0, TokenType.LEFT_BRACKET)) {
            return array();
        }

        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LEFT_BRACKET)) {
            return element();
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

    private IExpression array() {
        required(TokenType.LEFT_BRACKET);

        final List<IExpression> elements = new ArrayList<>();
        while (!match(TokenType.RIGHT_BRACKET)) {
            elements.add(expression());
            match(TokenType.COMMA);
        }

        return new ArrayExpression(elements);
    }

    private ArrayAccessExpression element() {
        final String variable = required(TokenType.WORD).getText();
        final List<IExpression> indices = new ArrayList<>();

        do {
            required(TokenType.LEFT_BRACKET);
            indices.add(expression());
            required(TokenType.RIGHT_BRACKET);
        } while(lookMatch(0, TokenType.LEFT_BRACKET));

        return new ArrayAccessExpression(variable, indices);
    }


    private boolean match(TokenType type) {
        final Token current = peek(0);

        if (type != current.getType()) {
            return false;
        }

        position++;
        return true;
    }

    private Token required(TokenType type) {
        final Token current = peek(0);

        if (type != current.getType()) {
            throw new RuntimeException("Token " + current.toString().trim() + " doesn't match " + type);
        }

        position++;

        return current;
    }

    private boolean lookMatch(int position, TokenType type) {
        return peek(position).getType() == type;
    }

    private Token peek(int relativePosition) {
        final int pos = position + relativePosition;

        if (pos >= size) {
            return EOF;
        }

        return tokens.get(pos);
    }
}
