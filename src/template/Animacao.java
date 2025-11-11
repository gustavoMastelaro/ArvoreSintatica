package template;

/**
 *
 * @author gusta
 */
public class Animacao {
    private Node node;

    private double centroXIni;
    private double centroYIni;
    private double centroXFim;
    private double centroYFim;

    private double tempoTotal;
    private double tempoAtual;

    private boolean finalizada = false;

    public Animacao(Node node, double centroXIni, double centroYIni, double centroXFim, double centroYFim, double tempoTotal) {
        this.node = node;
        this.centroXIni = centroXIni;
        this.centroYIni = centroYIni;
        this.centroXFim = centroXFim;
        this.centroYFim = centroYFim;
        this.tempoTotal = tempoTotal;
        tempoAtual = 0;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    public double getCentroXIni() {
        return centroXIni;
    }

    public void setCentroXIni(double centroXIni) {
        this.centroXIni = centroXIni;
    }

    public double getCentroYIni() {
        return centroYIni;
    }

    public void setCentroYIni(double centroYIni) {
        this.centroYIni = centroYIni;
    }

    public double getCentroXFim() {
        return centroXFim;
    }

    public void setCentroXFim(double centroXFim) {
        this.centroXFim = centroXFim;
    }

    public double getCentroYFim() {
        return centroYFim;
    }

    public void setCentroYFim(double centroYFim) {
        this.centroYFim = centroYFim;
    }

    public void atualizar(double delta) {

        if (finalizada) {
            return;
        }

        tempoAtual += delta;
        double t = Math.min(1, tempoAtual / tempoTotal);

        double novoCentroX = centroXIni + (centroXFim - centroXIni) * t;
        double novoCentroY = centroYIni + (centroYFim - centroYIni) * t;

        node.setCentroX(novoCentroX);
        node.setCentroY(novoCentroY);

        if (t > 1) {
            finalizada = true;
        }
    }
}