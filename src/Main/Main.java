/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Models.BancoNodos;
import Models.BancoPreguntas;
import Models.Nodo;
import Models.GeneradorArbol;
import Models.Pregunta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author A. Vega
 */
public class Main extends Application {

    private TextArea consola;
    private TextField input;
    private Canvas miniMapa;
    private GraphicsContext gc;

    // Lógica del juego
    private BancoPreguntas bancoPreguntas;
    private BancoNodos bancoN;
    private Nodo raiz;
    private Nodo actual;
    private Pregunta preguntaActual;

    // Mapa de posiciones para dibujar mini-mapa
    private Map<Nodo, Point2D> nodePositions = new HashMap<>();
    private double canvasWidth = 320;
    private double canvasHeight = 260;

    private enum Mode {
        RESPONDIENDO, COMANDO
    }
    private Mode mode = Mode.COMANDO;

    @Override
    public void start(Stage stage) {
        // UI: consola
        consola = new TextArea();
        consola.setEditable(false);

        consola.setWrapText(true);
        consola.setFont(Font.font("Consolas", 14));
        consola.setStyle("-fx-control-inner-background: black; -fx-text-fill: #00FF66; -fx-border-color: #00FF66; -fx-border-width: 2;");

        ScrollPane scroll = new ScrollPane(consola);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setStyle("-fx-background: black; -fx-border-color: #00FF66;");

        // input
        input = new TextField();
        input.setPromptText("Escribe: izquierda | derecha | salir (o responde la pregunta)");
        input.setFont(Font.font("Consolas", 13));
        input.setStyle("-fx-control-inner-background: black; -fx-text-fill: #33FF66;");
        
        input.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                String texto = input.getText();
                input.clear();
                procesarTexto(texto == null ? "" : texto.trim());
            }
        });

        // mini mapa (Canvas)
        miniMapa = new Canvas(canvasWidth, canvasHeight);
        gc = miniMapa.getGraphicsContext2D();

        StackPane miniWrapper = new StackPane(miniMapa);
        miniWrapper.setPadding(new Insets(8));
        miniWrapper.setStyle("-fx-border-color: #00FF66; -fx-border-width: 2; -fx-background-color: rgba(0,0,0,0.25);");
        miniWrapper.setMouseTransparent(true);
        miniWrapper.setAlignment(Pos.TOP_RIGHT);

        // layout: ponemos consola y sobre ella el mini mapa en la esquina superior derecha
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: black;");
        VBox centerBox = new VBox(6, scroll, input);
        centerBox.setPadding(new Insets(8));
        root.setCenter(centerBox);

        // StackPane para superponer mini-mapa arriba a la derecha
        StackPane stack = new StackPane();
        stack.getChildren().addAll(centerBox, miniWrapper);
        StackPane.setAlignment(miniWrapper, Pos.TOP_RIGHT);
        root.setCenter(stack);

        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("Aventura Cibernética - Consola");
        stage.setScene(scene);
        stage.show();

       

        // Cargar recursos y arrancar lógica
        
        try {
            // Ajusta los nombres de recursos JSON según tus archivos en resources
            bancoN = new BancoNodos("resources/nodos.json");
            bancoPreguntas = new BancoPreguntas("resources/preguntas.json");
            

            GeneradorArbol gen = new GeneradorArbol();
            List<Nodo> lista = bancoN.getNodos();
            raiz = gen.construirArbol(lista);       

            actual = raiz;

            bienvenida();
            calcularPosicionesArbol();
            dibujarMiniMapa();
            mostrarNodoActualYPreguntar();

        } catch (Exception ex) {
            consola.appendText("[ERROR] No fue posible cargar recursos: " + ex.getMessage() + "\n");
            ex.printStackTrace();
        }
    }
    
    private void bienvenida() {
        consola.appendText("¡Bienvenido a la Aventura Cibernética!\n");
        consola.appendText("Tu misión: recuperar el control de la red y detener el ataque.\n");
        consola.appendText("Comandos: 'izquierda', 'derecha', 'salir'. Responde las preguntas para avanzar.\n\n");
    }

    // -------------------------
    // Lógica de comandos / preguntas
    // -------------------------
    private void procesarTexto(String texto) {
        if (texto.isEmpty()) {
            return;
        }

        consola.appendText("> " + texto + "\n");

        if (mode == Mode.RESPONDIENDO) {
            comprobarRespuesta(texto);
            return;
        }

        String cmd = texto.trim().toLowerCase();
        if (cmd.equals("salir")) {
            consola.appendText("Saliendo... ¡Hasta la próxima!\n");
            System.exit(0);
            return;
        }

        if (cmd.equals("izquierda") || cmd.equals("izq")) {
            if (actual.getIzquierda() != null) {
                actual = actual.getIzquierda();
                consola.appendText("-> Te desplazas a la izquierda.\n\n");
                // redibujar mini-mapa con nuevo nodo destacado
                dibujarMiniMapa();
                mostrarNodoActualYPreguntar();
            } else {
                consola.appendText("No hay nodo a la izquierda. Intenta otra opción.\n");
            }
            return;
        }

        if (cmd.equals("derecha") || cmd.equals("der")) {
            if (actual.getDerecha() != null) {
                actual = actual.getDerecha();
                consola.appendText("-> Te desplazas a la derecha.\n\n");
                dibujarMiniMapa();
                mostrarNodoActualYPreguntar();
            } else {
                consola.appendText("No hay nodo a la derecha. Intenta otra opción.\n");
            }
            return;
        }

        if (cmd.equals("volver")) {
            try {
                Nodo padre = actual.getPadre();
                if (padre != null) {
                    actual = padre;
                    consola.appendText("↩️  Regresaste al nodo anterior: " + actual.getNodo() + "\n\n");
                    dibujarMiniMapa();
                    mostrarNodoActualYPreguntar();
                } else {
                    consola.appendText("No puedes volver, estás en la raíz.\n");
                }
            } catch (Exception e) {
                consola.appendText("No se pudo ejecutar 'volver' (verifica que Nodo tenga getPadre()).\n");
            }
            return;
        }

        consola.appendText("Comando no reconocido. Usa izquierda | derecha | volver | salir\n");
    }

    private void comprobarRespuesta(String texto) {
         if (preguntaActual == null) {
            consola.appendText("No hay pregunta activa. Cambiando a modo comando.\n");
            mode = Mode.COMANDO;
            return;
        }

        String respuestaEsperada = preguntaActual.getRespuesta().trim();

        // Permitir que el usuario responda por número si puso opciones
        boolean acerto = false;
        List<String> opciones = preguntaActual.getOpciones();
        if (respuestaEsperada.equalsIgnoreCase(texto)) {
            acerto = true;
        }

        if (acerto) {
            consola.appendText("¡Correcto! Ahora puedes elegir 'izquierda' o 'derecha'.\n\n");
            mode = Mode.COMANDO;
        } else {
            consola.appendText("Incorrecto. Intenta otra respuesta para este mismo reto.\n\n");
            // permanecer en RESPONDIENDO y mostrar la misma pregunta otra vez (opcional)
            consola.appendText(preguntaActual.getPregunta() + "\n");
            if (opciones != null && !opciones.isEmpty()) {
                for (int i = 0; i < opciones.size(); i++) {
                    consola.appendText("  " + (i + 1) + ") " + opciones.get(i) + "\n");
                }
                consola.appendText("Responde escribiendo la opción exacta (texto) o el número.\n\n");
            }
            mode = Mode.RESPONDIENDO;
        }
    }

    // -------------------------
    // Mostrar nodo y lanzar pregunta o manejos especiales
    // -------------------------
    private void mostrarNodoActualYPreguntar() {
        if (actual == null) {
            consola.appendText("> Nodo actual: null\n");
            mode = Mode.COMANDO;
            return;
        }

        consola.appendText("> Estás en: " + actual.getNodo() + "\n");

        String nombre = actual.getNodo() == null ? "" : actual.getNodo().toLowerCase();

        // nodo comprometido -> mostrar mini-mapa parcial y pista
        if (nombre.contains("comprometido") || nombre.contains("infectado") || nombre.contains("infiltrada") || nombre.contains("malware")) {
            consola.appendText("\n⚠️ Nodo comprometido detectado ⚠️\n");
            consola.appendText("Mostrando pista (esquema parcial):\n\n");
            // redibuja mini-mapa (ya hecho) pero también imprimimos un esquema ascii pequeño si quieres
            dibujarMiniMapa(); // ya marca el nodo actual
            consola.appendText("\nUsa esta pista para encontrar el Nodo Central Seguro.\n\n");
        }

        // victoria
        if (nombre.equals("nodo central seguro") || nombre.contains("central seguro")) {
            consola.appendText("\n🎉 ¡Felicitaciones! Has asegurado el Nodo Central Seguro.\n");
            consola.appendText("Escribe 'salir' para terminar o 'volver' para explorar más.\n\n");
            mode = Mode.COMANDO;
            return;
        }

        // nodo hoja y no es nodo central -> permitir 'volver'
        if (actual.getIzquierda() == null && actual.getDerecha() == null) {
            consola.appendText("\n🔒 Este nodo está aislado. No hay rutas desde aquí.\n");
            consola.appendText("Escribe 'volver' para regresar al nodo anterior o 'salir' para terminar.\n\n");
            mode = Mode.COMANDO;
            return;
        }

        // caso normal: lanzar pregunta
        lanzarPreguntaLocal();
    }

    private void lanzarPreguntaLocal() {
        try {
            preguntaActual = bancoPreguntas.obtenerPreguntaAleatoria();
            if (preguntaActual == null) {
                consola.appendText("[INFO] No hay preguntas disponibles. Avanza libremente.\n");
                mode = Mode.COMANDO;
                return;
            }

            consola.appendText("\n--- Reto de Ciberseguridad ---\n");
            consola.appendText(preguntaActual.getPregunta() + "\n");
            List<String> opts = preguntaActual.getOpciones();
            if (opts != null && !opts.isEmpty()) {
                for (int i = 0; i < opts.size(); i++) {
                    consola.appendText("  " + (i + 1) + ") " + opts.get(i) + "\n");
                }
                consola.appendText("Responde escribiendo el número o el texto de la opción.\n\n");
            } else {
                consola.appendText("Responde escribiendo la respuesta.\n\n");
            }
            mode = Mode.RESPONDIENDO;
        } catch (Exception e) {
            consola.appendText("[ERROR] No se pudo obtener pregunta: " + e.getMessage() + "\n");
            mode = Mode.COMANDO;
        }
    }

    // -------------------------
    // Mini-mapa: calcular posiciones y dibujar
    // -------------------------
    private void calcularPosicionesArbol() {
        nodePositions.clear();
        // Usaremos un layout simple: inorder traversal para x indices y depth para y.
        Map<Integer, List<Nodo>> depthMap = new HashMap<>();
        int maxDepth = fillDepthMap(raiz, 0, depthMap);
        // Obtener inorder order y asignar x por contador (por nivel global para simplicidad)
        Map<Nodo, Integer> inorderIndex = new HashMap<>();
        int[] counter = {0};
        inorderAssign(raiz, inorderIndex, counter);

        // Ahora asignar posiciones: x relativo al inorder index, y según profundidad
        // Normalizamos a canvasWidth/canvasHeight
        int maxIndex = counter[0] == 0 ? 1 : counter[0];
        for (Map.Entry<Nodo, Integer> e : inorderIndex.entrySet()) {
            Nodo node = e.getKey();
            int idx = e.getValue();
            int depth = depthOfNode(raiz, node, 0);
            double x = 30 + ((double) idx / (maxIndex)) * (canvasWidth - 60);
            double y = 20 + ((double) depth / (Math.max(1, maxDepth))) * (canvasHeight - 60);
            nodePositions.put(node, new Point2D(x, y));
        }
    }

    // llena depthMap y retorna profundidad maxima
    private int fillDepthMap(Nodo node, int depth, Map<Integer, List<Nodo>> depthMap) {
        if (node == null) {
            return depth - 1;
        }
        depthMap.computeIfAbsent(depth, k -> new ArrayList<>()).add(node);
        int leftMax = fillDepthMap(node.getIzquierda(), depth + 1, depthMap);
        int rightMax = fillDepthMap(node.getDerecha(), depth + 1, depthMap);
        return Math.max(depth, Math.max(leftMax, rightMax));
    }

    // asigna índices inorder incrementales
    private void inorderAssign(Nodo node, Map<Nodo, Integer> outIndex, int[] counter) {
        if (node == null) {
            return;
        }
        inorderAssign(node.getIzquierda(), outIndex, counter);
        outIndex.put(node, counter[0]++);
        inorderAssign(node.getDerecha(), outIndex, counter);
    }

    // calcula profundidad de un nodo (0 = raiz)
    private int depthOfNode(Nodo root, Nodo target, int depth) {
        if (root == null) {
            return -1;
        }
        if (root == target) {
            return depth;
        }
        int left = depthOfNode(root.getIzquierda(), target, depth + 1);
        if (left >= 0) {
            return left;
        }
        return depthOfNode(root.getDerecha(), target, depth + 1);
    }

    private void dibujarMiniMapa() {
        // limpiar
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        // fondo con leve grid
        gc.setStroke(Color.web("#003300", 0.2));
        for (int i = 0; i < canvasWidth; i += 20) {
            gc.strokeLine(i, 0, i, canvasHeight);
        }
        for (int i = 0; i < canvasHeight; i += 20) {
            gc.strokeLine(0, i, canvasWidth, i);
        }

        // dibujar conexiones
        gc.setLineWidth(2);
        gc.setStroke(Color.web("#00FF66", 0.9));
        for (Map.Entry<Nodo, Point2D> entry : nodePositions.entrySet()) {
            Nodo node = entry.getKey();
            Point2D p = entry.getValue();
            if (node.getIzquierda() != null) {
                Point2D q = nodePositions.get(node.getIzquierda());
                if (q != null) {
                    gc.strokeLine(p.getX(), p.getY(), q.getX(), q.getY());
                }
            }
            if (node.getDerecha() != null) {
                Point2D q = nodePositions.get(node.getDerecha());
                if (q != null) {
                    gc.strokeLine(p.getX(), p.getY(), q.getX(), q.getY());
                }
            }
        }

        // dibujar nodos
        double nodeRadius = 8;
        for (Map.Entry<Nodo, Point2D> entry : nodePositions.entrySet()) {
            Nodo node = entry.getKey();
            Point2D p = entry.getValue();

            // color según tipo
            String name = node.getNodo() == null ? "" : node.getNodo().toLowerCase();
            Color fill = Color.web("#00FF66"); // seguro
            if (name.contains("comprometido") || name.contains("infectado") || name.contains("infiltrada") || name.contains("malware")) {
                fill = Color.web("#FF3333"); // rojo
            } else if (name.contains("nodo central seguro") || name.contains("central seguro")) {
                fill = Color.web("#FFD166"); // amarillo
            }

            gc.setFill(fill);
            gc.fillOval(p.getX() - nodeRadius, p.getY() - nodeRadius, nodeRadius * 2, nodeRadius * 2);
            // contorno
            gc.setStroke(Color.web("#003300"));
            gc.strokeOval(p.getX() - nodeRadius, p.getY() - nodeRadius, nodeRadius * 2, nodeRadius * 2);
        }

        // destacar nodo actual
        if (actual != null && nodePositions.containsKey(actual)) {
            Point2D p = nodePositions.get(actual);
            gc.setStroke(Color.CYAN);
            gc.setLineWidth(3);
            gc.strokeOval(p.getX() - nodeRadius - 4, p.getY() - nodeRadius - 4, (nodeRadius + 4) * 2, (nodeRadius + 4) * 2);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
