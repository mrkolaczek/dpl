package com.kolaczek.winewednesday.printer;

import com.kolaczek.winewednesday.lexer.Lexeme;

public class PrettyPrinter {

    private Lexeme parseTree;

    public PrettyPrinter(Lexeme parseTree) {

        this.parseTree = parseTree;
    }

    public void displayParseTree(Lexeme root, String leftOrRight) {

        System.out.println(leftOrRight + ": " + root.getType() + " : " + root.getValue());

        if (root.getLeft() != null)
            displayParseTree(root.getLeft(), "L");

        if (root.getRight() != null)
            displayParseTree(root.getRight(), "R");
    }

    public void displayStatements(Lexeme l) {

        if (l != null)
            displayStatement(l);
        if (l.getRight() != null)
            displayStatements(l.getRight());
    }

    private void displayStatement(Lexeme l) {


        if (l.getLeft().check(Lexeme.Type.Expression))
            displayExpression(l.getLeft());
        else if (l.getLeft().check(Lexeme.Type.VarKeyword))
            displayVariableDef(l.getLeft());
        else if (l.getLeft().check(Lexeme.Type.FuncKeyword))
            displayFunction(l.getLeft());
        else if (l.getLeft().check(Lexeme.Type.ArrayKeyword))
            displayArray(l.getLeft());
        else if (l.getLeft().check(Lexeme.Type.ObjectKeyword))
            displayObject(l.getLeft());
        else if (l.getLeft().check(Lexeme.Type.Conditional))
            displayConditional(l.getLeft());

        System.out.println("");
    }

    private void displayConditional(Lexeme l) {

        if (l.getLeft().check(Lexeme.Type.IfKeyword))
            displayIfStatement(l.getLeft());
        else if (l.getLeft().check(Lexeme.Type.WhileKeyword));
            displayWhileStatement(l.getLeft());
    }

    private void displayIfStatement(Lexeme l) {

        System.out.print("if (");
        displayExpression(l.getLeft());
        System.out.print(") ");
        displayBlock(l.getRight().getLeft());

        if (l.getRight().getRight() != null)
            displayElseStatement(l.getRight().getRight());
    }

    private void displayElseStatement(Lexeme l) {

        System.out.print("else ");

        if (l.getLeft().check(Lexeme.Type.IfKeyword))
            displayIfStatement(l.getLeft());
        else
            displayBlock(l.getLeft());
    }

    private void displayWhileStatement(Lexeme l) {

        System.out.print("while (");
        displayExpression(l.getLeft());
        System.out.print(") ");
        displayBlock(l.getRight());
    }

    private void displayObject(Lexeme l) {

        System.out.print("obj ");

        displayVariable(l.getLeft());
        displayInitializerExpression(l.getRight());
    }

    private void displayArray(Lexeme l) {

        System.out.print("array ");

        displayVariable(l.getLeft());
        displayInitializerExpression(l.getRight());
    }

    private void displayFunction(Lexeme l) {

        System.out.print("func ");

        displayVariable(l.getLeft());
        displayParameterExpression(l.getLeft().getLeft());
        displayBlock(l.getRight());

    }

    private void displayExpression(Lexeme l) {

        Lexeme v = l.getLeft();
        if (v != null) {
            displayUnary(v);

            if (l.getRight() != null) {
                System.out.print(" ");
                displayExpression(l.getRight());
            }
        }
    }

    private void displayUnary(Lexeme v) {

        Lexeme l = v.getLeft();
        if (l.check(Lexeme.Type.IntegerType))
            System.out.print(l.getValue());
        else if (l.check(Lexeme.Type.StringType))
            System.out.print("\"" + l.getValue() + "\"");
        else if (l.check(Lexeme.Type.ObjectExpression))
            displayObjectExpression(l.getLeft());
        else if (l.check(Lexeme.Type.AnonymousExpression))
            displayAnonymousExpression(l.getLeft());
        else
            displayVariableExpression(l);
    }

    private void displayBinary(Lexeme l) {


    }

    private void displayObjectExpression(Lexeme l) {

        displayVariable(l.getLeft());

        while (l.getLeft() != null) {
            System.out.print(".");
            displayVariable(l.getLeft());
            l = l.getLeft();
        }

        if (l.getRight() != null) {
            if (l.getRight().check(Lexeme.Type.ObjectExpression))
                displayObjectExpression(l.getRight());
            else if (l.getRight().check(Lexeme.Type.InitializerExpression))
                displayInitializerExpression(l.getRight());
            else if (l.getRight().check(Lexeme.Type.Equal)) {
                System.out.print(" = ");
                displayUnary(l.getRight().getLeft());
            }

        }
    }

    private void displayInitializerExpression(Lexeme l) {

        System.out.print("(");

        if (l.getLeft() != null)
            displayExpression(l.getLeft());

        System.out.print(")");
    }

    private void displayAnonymousExpression(Lexeme l) {

        if (l.getRight().check(Lexeme.Type.AnonymousCall)) {
            System.out.print("afunc ");
            displayParameterExpression(l.getLeft());
            displayBlock(l.getRight());
        }
        else {
            System.out.print("(");
            System.out.print("afunc");
            displayParameterExpression(l.getLeft());
            displayBlock(l.getRight());
            displayInitializerExpression(l.getRight().getRight());
            System.out.print(")");
        }
    }

    private void displayParameterExpression(Lexeme l) {

        System.out.print("(");

        if (l.getLeft() != null)
            displayParameters(l.getLeft());

        System.out.print(")");
    }

    private void displayParameters(Lexeme v) {

        Lexeme l = v.getLeft();
        if (l != null) {
            if (v.getRight() == null)
                System.out.print(l.getValue());
            else {
                System.out.print(l.getValue() + ", ");
                displayParameters(v.getRight());
            }
        }
    }

    private void displayBlock(Lexeme l) {

        System.out.print("{");

        if (l.getLeft() != null)
            displayStatements(l.getLeft());

        System.out.print("}");
    }

    private void displayVariableExpression(Lexeme l) {

        displayVariable(l);

        if (l.getLeft() != null) {
            if (l.getLeft().check(Lexeme.Type.Equal)) {
                System.out.print(" = ");
                displayUnary(l.getLeft().getLeft());
            }
            else
                displayInitializerExpression(l.getLeft());
        }
    }

    private void displayVariable(Lexeme l) {

        System.out.print(l.getValue());
    }

    private void displayVariableDef(Lexeme l) {

        Lexeme v = l.getLeft();

        System.out.print("var ");
        displayVariable(v);
        displayInitializerExpression(l.getRight());
    }
}
