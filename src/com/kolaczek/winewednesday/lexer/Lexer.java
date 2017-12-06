package com.kolaczek.winewednesday.lexer;

import java.util.List;
import java.util.ArrayList;

public class Lexer {
    private String text;
    private int linePos;
    private int end;

    public Lexer(String source) {
        this.text = source;
        this.linePos = 0;
        this.end = source.length();
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
                return null;
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
                    return new Lexeme(Lexeme.Type.Plus, "+");
                case '-':
                    this.linePos++;

                    // Check if negative number follows
                    char c = this.text.charAt(linePos);

                    if (c >= '0' && c <= '9') {
                        String s = "-" + readInteger();
                        return new Lexeme(Lexeme.Type.IntegerType, s);
                    }

                    return new Lexeme(Lexeme.Type.Minus, "-");
                case '*':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.Multiply, "*");
                case '/':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.Divide, "/");
                case '<':
                    this.linePos++;

                    if (checkNextChar('=')) {
                        this.linePos++;
                        return new Lexeme(Lexeme.Type.LessThanEqual, "<=");
                    }

                    return new Lexeme(Lexeme.Type.LessThan, "<");
                case '>':
                    this.linePos++;

                    if (checkNextChar('=')) {
                        this.linePos++;
                        return new Lexeme(Lexeme.Type.GreaterThanEqual, ">=");
                    }

                    return new Lexeme(Lexeme.Type.GreaterThan, ">");
                case '=':
                    this.linePos++;

                    if (checkNextChar('=')) {
                        this.linePos++;
                        return new Lexeme(Lexeme.Type.EqualEqual, "==");
                    }

                    return new Lexeme(Lexeme.Type.Equal, "=");
                case '!':
                    this.linePos++;

                    if (checkNextChar('=')) {
                        this.linePos++;
                        return new Lexeme(Lexeme.Type.ExclamationEqual, "!=");
                    }
                    // Something Bad happened if this is called...
                    return null;
                case ',':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.Comma, ",");
                case '.':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.Dot, ".");
                case ':':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.Colon, ":");
                case ';':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.SemiColon,";");
                case '(':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.OpenParen, "(");
                case ')':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.CloseParen, ")");
                case '[':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.OpenBracket, "[");
                case ']':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.CloseBracket, "]");
                case '{':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.OpenCurly, "{");
                case '}':
                    this.linePos++;
                    return new Lexeme(Lexeme.Type.CloseCurly, "}");
                case '\\':
                    this.linePos++;
                    String comment = readComment();
                    return new Lexeme(Lexeme.Type.Comment, comment);
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
                    return new Lexeme(Lexeme.Type.IntegerType, s);
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

            if (c == '\\') {
                break;
            }

            s.append(c);
        }

        return s.toString();
    }

    private Lexeme createKeywordOrStringLexeme(String value) {
        switch (value) {
            case "if":
                return new Lexeme(Lexeme.Type.IfKeyword, "if");
            case "else":
                return new Lexeme(Lexeme.Type.ElseKeyword, "else");
            case "while":
                return new Lexeme(Lexeme.Type.WhileKeyword, "while");
            case "func":
                return new Lexeme(Lexeme.Type.FuncKeyword, "func");
            case "return":
                return new Lexeme(Lexeme.Type.ReturnKeyword, "return");
            case "var":
                String varName = readString();
                return new Lexeme(Lexeme.Type.VarKeyword, varName);
            default:
                return new Lexeme(Lexeme.Type.StringType, value);
        }
    }
}
