package com.kolaczek.winewednesday.evaluator;

import com.kolaczek.winewednesday.environment.Environment;
import com.kolaczek.winewednesday.lexer.Lexeme;
import com.kolaczek.winewednesday.parser.Parser;

import java.util.ArrayList;

public class Evaluator {

    private Environment thisEnv;
    private Lexeme parseTree;
    private Parser p;

    public Evaluator(String file) {

        thisEnv = new Environment(null);
        //thisEnv.addBuiltins();

        p = new Parser(file);
        parseTree = p.runner();
    }

    public void runner() {

        evalStatements(parseTree, thisEnv);
    }

    private Lexeme evalStatements(Lexeme tree, Environment env) {

        Lexeme current = tree;
        Lexeme returnVal = null;

        while(current != null) {
            returnVal = evalStatement(p.getStatement(current), env);
            current = current.getRight();
        }

        return returnVal;
    }

    private Lexeme evalStatement(Lexeme tree, Environment env) {

        if (tree.check(Lexeme.Type.VarKeyword))
            return evalVarDefine(tree, env);
        else if (tree.check(Lexeme.Type.FuncKeyword))
            return evalFuncDefine(tree, env);
        else if (tree.check(Lexeme.Type.ArrayKeyword))
            return evalArrayDefine(tree, env);
        else if (tree.check(Lexeme.Type.IfKeyword))
            return evalIfDefine(tree, env);

        return env.nullLexeme();
    }

    private Lexeme evalIfDefine(Lexeme tree, Environment env) {


    }

    private Lexeme evalArrayDefine(Lexeme tree, Environment env) {

        ArrayList<Lexeme> params = p.getArrayInitilizers(tree);
        ArrayList<Lexeme> init = evalInitializerExpression(params, env);

        return env.insertArray(p.getVariable(tree), init);
    }

    private Lexeme evalFuncDefine(Lexeme tree, Environment env) {

        return env.insertFuncDef(p.getVariable(tree), p.getParams(tree), p.getBlock(tree));
    }

    private Lexeme evalVarDefine(Lexeme tree, Environment env) {

        Lexeme var = p.getVariable(tree);
        Lexeme initializer = p.getInitilizer(tree);
        return env.insertVariable(var, evalVarInitializerExpression(initializer, env));
    }

    private Lexeme evalVarInitializerExpression(Lexeme lex, Environment env) {
        if(lex == null)
            return env.nullLexeme();
        else
            return evalExpression(lex, env);
    }

    private ArrayList<Lexeme> evalInitializerExpression(ArrayList<Lexeme> lex, Environment env) {

        ArrayList<Lexeme> res = new ArrayList<>();

        for(Lexeme l : lex) {
            res.add(evalExpression(l, env));
        }

        return res;
    }

    private Lexeme evalExpression(Lexeme tree, Environment env) {

        Lexeme unary = p.getUnary(tree);

        if (unary.check(Lexeme.Type.IntegerType) || unary.check(Lexeme.Type.StringType))
            return unary;

        return env.nullLexeme();
    }
}
