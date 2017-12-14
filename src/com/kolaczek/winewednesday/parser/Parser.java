package com.kolaczek.winewednesday.parser;

import com.kolaczek.winewednesday.lexer.Lexeme;
import com.kolaczek.winewednesday.lexer.Lexer;

import java.util.ArrayList;

public class Parser {

    private Lexer lexer;
    private Lexeme root;

    public Parser(String source) {

        this.lexer = new Lexer(source);
        lexer.advance();
    }

    public Lexeme getStatement(Lexeme tree) {

        return tree.getLeft();
    }

    public Lexeme getParams(Lexeme tree) {

        return tree.getLeft().getLeft().getLeft();
    }

    public Lexeme getConditional(Lexeme tree) {

        return tree.getLeft();
    }

    public Lexeme getIfExpression(Lexeme tree) {

        return tree.getLeft().getLeft();
    }

    public boolean hasElse(Lexeme tree) {

        return tree.getRight().getRight() != null;
    }

    public boolean hasElseIf(Lexeme tree) {

        return tree.getRight().getRight().getLeft().check(Lexeme.Type.IfKeyword);
    }

    public Lexeme getElseIfExpression(Lexeme tree) {

        return tree.getRight().getRight().getLeft();
    }

    public Lexeme getElseExpression(Lexeme tree) {

        return tree.getRight().getRight().getLeft().getLeft();
    }

    public Lexeme getIfBlock(Lexeme tree) {

        return tree.getRight().getLeft().getLeft();
    }

    public Lexeme getIfOptElse(Lexeme tree) {

        return tree.getRight().getRight();
    }

    public Lexeme getWhileExprssion(Lexeme tree) {

        return tree.getLeft().getLeft();
    }

    public Lexeme getWhileBlock(Lexeme tree) {

        return tree.getRight().getLeft();
    }

    public Lexeme getFirstExpression(Lexeme tree) {

        return tree.getLeft().getLeft().getLeft();
    }

    public Lexeme getSecondExpression(Lexeme tree) {

        return tree.getLeft().getLeft().getRight().getLeft();
    }

    public Lexeme getVariable(Lexeme tree) {

        return tree.getLeft();
    }

    public Lexeme getBlock(Lexeme tree) {

        return tree.getRight();
    }

    public Lexeme getAnonymousParameters(Lexeme tree) {

        return tree.getLeft().getLeft();
    }

    public Lexeme getAnonymousBlock(Lexeme tree) {

        return tree.getRight().getLeft().getLeft();
    }

    public Lexeme getInitilizer(Lexeme tree) {

        if(tree.getRight().getLeft() == null)
            return null;
        else
            return tree.getRight().getLeft().getLeft();
    }

    public ArrayList<Lexeme> getArrayInitilizers(Lexeme tree) {

        ArrayList<Lexeme> lex = new ArrayList<Lexeme>();
        Lexeme l = tree.getRight().getLeft();

        while (l != null) {
            lex.add(l.getLeft());
            l = l.getRight();
        }

        return lex;
    }

    public ArrayList<Lexeme> getFunctionInitializers(Lexeme tree) {

        ArrayList<Lexeme> lex = new ArrayList<Lexeme>();
        Lexeme l = tree.getLeft().getLeft();

        while (l != null) {
            lex.add(l.getLeft());
            l = l.getRight();
        }

        return lex;
    }

    public ArrayList<Lexeme> getObjectExpressionInitializers(Lexeme tree) {

        ArrayList<Lexeme> lex = new ArrayList<Lexeme>();
        Lexeme l = tree.getRight().getLeft();

        while (l != null) {
            lex.add(l.getLeft());
            l = l.getRight();
        }

        return lex;
    }

    public ArrayList<Lexeme> getObjectDefineInitializers(Lexeme tree) {

        ArrayList<Lexeme> lex = new ArrayList<Lexeme>();
        Lexeme l = tree.getRight().getLeft().getLeft().getLeft().getLeft().getLeft();

        while (l != null) {
            lex.add(l.getLeft());
            l = l.getRight();

        }

        return lex;
    }

    public ArrayList<Lexeme> getAnonymousInitilizers(Lexeme tree) {

        ArrayList<Lexeme> lex = new ArrayList<Lexeme>();
        Lexeme l = tree.getRight().getRight().getLeft();

        while (l != null) {
            lex.add(l.getLeft());
            l = l.getRight();
        }

        return lex;
    }

    public Lexeme getUnary(Lexeme tree) {

        return tree.getLeft();
    }

    public Lexeme getBinary(Lexeme tree) {

        return tree.getRight();
    }

    public boolean anonymousHasCall(Lexeme tree) {

        return tree.getLeft().getRight().check(Lexeme.Type.AnonymousCall);
    }

    public boolean variableExpressionLookup(Lexeme tree) {

        return tree.getLeft() == null;
    }

    public Lexeme getVariableExpression(Lexeme tree) {

        return tree.getLeft();
    }

    public Lexeme getInitializer(Lexeme tree) {

        if (tree.getRight().getLeft() == null)
            return null;
        else
            return tree.getRight().getLeft().getLeft();
    }

    public Lexeme getObject(Lexeme tree) {

        return tree.getLeft();
    }

    public Lexeme getObjectAssignInitilizers(Lexeme tree) {

        return tree.getRight().getLeft();
    }

    private boolean check(Lexeme.Type type) {

        return lexer.getCurrentLexeme().check(type);
    }

    private Lexeme match(Lexeme.Type type) {

        if (!check(type)) {
            int linePos = lexer.getCurrentLexeme().getLinePos();

            System.out.println("Error at line position " + linePos);
            System.out.println("Bad match in Parser");
            System.out.println("Expected: " + type + " Given: " + lexer.getCurrentLexeme().getType().name());
            System.out.println("Value: " + lexer.getCurrentLexeme().getValue());
            System.exit(1);
        }

        Lexeme old = lexer.getCurrentLexeme();
        lexer.advance();

        return old;
    }

    public Lexeme runner() {

        Lexeme parseTree = statement();

        while (lexer.statementPending() && parseTree == null)
            parseTree = statement();

        if (lexer.statementPending())
            parseTree.setRight(runner());

        return parseTree;
    }

    private Lexeme statement() {

        Lexeme tree = null;

        if (check(Lexeme.Type.Comment))
            match(Lexeme.Type.Comment);
        else {
            tree = new Lexeme(Lexeme.Type.Statement);

            if (lexer.expressionPending())
                tree.setLeft(expression());
            else if (lexer.varDefPending())
                tree.setLeft(variableDef());
            else if (check(Lexeme.Type.FuncKeyword))
                tree.setLeft(functionDef());
            else if (check(Lexeme.Type.ArrayKeyword))
                tree.setLeft(arrayDef());
            else if (check(Lexeme.Type.ObjectKeyword))
                tree.setLeft(objectDef());
            else if (lexer.conditionalPending())
                tree.setLeft(conditional());
        }

        return tree;
    }

    private Lexeme expression() {

        Lexeme currentLexeme = this.lexer.getCurrentLexeme();
        Lexeme l = new Lexeme(Lexeme.Type.Expression, currentLexeme.getLinePos());

        if (this.lexer.unaryPending())
            l.setLeft(unary());
        else
            l.setLeft(binary());

        if (this.lexer.expressionPending())
            l.setRight(expression());

        return l;
    }

    private Lexeme unary() {

        Lexeme currentLexeme = this.lexer.getCurrentLexeme();
        Lexeme l = new Lexeme(Lexeme.Type.Unary, currentLexeme.getLinePos());

        if (check(Lexeme.Type.IntegerType))
            l.setLeft(match(Lexeme.Type.IntegerType));
        else if (check(Lexeme.Type.StringType))
            l.setLeft(match(Lexeme.Type.StringType));
        else if (lexer.anonymousPending())
            l.setLeft(anonymousExpression());
        else
            l.setLeft(variableExpression());

        return l;
    }

    private Lexeme binary() {

        Lexeme currentLexeme = this.lexer.getCurrentLexeme();
        Lexeme l = new Lexeme(Lexeme.Type.Binary, currentLexeme.getLinePos());
        l.setLeft(new Lexeme(Lexeme.Type.Glue, currentLexeme.getLinePos()));

        if (check(Lexeme.Type.Minus))
            l.getLeft().setLeft(match(Lexeme.Type.Minus));
        else if (check(Lexeme.Type.Plus))
            l.getLeft().setLeft(match(Lexeme.Type.Plus));
        else if (check(Lexeme.Type.Multiply))
            l.getLeft().setLeft(match(Lexeme.Type.Multiply));
        else if (check(Lexeme.Type.Divide))
            l.getLeft().setLeft(match(Lexeme.Type.Divide));
        else if (check(Lexeme.Type.GreaterThan))
            l.getLeft().setLeft(match(Lexeme.Type.GreaterThan));
        else if (check(Lexeme.Type.GreaterThanEqual))
            l.getLeft().setLeft(match(Lexeme.Type.GreaterThanEqual));
        else if (check(Lexeme.Type.LessThan))
            l.getLeft().setLeft(match(Lexeme.Type.LessThanEqual));
        else if (check(Lexeme.Type.EqualEqual))
            l.getLeft().setLeft(match(Lexeme.Type.EqualEqual));
        else if (check(Lexeme.Type.ExclamationEqual))
            l.getLeft().setLeft(match(Lexeme.Type.ExclamationEqual));

        return l;
    }

    private Lexeme initializerExpression() {

        Lexeme currentLexeme = this.lexer.getCurrentLexeme();
        Lexeme l = new Lexeme(Lexeme.Type.InitializerExpression, currentLexeme.getLinePos());

        match(Lexeme.Type.OpenParen);

        if (lexer.unaryPending())
            l.setLeft(expression());

        match(Lexeme.Type.CloseParen);

        return l;
    }

    private Lexeme parameterExpression() {

        Lexeme currentLexeme = this.lexer.getCurrentLexeme();
        Lexeme l = new Lexeme(Lexeme.Type.ParameterExpression, currentLexeme.getLinePos());

        match(Lexeme.Type.OpenParen);

        l.setLeft(optParameterList());

        match(Lexeme.Type.CloseParen);

        return l;
    }

    private Lexeme block() {

        Lexeme currentLexeme = this.lexer.getCurrentLexeme();
        Lexeme l = new Lexeme(Lexeme.Type.Block, currentLexeme.getLinePos());

        match(Lexeme.Type.OpenCurly);

        if (lexer.statementPending())
            l.setLeft(runner());

        match(Lexeme.Type.CloseCurly);

        return l;
    }

    private Lexeme whileLoop() {

        Lexeme l = match(Lexeme.Type.WhileKeyword);

        match(Lexeme.Type.OpenParen);
        l.setLeft(expression());
        match(Lexeme.Type.CloseParen);

        l.setRight(block());

        return l;
    }

    private Lexeme ifStatement() {

        Lexeme l = match(Lexeme.Type.IfKeyword);
        Lexeme glue = new Lexeme(Lexeme.Type.Glue);

        match(Lexeme.Type.OpenParen);
        l.setLeft(expression());
        match(Lexeme.Type.CloseParen);

        l.setRight(glue);
        l.getRight().setRight(block());
        l.getRight().setLeft(optElse());

        return l;
    }

    private Lexeme optElse() {

        Lexeme l = null;

        if (check(Lexeme.Type.ElseKeyword)) {
            l = match(Lexeme.Type.ElseKeyword);

            if (lexer.ifPending())
                l.setLeft(ifStatement());
            else
                l.setLeft(block());
        }

        return l;
    }

    private Lexeme optParameterList() {

        Lexeme l = null;

        if (check(Lexeme.Type.Var)) {
            Lexeme currentLexeme = this.lexer.getCurrentLexeme();
            l = new Lexeme(Lexeme.Type.Parameter, currentLexeme.getLinePos());

            l.setLeft(match(Lexeme.Type.Var));
            l.setRight(optParameterList());
        }

        return l;
    }

    private Lexeme anonymousExpression() {

        Lexeme currentLexeme = this.lexer.getCurrentLexeme();
        Lexeme l = new Lexeme(Lexeme.Type.AnonymousExpression, currentLexeme.getLinePos());

        if (check(Lexeme.Type.AnonymousKeyword))
            l.setLeft(anonymousDefine());
        else
            l.setLeft(anonymousCall());

        return l;
    }

    private Lexeme anonymousDefine() {

        Lexeme l = match(Lexeme.Type.AnonymousKeyword);

        l.setLeft(parameterExpression());
        l.setRight(block());

        return l;
    }

    private Lexeme anonymousCall() {

        match(Lexeme.Type.OpenParen);

        Lexeme l = match(Lexeme.Type.AnonymousKeyword);
        Lexeme currentLexeme = lexer.getCurrentLexeme();
        Lexeme anonymousCall = new Lexeme(Lexeme.Type.AnonymousCall, currentLexeme.getLinePos());

        l.setLeft(parameterExpression());
        l.setRight(anonymousCall);

        l.getRight().setLeft(block());
        l.getRight().setRight(initializerExpression());

        match(Lexeme.Type.CloseParen);

        return l;
    }

    private Lexeme variableDef() {

        Lexeme l = match(Lexeme.Type.VarKeyword);
        l.setLeft(match(Lexeme.Type.Var));
        l.setRight(initializerExpression());

        return l;
    }

    private Lexeme functionDef() {

        Lexeme l = match(Lexeme.Type.FuncKeyword);
        l.setLeft(match(Lexeme.Type.Var));
        l.getLeft().setLeft(parameterExpression());
        l.setRight(block());

        return l;
    }

    private Lexeme objectDef() {

        Lexeme l = match(Lexeme.Type.ObjectKeyword);
        l.setLeft(match(Lexeme.Type.Var));
        l.setRight(initializerExpression());

        return l;
    }

    private Lexeme arrayDef() {

        Lexeme l = match(Lexeme.Type.ArrayKeyword);
        l.setLeft(match(Lexeme.Type.Var));
        l.setRight(initializerExpression());

        return l;
    }

    private Lexeme variableExpression() {

        Lexeme var = match(Lexeme.Type.Var);
        Lexeme l = null;

        if (check(Lexeme.Type.OpenParen)) {
            l = var;
            l.setLeft(initializerExpression());
        }
        else if (check(Lexeme.Type.Dot)) {
            l = objectExpression(var);
        }
        else {
            l = var;
            l.setLeft(variableAssign());
        }

        return l;
    }

    private Lexeme objectExpression(Lexeme var) {

        Lexeme currentLexeme = lexer.getCurrentLexeme();
        Lexeme l = new Lexeme(Lexeme.Type.ObjectExpression, currentLexeme.getLinePos());

        l.setLeft(var);

        Lexeme t = l.getLeft();

        while (check(Lexeme.Type.Dot)) {
            match(Lexeme.Type.Dot);
            t.setLeft(match(Lexeme.Type.Var));
            t = t.getLeft();
        }

        if (check(Lexeme.Type.Equal))
            l.setRight(objectAssign());
        else if (check(Lexeme.Type.InitializerExpression))
            l.setRight(initializerExpression());

        return l;
    }

    private Lexeme objectAssign() {

        Lexeme l = match(Lexeme.Type.Equal);
        l.setLeft(unary());

        return l;
    }

    private Lexeme variableAssign() {

        Lexeme l = null;

        if (check(Lexeme.Type.Equal)) {
            l = match(Lexeme.Type.Equal);
            l.setLeft(unary());
        }

        return l;
    }

    private Lexeme conditional() {

        Lexeme currentLexeme = lexer.getCurrentLexeme();
        Lexeme l = new Lexeme(Lexeme.Type.Conditional, currentLexeme.getLinePos());

        if (lexer.ifPending())
            l.setLeft(ifStatement());
        else
            l.setLeft(whileLoop());

        return l;
    }
}
