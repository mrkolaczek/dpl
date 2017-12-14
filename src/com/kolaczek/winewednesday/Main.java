package com.kolaczek.winewednesday;

import com.kolaczek.winewednesday.lexer.Lexeme;
import com.kolaczek.winewednesday.parser.Parser;
import com.kolaczek.winewednesday.printer.PrettyPrinter;

public class Main {

    public static void main(String[] args) {
        String input = " func c ( a ) { array a ( 1 2 3 4 ) }";

        Parser p = new Parser(input);
        Lexeme root = p.runner();

        PrettyPrinter pp = new PrettyPrinter(root);
        //pp.displayParseTree(root, "R");
        pp.displayStatements(root);

        System.out.println("Help me...");
    }
}
