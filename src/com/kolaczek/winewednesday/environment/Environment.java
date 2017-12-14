package com.kolaczek.winewednesday.environment;

import com.kolaczek.winewednesday.lexer.Lexeme;
import com.kolaczek.winewednesday.closure.Closure;

import java.util.ArrayList;

public class Environment {

    private Environment parent;
    private ArrayList<Lexeme> variables;
    private ArrayList<Lexeme> values;

    public Environment(Environment p) {

        this.parent = p;
        this.variables = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    public Environment getParent() {

        return this.parent;
    }

    public Lexeme setEnv(Lexeme var, Lexeme val) {

        for (int i = 0; i < variables.size(); ++i) {
            if (variables.get(i).getValue().equals(var.getValue())) {
                values.set(i, val);
                return values.get(i);
            }
        }

        if (parent != null)
            return parent.setEnv(var, val);
        else {
            System.out.println(var.getValue() + " is not defined");
            System.exit(1);
        }

        return null;
    }

    public Lexeme lookupEnv(Lexeme var) {

        for(int i = 0; i < variables.size(); i++) {
            if(variables.get(i).getValue().equals(var.getValue()))
                return values.get(i);
        }

        if (parent != null)
            return parent.lookupEnv(var);
        else {
            System.out.println(var.getValue() + " is not defined");
            System.exit(1);
        }

        return null;
    }

    public void displayEnv() {

        for(int i = 0; i < variables.size(); i ++) {
            System.out.print(variables.get(i).getValue() + ": ");
            Lexeme key = values.get(i);

            if (key.check(Lexeme.Type.Var)) {
                Lexeme val = (Lexeme) values.get(i).getValue();

                if (val != null)
                    System.out.println(val.getValue());
                else
                    System.out.println();
            }
            else if (key.check(Lexeme.Type.Func)) {
                Closure c = (Closure) values.get(i).getValue();
                c.displayCons();
            }
        }
    }

    public Environment copyEnv() {

        Environment env = new Environment(null);

        for (int i = 0; i < variables.size(); ++ i) {
            Lexeme var = new Lexeme(Lexeme.Type.Var, variables.get(i).getValue());
            Lexeme val = values.get(i);

            if (val != null) {

                if (val.check(Lexeme.Type.Obj))
                    env.insertObj(var, ((Environment) val.getValue()), env);
                else if (val.check(Lexeme.Type.Func)) {
                    Closure c = (Closure) val.getValue();
                    env.insertFuncDef(var, c.getParams(), c.getTree());
                }
                else if (val.check(Lexeme.Type.Array))
                    insertArray(var, new ArrayList<>());
                else {
                    Lexeme l = new Lexeme(val.getType(), val.getValue());
                    insertVariable(var, l);
                }
            }
            else
                insertVariable(var, null);
        }

        return env;
    }

    public Lexeme nullLexeme() {
        Lexeme l = new Lexeme(Lexeme.Type.Null, null);
        return l;
    }

    public boolean hasBeenCreated() {

        for (Lexeme l: variables) {
            if (l.getValue().equals("create"));
                return true;
        }

        return false;
    }

    public Closure getCreated() {

        for (int i = 0; i < variables.size(); ++i) {
            if (variables.get(i).getValue().equals("create"));
                return (Closure)values.get(i).getValue();
        }

        return null;
    }

    public Lexeme insertVariable(Lexeme var, Lexeme val) {

        checkIfKeyword(var);
        checkIfDefined(var);

        variables.add(var);
        values.add(val);

        return val;
    }

    public Lexeme insertArray(Lexeme var, ArrayList<Lexeme> vals) {

        checkIfKeyword(var);
        checkIfDefined(var);

        variables.add(var);
        Lexeme l = new Lexeme(Lexeme.Type.ArrayKeyword, vals);

        values.add(l);

        return l;
    }

    public Lexeme insertObj(Lexeme var, Environment env, Environment parent) {

        checkIfKeyword(var);
        checkIfDefined(var);

        variables.add(var);

        Lexeme l = null;
        if (env == null)
            l = new Lexeme(Lexeme.Type.Null, null);
        else
            l = new Lexeme(Lexeme.Type.Obj, env);

        values.add(l);

        return l;
    }

    public Lexeme insertFuncDef(Lexeme var, Lexeme paramTree, Lexeme body) {

        checkIfKeyword(var);
        checkIfDefined(var);

        // TODO: Builtin check

        variables.add(var);
        Closure c = new Closure(this, paramTree, body.getLeft());
        Lexeme l = new Lexeme(Lexeme.Type.Func, c);

        values.add(l);

        return l;
    }

    public Lexeme insertFuncDef(Lexeme var, ArrayList<Lexeme> params, Lexeme body) {

        checkIfKeyword(var);
        checkIfDefined(var);

        // TODO: Builtin check

        variables.add(var);
        Closure c = new Closure(this, params, body.getLeft());
        Lexeme l = new Lexeme(Lexeme.Type.Func, c);

        values.add(l);

        return l;
    }

    private boolean checkIfKeyword(Lexeme l) {

        String value = (String) l.getValue();

        switch (value) {
            case "if":
            case "else":
            case "func":
            case "array":
            case "aFunc":
            case "while":
            case "obj":
                System.out.println("Error var: " + value + "can not be named a keyword");
                System.exit(1);
        }

        return false;
    }

    private boolean checkIfDefined(Lexeme l) {

        for (Lexeme t: this.variables) {
            if (l.getValue().equals(t.getValue())) {
                System.out.println("Error var: " + l.getValue() + "is already defined in this scope");
                System.exit(1);
            }
        }

        return false;
    }
}
