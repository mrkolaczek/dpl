package com.kolaczek.winewednesday.lexer;

public class Lexeme {

    private Type type;
    private String value;
    private int linePos;
    private Lexeme left;
    private Lexeme right;

    public Lexeme(Type type) {

        this.type = type;
    }

    public Lexeme(Type type, String value) {

        this.type = type;
        this.value = value;
    }

    public Lexeme(Type type, String value, int linePos) {

        this.type = type;
        this.value = value;
        this.linePos = linePos;
    }

    public Lexeme(Type type, int linePos) {

        this.type = type;
        this.linePos = linePos;
    }

    public Lexeme getLeft() {

        return this.left;
    }

    public Lexeme getRight() {

        return this.right;
    }

    public void setLeft(Lexeme value) {

        this.left = value;
    }

    public void setRight(Lexeme value) {

        this.right = value;
    }

    public boolean check(Type type) {

        return this.type.equals(type);
    }

    @Override
    public String toString() {

        return this.type.name() + " " + this.value;
    }

    public Type getType() {

        return this.type;
    }

    public int getLinePos() {

        return this.linePos;
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
        WhileKeyword,
        FuncKeyword,
        ReturnKeyword,
        ObjectKeyword,
        VarKeyword,
        AnonymousKeyword,
        ArrayKeyword,
        Comment,
        Glue,
        EOF,
        Statement,
        Expression,
        Unary,
        Binary,
        InitializerExpression,
        ParameterExpression,
        AnonymousExpression,
        ObjectExpression,
        VariableExpression,
        AnonymousCall,
        Block,
        Parameter,
        Var,
        Conditional
    }
}
