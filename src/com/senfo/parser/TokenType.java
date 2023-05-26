package com.senfo.parser;

public enum TokenType {
    NUMBER,
    HEX_NUMBER,
    WORD,
    TEXT,

    //keyword
    PRINT,
    IF,
    ELSE,
    WHILE,
    FOR,
    DO,
    BREAK,
    CONTINUE,
    DEF,
    RETURN,

    LEFT_PAREN, // (
    RIGHT_PAREN, // )
    LEFT_BRACE, // {
    RIGHT_BRACE, // }
    LEFT_BRACKET, // [
    RIGHT_BRACKET, // ]
    COMMA, // ,

    STAR,
    SLASH,
    PLUS,
    MINUS,
    EQ,
    EQEQ,
    EXCL,
    EXCLEQ,
    LT,
    LTEQ,
    GT,
    GTEQ,

    BAR,
    BARBAR,
    AMP,
    AMPAMP,

    EOF,
}
