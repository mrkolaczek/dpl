package com.kolaczek.winewednesday;

import com.kolaczek.winewednesday.lexer.Lexer;
import com.kolaczek.winewednesday.lexer.Lexeme;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String input = " if \"test\" 0 + 8 - / >= <= 9998766";
        Lexer l = new Lexer(input);
        List<Lexeme> lexList = l.getLexes();

        for (Lexeme lex: lexList) {
            System.out.println(lex.toString());
        }
    }
}
