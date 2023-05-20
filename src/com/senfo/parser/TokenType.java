package com.senfo.parser;

public enum TokenType {
    NUMBER,
    HEX_NUMBER,
    WORD,

    LEFT_PAREN, // (
    RIGHT_PAREN, // )

    STAR,
    SLASH,
    PLUS,
    MINUS,

    EOF,
}
