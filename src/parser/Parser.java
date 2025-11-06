package parser;

import parser.ast.Expressao;
import parser.ast.ExpressaoAdicao;
import parser.ast.ExpressaoBinaria;
import parser.ast.ExpressaoMultiplicacao;
import parser.ast.Numero;

/**
 * Parser para expressões aritméticas.
 *
 * Gramática (EBNF): expressao -> termo ( opAd termo )* . termo -> fator ( opMu
 * fator )* . fator -> inteiro | '(' expressao ')' . opAd -> '+' | '-' . opMu ->
 * '*' | '/' . inteiro -> [0..9]+ .
 *
 * @author Prof. Dr. David Buzatto
 */
public class Parser {

    private Lexer lexer;
    private Expressao expressaoResultante;

    private double resultado;
    private String AST;

    public static Parser parse(String expressao) {
        return new Parser(expressao);
    }


    public double getResultado() {
        return resultado;
    }

    public String getAST() {
        return AST;
    }

    private Parser(String expressao) {

        lexer = new Lexer(expressao);
        expressaoResultante = parseExpressao();

        resultado = calcular(expressaoResultante);

        StringBuilder sbAST = new StringBuilder();
        gerarAST(expressaoResultante, 0, sbAST);
        AST = sbAST.toString();

    }

    // expressao -> termo ( opAd termo )* .
    private Expressao parseExpressao() {

        Expressao termoE = null;
        Expressao termoD = null;
        Token operador = null;

        termoE = parseTermo();

        while (lexer.getToken().tipo() != TipoToken.EOF
                && (lexer.getToken().tipo() == TipoToken.ADICAO
                || lexer.getToken().tipo() == TipoToken.SUBTRACAO)) {

            operador = lexer.getToken();
            lexer.proximo();

            termoD = parseTermo();
            termoE = new ExpressaoAdicao(termoE, operador, termoD);

        }

        return termoE;

    }

    // termo -> fator ( opMu fator )* .
    private Expressao parseTermo() {

        Expressao fatorE = null;
        Expressao fatorD = null;
        Token operador = null;

        fatorE = parseFator();

        while (lexer.getToken().tipo() != TipoToken.EOF
                && (lexer.getToken().tipo() == TipoToken.MULTIPLICACAO
                || lexer.getToken().tipo() == TipoToken.DIVISAO || lexer.getToken().tipo() == TipoToken.RESTO)) {

            operador = lexer.getToken();
            lexer.proximo();

            fatorD = parseFator();
            fatorE = new ExpressaoMultiplicacao(fatorE, operador, fatorD);

        }

        return fatorE;

    }

    // fator -> inteiro | '(' expressao ')' .
    private Expressao parseFator() {

        Expressao expressao = null;

        if (lexer.getToken().tipo() == TipoToken.NUMERO) {
            Token inteiro = lexer.getToken();
            lexer.proximo();
            expressao = new Numero(inteiro);
        } else if (lexer.getToken().tipo() == TipoToken.PARENTESES_ESQUERDO) {
            lexer.proximo();
            expressao = parseExpressao();
            lexer.proximo(); // depois do parênteses da direita
        }

        return expressao;

    }

    // visitor para cálculo
    private double calcular(Expressao e) {

        if (e instanceof Numero numero) {
            return Double.parseDouble(numero.getToken().valor());
        } else if (e instanceof ExpressaoAdicao adicao) {
            double valorE = calcular(adicao.getOperandoE());
            double valorD = calcular(adicao.getOperandoD());
            if (adicao.getOperador().tipo() == TipoToken.ADICAO) {
                return valorE + valorD;
            } else {
                return valorE - valorD;
            }
        } else if (e instanceof ExpressaoMultiplicacao multiplicacao) {
            double valorE = calcular(multiplicacao.getOperandoE());
            double valorD = calcular(multiplicacao.getOperandoD());
            if (multiplicacao.getOperador().tipo() == TipoToken.MULTIPLICACAO) {
                return valorE * valorD;
            } else if (multiplicacao.getOperador().tipo() == TipoToken.RESTO) {
                return valorE / valorD;
            } else {
                return valorE % valorD;
            }
        }

        return 0;

    }

    // visitor para impressão
    private void gerarAST(Expressao e, int nivel, StringBuilder sb) {

        String espacamento = nivel > 0 ? " |   ".repeat(nivel - 1) : "";
        String linha = nivel > 0 ? " |___" : "";

        if (e instanceof Numero inteiro) {
            sb.append(espacamento).append(linha).append(inteiro).append("\n");
        } else if (e instanceof ExpressaoBinaria exprBin) {
            sb.append(espacamento).append(linha).append("(").append(exprBin.getOperador()).append(")").append("\n");
            gerarAST(exprBin.getOperandoE(), nivel + 1, sb);
            gerarAST(exprBin.getOperandoD(), nivel + 1, sb);
        }

    }

    public parser.ast.Expressao getExpressaoResultante() {
        return expressaoResultante;
    }

}
