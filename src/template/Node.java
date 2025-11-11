package template;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.awt.Color;

public class Node {

    private int valor;
    private Color corLinha;
    private double centroX;
    private double centroY;
    private Node filhoEsquerda;
    private Node filhoDireita;

    public Node() {

    }

    public Node(int valor, Color corLinha, double centroX, double centroY) {
        this.valor = valor;
        this.corLinha = corLinha;
        this.centroX = centroX;
        this.centroY = centroY;
        filhoEsquerda = null;
        filhoDireita = null;
    }

    public void setCorLinha(Color corLinha) {
        this.corLinha = corLinha;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public double getCentroX() {
        return centroX;
    }

    public void setCentroX(double centroX) {
        this.centroX = centroX;
    }

    public double getCentroY() {
        return centroY;
    }

    public void setCentroY(double centroY) {
        this.centroY = centroY;
    }

    public Node getFilhoEsquerda() {
        return filhoEsquerda;
    }

    public void setFilhoEsquerda(Node filhoEsquerda) {
        this.filhoEsquerda = filhoEsquerda;
    }

    public Node getFilhoDireita() {
        return filhoDireita;
    }

    public void setFilhoDireita(Node filhoDireita) {
        this.filhoDireita = filhoDireita;
    }

    public void setCor(Color corLinha) {
        this.corLinha = corLinha;
    }

    public Color getCor() {
        return corLinha;
    }

    public void drawNode(EngineFrame e) {

        if (filhoEsquerda != null) {
            e.drawLine(
                    centroX,
                    centroY + 20,
                    filhoEsquerda.centroX,
                    filhoEsquerda.centroY - 20,
                    filhoEsquerda.corLinha
            );
        }

        if (filhoDireita != null) {
            e.drawLine(
                    centroX,
                    centroY + 20,
                    filhoDireita.centroX,
                    filhoDireita.centroY - 20,
                    filhoDireita.corLinha
            );
        }

        e.fillCircle(centroX, centroY, 20, EngineFrame.WHITE);
        e.drawCircle(centroX, centroY, 20, EngineFrame.BLACK);
        e.drawText(
                String.valueOf(valor),
                (centroX - (e.measureText(String.valueOf(valor), 18) / 2)),
                centroY - 3,
                18,
                EngineFrame.BLACK);
    }
}
