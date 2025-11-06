package template;

import br.com.davidbuzatto.jsge.core.Camera2D;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.awt.Color;
import javax.swing.JOptionPane;
import parser.Parser;
import parser.ast.Expressao;
import parser.ast.ExpressaoBinaria;
import parser.ast.Numero;

public class Main extends EngineFrame {
    
    private Rectangle focoBoundary;
    private Camera foco;
    private Camera2D camera;
    private Expressao raiz;
    private String expressaoAtual;

    public Main() {
        super(
                800, 600, "Arvore Sintatica", 60,
                true, false, false, false, false, false
        );
    }

    @Override
    public void create() {
        expressaoAtual = "nenhuma expressao";
        foco = new Camera(new Vector2 ((getScreenWidth() - 5) / 2, (getScreenHeight() - 5 ) / 2), 
                new Vector2 (10,10), 400);
        
        camera = new Camera2D();
        focoBoundary = new Rectangle(0,0,getScreenWidth(), getScreenHeight());
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
            JOptionPane.showMessageDialog(null, "Erro: Expressão inválida");
            expressaoAtual = "";
        }
    }

    private Expressao obterExpressao(Parser parser) {
        return parser.getExpressaoResultante();
    }

    @Override
    public void update(double delta) {
        if (isKeyPressed(KEY_ENTER)) {
            solicitarExpressao();
        }
        foco.update( delta, focoBoundary, this );
        
        if ( isKeyDown( KEY_DELETE ) ) {
            camera.rotation--;
        } else if ( isKeyDown( KEY_PAGE_DOWN ) ) {
            camera.rotation++;
        }
        
        if ( isKeyDown( KEY_KP_ADD ) || isKeyDown( KEY_EQUAL ) ) {
            camera.zoom += 0.01;
        } else if ( isKeyDown( KEY_KP_SUBTRACT ) || isKeyDown( KEY_MINUS ) ) {
            camera.zoom -= 0.01;
            if ( camera.zoom < 0.1 ) {
                camera.zoom = 0.1;
            }
        }
        
        if ( isKeyPressed( KEY_R ) ) {
            camera.rotation = 0;
            camera.zoom = 1;
            foco.pos.x = getScreenWidth() / 2;
            foco.pos.y = getScreenHeight() / 2;
        }
        
        atualizarCamera();
    }

    @Override
    public void draw() {
        
        clearBackground(WHITE);
        
        beginMode2D(camera);
        if (raiz == null) {
            String text = "Pressione Enter para inserir uma expressao";
            double tx = getScreenWidth() / 2.0 - measureText(text, 26) / 2.0;
            drawText(text, (int) tx, getScreenHeight() / 2, 26, BLACK);
        } else {
            double rootX = getScreenWidth() / 2.0;
            double espacamentoInicial = getScreenWidth() / 6.0;
            drawTree(raiz, rootX, 280, espacamentoInicial);
        }
        
        endMode2D();
        int margemEsquerda = 25;
        int margemInferior = 35;

        String texto2 = "Pressione Enter para digitar uma nova expressão";
        String texto1 = !expressaoAtual.isBlank()
                ? "Resultado: \"" + expressaoAtual + "\""
                : "Nenhuma expressão registrada";

        int tamTexto1 = 20;
        int tamTexto2 = 18;

        int yTexto2 = getScreenHeight() - margemInferior;
        int yTexto1 = yTexto2 - tamTexto1 - 6;

        drawText(texto1, margemEsquerda, yTexto1, tamTexto1, BLACK);
        drawText(texto2, margemEsquerda, yTexto2, tamTexto2, BLACK);
     
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
            drawTextCentralizado(num.getToken().valor(), x, y + 6, fontSize, BLACK);
        } else if (e instanceof ExpressaoBinaria bin) {
            fillCircle(x, y, raio, Color.WHITE);
            drawCircle(x, y, raio, Color.BLACK);
            drawTextCentralizado(bin.getOperador().valor(), x, y + 6, fontSize, BLACK);

            double childY = y + 100.0;

            if (bin.getOperandoE() != null) {
                double childX = x - espacamento;
                drawLine(x, y + raio, childX, childY - raio, BLACK);
                drawTree(bin.getOperandoE(), childX, childY, espacamento / 1.6);
            }

            if (bin.getOperandoD() != null) {
                double childX = x + espacamento;
                drawLine(x, y + raio, childX, childY - raio, BLACK);
                drawTree(bin.getOperandoD(), childX, childY, espacamento / 1.6);
            }
        }
    }

    private void drawTextCentralizado(String text, double x, double y, int size, Color color) {
        double largura = measureText(text, size);
        int tx = (int) Math.round(x - largura / 2.0);
        int ty = (int) Math.round(y + size / 3.0 - 2);
        drawText(text, tx, ty - 22, size, color);
    }
    
    private void atualizarCamera(){
        camera.target.x = foco.pos.x;
        camera.target.y = foco.pos.y;
        camera.offset.x = 0;
        camera.offset.y = 0;
    }

    public static void main(String[] args) {
        new Main();
    }
}
