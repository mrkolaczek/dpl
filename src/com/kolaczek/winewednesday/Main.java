package com.kolaczek.winewednesday;

import com.kolaczek.winewednesday.evaluator.Evaluator;
import com.kolaczek.winewednesday.lexer.Lexeme;
import com.kolaczek.winewednesday.parser.Parser;
import com.kolaczek.winewednesday.printer.PrettyPrinter;

public class Main {

    public static void main(String[] args) {
        String input = " array a ( 1 2 3 ) }";

        Parser p = new Parser(input);
        Lexeme root = p.runner();

        PrettyPrinter pp = new PrettyPrinter(root);
        pp.displayStatements(root);

        Evaluator e = new Evaluator(input);
        e.runner();

        System.out.println("Help me...");
    }
}
