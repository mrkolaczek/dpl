package com.kolaczek.winewednesday.lexer;

import java.util.List;
import java.util.ArrayList;

public class Lexer {

    private Lexeme currentLexeme;
    private String text;
    private int linePos;
    private int end;

    public Lexer(String source) {

        this.text = source;
        this.linePos = 0;
        this.end = source.length();
    }

    public void advance() {

        if (this.linePos == end)
            this.currentLexeme = new Lexeme(Lexeme.Type.EOF, "EOF");
        else
            this.currentLexeme = this.lex();
    }

    public Lexeme getCurrentLexeme() {

        return this.currentLexeme;
    }

    public List<Lexeme> getLexes() {

        List<Lexeme> lexemes = new ArrayList<>();

        Lexeme lexeme = this.lex();

        while(lexeme != null) {
            lexemes.add(lexeme);
            lexeme = this.lex();
        }

        return lexemes;
    }

    private Lexeme lex() {

        while (true) {

            if (this.linePos >= this.end) {
                return new Lexeme(Lexeme.Type.EOF, "EOF", linePos);
            }

            char ch = this.text.charAt(this.linePos);

            switch (ch) {
                case '\n':
                case '\t':
                case ' ':
                    this.linePos++;
                    continue;
                case '+':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.Plus, "+", linePos);
                case '-':
                    this.linePos++;

                    // Check if negative number follows
                    char c = this.text.charAt(linePos);

                    if (c >= '0' && c <= '9') {
                        String s = "-" + readInteger();
                        return new Lexeme(Lexeme.Type.IntegerType, s, linePos);
                    }

                    return new Lexeme(Lexeme.Type.Minus, "-", linePos);
                case '*':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.Multiply, "*", linePos);
                case '/':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.Divide, "/", linePos);
                case '<':
                    this.linePos++;

                    if (checkNextChar('=')) {
                        this.linePos++;
                        return new Lexeme(Lexeme.Type.LessThanEqual, "<=", linePos);
                    }

                    return new Lexeme(Lexeme.Type.LessThan, "<", linePos);
                case '>':
                    this.linePos++;

                    if (checkNextChar('=')) {
                        this.linePos++;
                        return new Lexeme(Lexeme.Type.GreaterThanEqual, ">=", linePos);
                    }

                    return new Lexeme(Lexeme.Type.GreaterThan, ">", linePos);
                case '=':
                    this.linePos++;

                    if (checkNextChar('=')) {
                        this.linePos++;
                        return new Lexeme(Lexeme.Type.EqualEqual, "==", linePos);
                    }

                    return new Lexeme(Lexeme.Type.Equal, "=", linePos);
                case '!':
                    this.linePos++;

                    if (checkNextChar('=')) {
                        this.linePos++;
                        return new Lexeme(Lexeme.Type.ExclamationEqual, "!=", linePos);
                    }
                    // Something Bad happened if this is called...
                    return null;
                case ',':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.Comma, ",", linePos);
                case '.':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.Dot, ".", linePos);
                case ':':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.Colon, ":", linePos);
                case ';':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.SemiColon,";", linePos);
                case '(':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.OpenParen, "(", linePos);
                case ')':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.CloseParen, ")", linePos);
                case '[':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.OpenBracket, "[", linePos);
                case ']':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.CloseBracket, "]", linePos);
                case '{':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.OpenCurly, "{", linePos);
                case '}':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.CloseCurly, "}", linePos);
                case '#':
                    this.linePos++;
                    String comment = readComment();
                    return new Lexeme(Lexeme.Type.Comment, comment, linePos);
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    String s = readInteger();
                    return new Lexeme(Lexeme.Type.IntegerType, s, linePos);
                case '\"':
                    String x = readString();
                    return new Lexeme(Lexeme.Type.StringType, x, linePos);
                default:
                    String a = readString();
                    return createKeywordOrStringLexeme(a);
            }

        }
    }

    private boolean checkNextChar(char match) {

        return this.linePos < this.end && this.text.charAt(this.linePos) == match;
    }

    private String readInteger() {

        StringBuilder i = new StringBuilder();

        while (this.linePos < this.end) {
            char c = this.text.charAt(this.linePos);
            this.linePos++;

            // Break out if ;
            if (c == ';') {
                this.linePos--;
                break;
            }

            if (c >= '0' && c <= '9') {
                i.append(c);
            }
            else {
                break;
            }
        }

        return i.toString();
    }

    private String readString() {

        StringBuilder i = new StringBuilder();

        while (this.linePos < this.end) {
            char c = this.text.charAt(this.linePos);
            this.linePos++;

            // Needed to remove leading " on string
            if (c == '\"') {
                c = this.text.charAt(this.linePos);
                this.linePos++;
            }

            // Break out if ;
            if (c == ';') {
                this.linePos--;
                break;
            }

            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                i.append(c);
            }
            else {
                break;
            }
        }

        return i.toString();
    }

    private String readComment() {

        StringBuilder s = new StringBuilder();

        while (this.linePos < this.end) {
            char c = this.text.charAt(this.linePos);
            this.linePos++;

            if (c == '#') {
                break;
            }

            s.append(c);
        }

        return s.toString();
    }

    private Lexeme createKeywordOrStringLexeme(String value) {

        switch (value) {
            case "if":
                return new Lexeme(Lexeme.Type.IfKeyword, "if", linePos);
            case "else":
                return new Lexeme(Lexeme.Type.ElseKeyword, "else", linePos);
            case "while":
                return new Lexeme(Lexeme.Type.WhileKeyword, "while", linePos);
            case "func":
                return new Lexeme(Lexeme.Type.FuncKeyword, "func", linePos);
            case "return":
                return new Lexeme(Lexeme.Type.ReturnKeyword, "return", linePos);
            case "obj":
                return new Lexeme(Lexeme.Type.ObjectKeyword, "obj", linePos);
            case "var":
                return new Lexeme(Lexeme.Type.VarKeyword, "var", linePos);
            case "array":
                return new Lexeme(Lexeme.Type.ArrayKeyword, "array", linePos);
            default:
                return new Lexeme(Lexeme.Type.Var, value, linePos);
        }
    }


    public boolean statementPending() {

        return expressionPending() || unaryPending() || commentPending() ||
                binaryPending() || operatorPending();
    }

    public boolean operatorPending() {

        return this.currentLexeme.check(Lexeme.Type.Plus)
                || this.currentLexeme.check(Lexeme.Type.Minus)
                || this.currentLexeme.check(Lexeme.Type.Multiply)
                || this.currentLexeme.check(Lexeme.Type.Divide)
                || this.currentLexeme.check(Lexeme.Type.LessThanEqual)
                || this.currentLexeme.check(Lexeme.Type.LessThan)
                || this.currentLexeme.check(Lexeme.Type.GreaterThan)
                || this.currentLexeme.check(Lexeme.Type.GreaterThanEqual)
                || this.currentLexeme.check(Lexeme.Type.Equal)
                || this.currentLexeme.check(Lexeme.Type.EqualEqual)
                || this.currentLexeme.check(Lexeme.Type.ExclamationEqual);
    }

    public boolean binaryPending() {

        return operatorPending();
    }

    public boolean unaryPending() {

        return this.currentLexeme.check(Lexeme.Type.IntegerType)
                || this.currentLexeme.check(Lexeme.Type.StringType)
                || this.currentLexeme.check(Lexeme.Type.Equal);
    }

    public boolean commentPending() {

        return this.currentLexeme.check(Lexeme.Type.Comment);
    }

    public boolean expressionPending() {

        return unaryPending() || binaryPending();
    }

    public boolean ifPending() {

        return this.currentLexeme.check(Lexeme.Type.IfKeyword);
    }

    public boolean whilePending() {

        return this.currentLexeme.check(Lexeme.Type.WhileKeyword);
    }

    public boolean conditionalPending() {

        return ifPending() || whilePending();
    }

    public boolean varDefPending() {

        return this.currentLexeme.check(Lexeme.Type.VarKeyword);
    }
}
