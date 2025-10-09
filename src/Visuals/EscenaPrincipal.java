/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Visuals;

import Models.BancoNodos;
import Models.BancoPreguntas;
import Models.GeneradorArbol;
import Models.Nodo;
import Models.Pregunta;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author A. Vega
 */
public class EscenaPrincipal {

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
    private double canvasWidth = 280; // Ajustado para un look más compacto
    private double canvasHeight = 220;

    private enum Mode {
        RESPONDIENDO, COMANDO
    }
    private Mode mode = Mode.COMANDO;

    public Scene crearEscena() {
        consola = new TextArea();
        consola.setEditable(false);
        consola.setWrapText(true);
        consola.setFont(Font.font("Consolas", 14));
        consola.setStyle("""
        -fx-control-inner-background: #0A0A0A;
        -fx-text-fill: #00FF66;
        -fx-background-color: transparent;
        -fx-border-color: transparent;
    """);

        input = new TextField();
        input.setPromptText(">");
        input.setFont(Font.font("Consolas", 13));
        input.setStyle("""
        -fx-background-color: #0A0A0A;
        -fx-text-fill: #FFFFFF;
        -fx-border-color: transparent;
        -fx-prompt-text-fill: #555555;
    """);

        input.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                String texto = input.getText();
                input.clear();
                if (texto != null && !texto.trim().isEmpty()) {
                    procesarTexto(texto.trim());
                } else {
                    consola.appendText("> \n");
                }
            }
        });

        miniMapa = new Canvas(canvasWidth, canvasHeight);
        gc = miniMapa.getGraphicsContext2D();
        StackPane miniWrapper = new StackPane(miniMapa);
        miniWrapper.setPadding(new Insets(8));
        miniWrapper.setLayoutX(canvasWidth);
        miniWrapper.setLayoutY(canvasWidth);

        miniWrapper.setStyle("""
        -fx-border-color: #00FF66;
        -fx-border-width: 1;
        -fx-background-color: transparent;
    """);
        miniWrapper.setMouseTransparent(true);
        miniWrapper.setAlignment(Pos.TOP_RIGHT);

        miniWrapper.setOnMousePressed(e -> {

        });

        // --- Ventana de la consola ---
        BorderPane consoleWindow = new BorderPane();
        consoleWindow.setMaxSize(600, 450);
        consoleWindow.setStyle("""
        -fx-background-color: rgba(10, 10, 10, 0.9);
        -fx-border-color: #00FF66;
        -fx-border-width: 1;
        -fx-background-radius: 8;
        -fx-border-radius: 8;
        -fx-effect: dropshadow(gaussian, #00FF66, 10, 0.3, 0, 0);
    """);
        consoleWindow.setOnMouseClicked(event -> input.requestFocus());

        // --- Barra de título ---
        Label titleLabel = new Label("Command");
        titleLabel.setTextFill(Color.web("#CCCCCC"));
        titleLabel.setFont(Font.font("Consolas", 12));
        HBox titleBar = new HBox(titleLabel);
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(5, 10, 5, 10));
        titleBar.setStyle("-fx-background-color: #1E1E1E;");
        
        consoleWindow.setBottom(input);
        consoleWindow.setTop(titleBar);
        consoleWindow.setCenter(consola);   
        
        consoleWindow.setOnMouseClicked(e -> consola.requestFocus());


        // --- Contenedor raíz ---
        StackPane root = new StackPane();

        // --- Ensamblar escena ---
        root.getChildren().addAll( consoleWindow, miniWrapper);
        StackPane.setAlignment(consoleWindow, Pos.CENTER);
        StackPane.setAlignment(miniWrapper, Pos.TOP_RIGHT);
        StackPane.setMargin(miniWrapper, new Insets(20));

        // --- Cargar imagen de fondo tipo escritorio ---
        try {
            Image backgroundImg = new Image(new FileInputStream("C:\\Users\\USUARIO\\Downloads\\desktop_background.jpg"));
            BackgroundImage backgroundImage = new BackgroundImage(
                    backgroundImg,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            root.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            System.out.println("[ERROR] No se pudo cargar la imagen de fondo. Usando color negro.");
            root.setStyle("-fx-background-color: black;");
        }

        // --- Crear escena ---
        Scene scene = new Scene(root, 1024, 600);

        Platform.runLater(() -> input.requestFocus());
        // --- Inicializar lógica del juego ---
        inicializarJuego();

        return scene;
    }

    private void inicializarJuego() {
        try {
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
        boolean acerto = false;

        // Comprobar si la respuesta es el texto exacto
        if (respuestaEsperada.equalsIgnoreCase(texto)) {
            acerto = true;
        } // Comprobar si la respuesta es un número de opción válido
        else {
            try {
                int numRespuesta = Integer.parseInt(texto);
                List<String> opciones = preguntaActual.getOpciones();
                if (opciones != null && numRespuesta > 0 && numRespuesta <= opciones.size()) {
                    if (opciones.get(numRespuesta - 1).equalsIgnoreCase(respuestaEsperada)) {
                        acerto = true;
                    }
                }
            } catch (NumberFormatException e) {
                // No era un número, la comprobación por texto ya falló.
            }
        }

        if (acerto) {
            consola.appendText("¡Correcto! Ahora puedes elegir 'izquierda' o 'derecha'.\n\n");
            mode = Mode.COMANDO;
        } else {
            consola.appendText("Incorrecto. Intenta otra respuesta para este mismo reto.\n\n");
            lanzarPreguntaLocal(true); // Vuelve a mostrar la misma pregunta
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

        if (nombre.contains("comprometido") || nombre.contains("infectado") || nombre.contains("infiltrada") || nombre.contains("malware")) {
            consola.appendText("\n⚠️ Nodo comprometido detectado ⚠️\n");
            consola.appendText("Mostrando pista (esquema parcial).\n");
            consola.appendText("Usa esta pista para encontrar el Nodo Central Seguro.\n\n");
            dibujarMiniMapa();
        }

        if (nombre.equals("nodo central seguro") || nombre.contains("central seguro")) {
            consola.appendText("\n🎉 ¡Felicitaciones! Has asegurado el Nodo Central Seguro.\n");
            consola.appendText("Escribe 'salir' para terminar o 'volver' para explorar más.\n\n");
            mode = Mode.COMANDO;
            return;
        }

        if (actual.getIzquierda() == null && actual.getDerecha() == null) {
            consola.appendText("\n🔒 Este nodo está aislado. No hay rutas desde aquí.\n");
            consola.appendText("Escribe 'volver' para regresar al nodo anterior o 'salir' para terminar.\n\n");
            mode = Mode.COMANDO;
            return;
        }

        lanzarPreguntaLocal(true); // Lanza una nueva pregunta
    }

    private void lanzarPreguntaLocal(boolean nuevaPregunta) {
        try {
            if (nuevaPregunta) {
                preguntaActual = bancoPreguntas.obtenerPreguntaAleatoria();
            }

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
        Map<Integer, List<Nodo>> depthMap = new HashMap<>();
        int maxDepth = fillDepthMap(raiz, 0, depthMap);
        Map<Nodo, Integer> inorderIndex = new HashMap<>();
        int[] counter = {0};
        inorderAssign(raiz, inorderIndex, counter);

        int maxIndex = counter[0] == 0 ? 1 : counter[0];
        for (Map.Entry<Nodo, Integer> e : inorderIndex.entrySet()) {
            Nodo node = e.getKey();
            int idx = e.getValue();
            int depth = depthOfNode(raiz, node, 0);
            double x = 20 + ((double) idx / (maxIndex)) * (canvasWidth - 40);
            double y = 20 + ((double) depth / (Math.max(1, maxDepth))) * (canvasHeight - 40);
            nodePositions.put(node, new Point2D(x, y));
        }
    }

    private int fillDepthMap(Nodo node, int depth, Map<Integer, List<Nodo>> depthMap) {
        if (node == null) {
            return depth - 1;
        }
        depthMap.computeIfAbsent(depth, k -> new ArrayList<>()).add(node);
        int leftMax = fillDepthMap(node.getIzquierda(), depth + 1, depthMap);
        int rightMax = fillDepthMap(node.getDerecha(), depth + 1, depthMap);
        return Math.max(depth, Math.max(leftMax, rightMax));
    }

    private void inorderAssign(Nodo node, Map<Nodo, Integer> outIndex, int[] counter) {
        if (node == null) {
            return;
        }
        inorderAssign(node.getIzquierda(), outIndex, counter);
        outIndex.put(node, counter[0]++);
        inorderAssign(node.getDerecha(), outIndex, counter);
    }

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
        gc.clearRect(0, 0, canvasWidth, canvasHeight); // Usa clearRect para transparencias

        gc.setStroke(Color.web("#003300", 0.5));
        for (int i = 0; i < canvasWidth; i += 20) {
            gc.strokeLine(i, 0, i, canvasHeight);
        }
        for (int i = 0; i < canvasHeight; i += 20) {
            gc.strokeLine(0, i, canvasWidth, i);
        }

        gc.setLineWidth(1.5);
        for (Map.Entry<Nodo, Point2D> entry : nodePositions.entrySet()) {
            Nodo node = entry.getKey();
            Point2D p = entry.getValue();
            if (node.getIzquierda() != null) {
                Point2D q = nodePositions.get(node.getIzquierda());
                if (q != null) {
                    gc.setStroke(Color.web("#FF3333", 0.9)); // Conexiones rojas
                    gc.strokeLine(p.getX(), p.getY(), q.getX(), q.getY());
                }
            }
            if (node.getDerecha() != null) {
                Point2D q = nodePositions.get(node.getDerecha());
                if (q != null) {
                    gc.setStroke(Color.web("#00FF66", 0.9)); // Conexiones verdes
                    gc.strokeLine(p.getX(), p.getY(), q.getX(), q.getY());
                }
            }
        }

        double nodeRadius = 6;
        for (Map.Entry<Nodo, Point2D> entry : nodePositions.entrySet()) {
            Nodo node = entry.getKey();
            Point2D p = entry.getValue();
            String name = node.getNodo() == null ? "" : node.getNodo().toLowerCase();

            Color fill = Color.web("#00FF66"); // Verde por defecto
            if (name.contains("comprometido") || name.contains("infectado")) {
                fill = Color.web("#FF3333");
            } else if (name.contains("central seguro")) {
                fill = Color.web("#FFD166");
            }

            gc.setFill(fill);
            gc.fillOval(p.getX() - nodeRadius, p.getY() - nodeRadius, nodeRadius * 2, nodeRadius * 2);
        }

        if (actual != null && nodePositions.containsKey(actual)) {
            Point2D p = nodePositions.get(actual);
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(2);
            double highlightRadius = nodeRadius + 3;
            gc.strokeOval(p.getX() - highlightRadius, p.getY() - highlightRadius, highlightRadius * 2, highlightRadius * 2);
        }
    }
}
