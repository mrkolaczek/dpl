package com.kolaczek.winewednesday;

import com.kolaczek.winewednesday.lexer.Lexeme;
import com.kolaczek.winewednesday.parser.Parser;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String input = " func a(a) { var 1 = 2 }";
        Parser p = new Parser(input);

        Lexeme root = p.runner();

        System.out.println("Help me...");
    }
}
