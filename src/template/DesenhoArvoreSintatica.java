package template;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.awt.Color;
import parser.ast.Expressao;
import parser.ast.ExpressaoBinaria;
import parser.ast.Numero;


public class DesenhoArvoreSintatica {
    
    public static void desenhar(EngineFrame frame, Expressao e, 
            double x, double y, double espacamento) {
        if (e == null) return;

        double raio = 25.0;

        Color corNo = Color.BLACK;
        Color corBorda = Color.WHITE;
        Color corTexto = Color.WHITE;
        Color corLigacao = Color.BLACK;

        if (e instanceof Numero) {
            Numero num = (Numero) e;
            frame.fillCircle(x, y, raio, corNo);

            frame.drawCircle(x, y, raio, corBorda);
            drawTextCentered(frame, num.getToken().valor(), x, y, 14, corTexto);
            return;
        }

        if (e instanceof ExpressaoBinaria) {
            ExpressaoBinaria bin = (ExpressaoBinaria) e;

            // desenha o nó atual
            frame.fillCircle(x, y, raio, corNo);
            frame.drawCircle(x, y, raio, corBorda);
            String texto = bin.getOperador() != null ? bin.getOperador().valor() : "?";
            drawTextCentered(frame, texto, x, y, 14, corTexto);

            double childY = y + 80.0;

            // filho esquerdo
            if (bin.getOperandoE() != null) {
                double childX = x - espacamento;
                frame.drawLine(x, y + raio, childX, childY - raio, corLigacao);
                desenhar(frame, bin.getOperandoE(), childX, childY, espacamento / 1.8);
            }

            // filho direito
            if (bin.getOperandoD() != null) {
                double childX = x + espacamento;
                frame.drawLine(x, y + raio, childX, childY - raio, corLigacao);
                desenhar(frame, bin.getOperandoD(), childX, childY, espacamento / 1.8);
            }

            return;
        }

        // caso generico
        frame.fillCircle(x, y, raio, corNo);
        frame.drawCircle(x, y, raio, corBorda);
        drawTextCentered(frame, "?", x, y, 14, corTexto);
    }

    private static void drawTextCentered(EngineFrame frame, String text, 
            double x, double y, int size, Color color) {
        double textWidth = frame.measureText(text, size);
        int tx = (int) Math.round(x - textWidth / 2.0);
        int ty = (int) Math.round(y + (size / 3.0));
        frame.drawText(text, tx, ty, size, color);
    }

}




