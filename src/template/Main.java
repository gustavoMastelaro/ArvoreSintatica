package template;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.core.utils.CoreUtils;
import br.com.davidbuzatto.jsge.core.utils.DrawingUtils;
import br.com.davidbuzatto.jsge.image.Image;
import java.awt.Color;
import javax.swing.JOptionPane;
import parser.Parser;
import parser.ast.Expressao;
import parser.ast.ExpressaoBinaria;
import parser.ast.Numero;

public class Main extends EngineFrame {

    private Image logo;
    private Expressao raiz;
    private String expressaoAtual = "";

    public Main() {
        super(
                800, 600, "Arvore Sintatica", 60,
                true, false, false, false, false, false
        );
    }

    @Override
    public void create() {
        logo = DrawingUtils.createLogo();
        logo.resize((int) (logo.getWidth() * 0.1), (int) (logo.getWidth() * 0.1));
        setWindowIcon(logo);
        solicitarExpressao();
    }

    private void solicitarExpressao() {
        String expr = JOptionPane.showInputDialog("Digite a expressao:");

        if (expr == null || expr.isBlank()) {
            expressaoAtual = "";
            raiz = null;
            return;
        }

        expressaoAtual = expr;
        Parser parser = Parser.parse(expr);
        raiz = obterExpressao(parser);

        if (raiz == null) {
            JOptionPane.showMessageDialog(null, "Erro: Expressão inválida.");
            expressaoAtual = "";
        }
    }

    private Expressao obterExpressao(Parser parser) {
        try {
            var campo = Parser.class.getDeclaredField("expressaoResultante");
            campo.setAccessible(true);
            return (Expressao) campo.get(parser);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void update(double delta) {
        if (isKeyPressed(KEY_ENTER)) {
            solicitarExpressao();
        }
    }

    @Override
    public void draw() {
        clearBackground(Color.WHITE);

        if (raiz == null) {
            String text = "Pressione Enter para inserir uma expressao.";
            double tx = getScreenWidth() / 2.0 - measureText(text, 26) / 2.0;
            drawText(text, (int) tx, getScreenHeight() / 2, 26, Color.BLACK);
        } else {
            double rootX = getScreenWidth() / 2.0;
            double espacamentoInicial = getScreenWidth() / 6.0;
            drawTree(raiz, rootX, 280, espacamentoInicial);
        }

        int margemEsquerda = 25;
        int margemInferior = 35;

        String texto2 = "Pressione Enter para digitar uma nova expressão";
        String texto1 = !expressaoAtual.isBlank()
                ? "Última expressão: \"" + expressaoAtual + "\""
                : "Nenhuma expressão registrada";

        int tamTexto1 = 20;
        int tamTexto2 = 18;

        int yTexto2 = getScreenHeight() - margemInferior;
        int yTexto1 = yTexto2 - tamTexto1 - 6;

        drawText(texto1, margemEsquerda, yTexto1, tamTexto1, Color.BLACK);
        drawText(texto2, margemEsquerda, yTexto2, tamTexto2, Color.BLACK);

        drawImage(
                logo,
                getScreenWidth() - logo.getWidth() - 20,
                getScreenHeight() - logo.getHeight() - 30
        );

        drawText(
                CoreUtils.getVersion(),
                (int) (getScreenWidth() - measureText(CoreUtils.getVersion(), 16) - 20),
                getScreenHeight() - 25,
                16,
                Color.BLACK
        );

        drawFPS(20, 20);
    }

    private void drawTree(Expressao e, double x, double y, double espacamento) {
        if (e == null) {
            return;
        }

        double raio = 18.0;
        int fontSize = 28;

        if (e instanceof Numero num) {
            fillCircle(x, y, raio, Color.WHITE);
            drawCircle(x, y, raio, Color.BLACK);
            drawTextCentralizado(num.getToken().valor(), x, y + 6, fontSize, Color.BLACK);
        } else if (e instanceof ExpressaoBinaria bin) {
            fillCircle(x, y, raio, Color.WHITE);
            drawCircle(x, y, raio, Color.BLACK);
            drawTextCentralizado(bin.getOperador().valor(), x, y + 6, fontSize, Color.BLACK);

            double childY = y + 100.0;

            if (bin.getOperandoE() != null) {
                double childX = x - espacamento;
                drawLine(x, y + raio, childX, childY - raio, Color.BLACK);
                drawTree(bin.getOperandoE(), childX, childY, espacamento / 1.6);
            }

            if (bin.getOperandoD() != null) {
                double childX = x + espacamento;
                drawLine(x, y + raio, childX, childY - raio, Color.BLACK);
                drawTree(bin.getOperandoD(), childX, childY, espacamento / 1.6);
            }
        }
    }

    private void drawTextCentralizado(String text, double x, double y, int size, Color color) {
        double largura = measureText(text, size);
        int tx = (int) Math.round(x - largura / 2.0);
        int ty = (int) Math.round(y + size / 3.0 - 2);
        drawText(text, tx, ty, size, color);
    }

    public static void main(String[] args) {
        new Main();
    }
}
