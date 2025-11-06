package template;

import br.com.davidbuzatto.jsge.core.Camera2D;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.imgui.GuiButton;
import br.com.davidbuzatto.jsge.imgui.GuiInputDialog;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.awt.Color;

public class Main extends EngineFrame {

    private Rectangle focoBoundary;
    private Camera foco;
    private Camera2D camera;
    private GuiButton btnInserirExpressao;
    private GuiInputDialog entradaExpressao;
    private String expressao;
    private Node raiz;
    private String mensagem;

    public Main() {
        super(
                800,
                600,
                "Árvore Sintática",
                60,
                true,
                false,
                false,
                false,
                false,
                false
        );
    }

    @Override
    public void create() {
        btnInserirExpressao = new GuiButton(50, 100, 200, 40, "Inserir Expressão", this);
        entradaExpressao = new GuiInputDialog(
                "Entrada de Expressão",
                "Digite uma expressão (ex: (3+4)*2 )",
                "Fechar",
                true,
                this
        );
        mensagem = "Pressione o botão acima para inserir uma expressão.";
        expressao = "";
        foco = new Camera(new Vector2((getScreenWidth() - 5) / 2, (getScreenHeight() - 5) / 2),
                new Vector2(10, 10), 400);

        camera = new Camera2D();
        focoBoundary = new Rectangle(0, 0, getScreenWidth(), getScreenHeight());
    }

    @Override
    public void update(double delta) {

        btnInserirExpressao.update(delta);
        entradaExpressao.update(delta);

        foco.update(delta, focoBoundary, this);

        if (isKeyDown(KEY_DELETE)) {
            camera.rotation--;
        } else if (isKeyDown(KEY_PAGE_DOWN)) {
            camera.rotation++;
        }

        if (isKeyDown(KEY_KP_ADD) || isKeyDown(KEY_EQUAL)) {
            camera.zoom += 0.01;
        } else if (isKeyDown(KEY_KP_SUBTRACT) || isKeyDown(KEY_MINUS)) {
            camera.zoom -= 0.01;
            if (camera.zoom < 0.1) {
                camera.zoom = 0.1;
            }
        }

        if (isKeyPressed(KEY_R)) {
            camera.rotation = 0;
            camera.zoom = 1;
            foco.pos.x = getScreenWidth() / 2;
            foco.pos.y = getScreenHeight() / 2;
        }

        atualizarCamera();

        if (btnInserirExpressao.isMousePressed()) {
            entradaExpressao.show();
        }

        if ((entradaExpressao.isOkButtonPressed() || entradaExpressao.isEnterKeyPressed())) {
            expressao = entradaExpressao.getValue();
            entradaExpressao.hide();
            if (expressao != null && !expressao.isBlank()) {
                try {
                    raiz = construirArvore(expressao.replaceAll("\\s+", ""));
                    mensagem = "Expressão carregada com sucesso.";
                } catch (Exception e) {
                    mensagem = "Erro ao processar a expressão!";
                    raiz = null;
                }
            } else {
                mensagem = "Nenhuma expressão informada.";
            }
        }
    }

    @Override
    public void draw() {

        beginMode2D(camera);
        drawText("Visualizador de Árvore Sintática", new Vector2(50, 40), 22, BLACK);
        btnInserirExpressao.draw();

        drawText("Expressão: " + (expressao.isEmpty() ? "Nenhuma" : expressao), new Vector2(50, 180), 18, BLACK);
        drawText("" + mensagem, new Vector2(50, 210), 16, BLACK);

        if (raiz != null) {
            drawArvore(raiz, getWidth() / 2.0, 300, getWidth() / 4.0);
        }
       
        entradaExpressao.draw();
        endMode2D();
    }

    private static class Node {

        String valor;
        Node esq;
        Node dir;

        Node(String v) {
            valor = v;
        }
    }

    private void drawArvore(Node n, double x, double y, double offsetX) {
        if (n == null) {
            return;
        }

        double yOffset = 80;

        // ligações entre nós
        if (n.esq != null) {
            drawLine(new Vector2(x, y), new Vector2(x - offsetX, y + yOffset), BLACK);
        }
        if (n.dir != null) {
            drawLine(new Vector2(x, y), new Vector2(x + offsetX, y + yOffset), BLACK);
        }

        // nó atual
        fillCircle(new Vector2(x, y), 25, new Color(200, 230, 255));
        drawCircle(new Vector2(x, y), 25, BLACK);
        drawTextCentered(n.valor, new Vector2(x, y - 8), 20, BLACK);

        // recursão
        drawArvore(n.esq, x - offsetX, y + yOffset, offsetX / 2);
        drawArvore(n.dir, x + offsetX, y + yOffset, offsetX / 2);
    }

    private Node construirArvore(String expr) {
        return parseExpr(expr);
    }

    private Node parseExpr(String expr) {

        expr = removeParentesesExternos(expr);

        int nivel = 0;
        int pos = -1;

        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == ')') {
                nivel++;
            } else if (c == '(') {
                nivel--;
            } else if ((c == '+' || c == '-') && nivel == 0) {
                pos = i;
                break;
            }
        }

        if (pos != -1) {
            Node n = new Node(String.valueOf(expr.charAt(pos)));
            n.esq = parseExpr(expr.substring(0, pos));
            n.dir = parseExpr(expr.substring(pos + 1));
            return n;
        }

        nivel = 0;
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == ')') {
                nivel++;
            } else if (c == '(') {
                nivel--;
            } else if ((c == '*' || c == '/') && nivel == 0) {
                pos = i;
                break;
            }
        }

        if (pos != -1) {
            Node n = new Node(String.valueOf(expr.charAt(pos)));
            n.esq = parseExpr(expr.substring(0, pos));
            n.dir = parseExpr(expr.substring(pos + 1));
            return n;
        }

        if (expr.startsWith("(") && expr.endsWith(")")) {
            return parseExpr(expr.substring(1, expr.length() - 1));
        }

        return new Node(expr);
    }

    private String removeParentesesExternos(String expressao) {
        while (expressao.startsWith("(") && expressao.endsWith(")") && corresponde(expressao)) {
            expressao = expressao.substring(1, expressao.length() - 1);
        }
        return expressao;
    }

    private boolean corresponde(String expr) {
        int nivel = 0;
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') {
                nivel++;
            } else if (c == ')') {
                nivel--;
            }
            if (nivel == 0 && i < expr.length() - 1) {
                return false;
            }
        }
        return true;
    }

    private void drawTextCentered(String text, Vector2 pos, int size, Color color) {
        double largura = measureText(text, size);
        int tx = (int) (pos.x - largura / 2);
        int ty = (int) (pos.y + size / 3.0);
        drawText(text, tx, ty, size, color);
    }

    private void atualizarCamera() {
        camera.target.x = foco.pos.x;
        camera.target.y = foco.pos.y;
        camera.offset.x = getScreenWidth() / 2;
        camera.offset.y = getScreenHeight() / 2;
    }

    public static void main(String[] args) {
        new Main();
    }

}
