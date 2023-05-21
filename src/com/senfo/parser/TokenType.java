package com.senfo.parser;

public enum TokenType {
    NUMBER,
    HEX_NUMBER,
    WORD,
    TEXT,

    //keyword
    PRINT,

    LEFT_PAREN, // (
    RIGHT_PAREN, // )

    STAR,
    SLASH,
    PLUS,
    MINUS,
    EQ,

    EOF,
}
