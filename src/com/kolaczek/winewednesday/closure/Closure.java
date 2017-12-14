package com.kolaczek.winewednesday.closure;

import com.kolaczek.winewednesday.environment.Environment;
import com.kolaczek.winewednesday.lexer.Lexeme;
import com.kolaczek.winewednesday.printer.PrettyPrinter;

import java.util.ArrayList;

public class Closure {

    private Environment defEnv;
    private Lexeme tree;
    private ArrayList<Lexeme> params;

    public Closure(Environment def, ArrayList<Lexeme> params, Lexeme body) {

        this.defEnv = def;
        this.params = params;
        this.tree = body;
    }

    public Closure(Environment def, Lexeme paramTree, Lexeme body) {

        this.defEnv = def;
        this.params = createArrayList(paramTree);
        this.tree = body;
    }

    private ArrayList<Lexeme> createArrayList(Lexeme paramTree) {

        ArrayList<Lexeme> params = new ArrayList<>();
        Lexeme current = paramTree;

        while(current != null) {
            params.add(current.getLeft());
            current = current.getRight();
        }

        return params;
    }

    public Environment getDefEnv() {

        return this.defEnv;
    }

    public ArrayList<Lexeme> getParams() {

        return params;
    }

    public Lexeme getTree() {

        return tree;
    }

    public void displayCons() {

        System.out.println("Params: ");

        for (Lexeme p : this.params) {
            System.out.print(p.getValue() + " ");
        }

        System.out.println("Body: ");

        PrettyPrinter p = new PrettyPrinter(this.tree);
        p.displayStatements(this.tree);

        System.out.println("\n\n");
    }
}
