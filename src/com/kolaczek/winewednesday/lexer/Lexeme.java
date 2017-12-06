package com.kolaczek.winewednesday.lexer;

public class Lexeme {
    public Type type;
    public String value;

    public Lexeme(Type type) {
        this.type = type;
    }

    public Lexeme(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return this.type.name() + " " + this.value;
    }

    public enum Type {
        IntegerType,
        StringType,
        OpenParen,
        CloseParen,
        OpenBracket,
        CloseBracket,
        OpenCurly,
        CloseCurly,
        Plus,
        Minus,
        Multiply,
        Divide,
        LessThan,
        GreaterThan,
        LessThanEqual,
        GreaterThanEqual,
        Equal,
        EqualEqual,
        ExclamationEqual,
        Comma,
        Dot,
        Colon,
        SemiColon,
        IfKeyword,
        ElseKeyword,
        ForKeyword,
        FuncKeyword,
        ReturnKeyword
    }
}
