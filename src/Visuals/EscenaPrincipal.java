/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Visuals;

import Models.Arbol;
import Models.BancoNodos;
import Models.BancoPreguntas;
import Models.GeneradorArbol;
import Models.Nodo;
import Models.Pregunta;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

/**
 *
 * @author A. Vega
 */
public class EscenaPrincipal {

    private TextArea consola;
    private TextField input;
    private Canvas miniMapa;
    private StackPane miniWrapper;
    private GraphicsContext gc;

    // cantidad máxima de líneas visibles antes de limpiar
    private static final int lineasMaximasConsola = 19;
    private static final int BUFFER_LINEAS_RECIENTES = 10; // Líneas a conservar tras limpiar

    // Lógica del juego
    private final Queue<String> colaMensajes = new LinkedList<>();
    private boolean escribiendo = false;
    private BancoPreguntas bancoPreguntas;
    private BancoNodos bancoN;
    private Arbol arbol;
    private Nodo actual;
    private Pregunta preguntaActual;
    private boolean juegoIniciado = false;
    private ScrollBar verticalScrollBar;

    // Sonido de tecleo
    private AudioClip typeSound;

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
        consola.setScrollTop(Double.MAX_VALUE);
        consola.setPrefRowCount(10);
        consola.setStyle("""
    -fx-control-inner-background: #0A0A0A;
    -fx-text-fill: #00FF66;
    -fx-background-color: transparent;
    -fx-border-color: transparent;
    -fx-focus-color: transparent;
    -fx-faint-focus-color: transparent;
""");
        consola.textProperty().addListener((obs, oldVal, newVal)
                -> consola.setScrollTop(Double.MAX_VALUE)
        );

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
        miniWrapper = new StackPane(miniMapa);
        miniWrapper.setPadding(new Insets(8));
        miniWrapper.setLayoutX(canvasWidth);
        miniWrapper.setLayoutY(canvasWidth);
        miniWrapper.setVisible(false);

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
        consoleWindow.setMaxSize(700, 550);
        consoleWindow.setStyle("""
        -fx-background-color: rgba(10, 10, 10, 0.9);
        -fx-border-width: 10;
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
        root.getChildren().addAll(consoleWindow, miniWrapper);
        StackPane.setAlignment(consoleWindow, Pos.CENTER);
        StackPane.setAlignment(miniWrapper, Pos.TOP_RIGHT);
        StackPane.setMargin(miniWrapper, new Insets(2));

        // --- Cargar sonido ---
        try {
            URL soundUrl = getClass().getResource("/resources/type.wav"); // desde src/resources
            if (soundUrl != null) {
                typeSound = new AudioClip(soundUrl.toString());
                typeSound.setVolume(0.05);
            } else {
                System.out.println("No se encontró el archivo type.wav");
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar el sonido de tecleo: " + e.getMessage());
        }

        // --- Cargar imagen de fondo tipo escritorio ---
        try {
            // Carga la imagen desde el classpath (src/resources)
            Image backgroundImg = new Image(
                    getClass().getResourceAsStream("/resources/desktop_background.jpg")
            );

            BackgroundImage backgroundImage = new BackgroundImage(
                    backgroundImg,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(
                            BackgroundSize.AUTO,
                            BackgroundSize.AUTO,
                            false, false, true, true
                    )
            );

            root.setBackground(new Background(backgroundImage));

        } catch (Exception e) {
            System.out.println("[ERROR] No se pudo cargar la imagen de fondo. Usando color negro.");
            root.setStyle("-fx-background-color: black;");
        }

        // --- Crear escena ---
        Scene scene = new Scene(root, 1024, 600);

        inicializarJuego();
        return scene;
    }

    private void escribirConsola(String texto, double velocidadMs) {
        if (texto == null || texto.isEmpty()) {
            return;
        }

        colaMensajes.offer(texto);
        if (!escribiendo) {
            procesarCola(velocidadMs);
        }
    }

    private void procesarCola(double velocidadMs) {
        if (colaMensajes.isEmpty()) {
            escribiendo = false;
            return;
        }

        escribiendo = true;
        String texto = colaMensajes.poll();

        Timeline timeline = new Timeline();
        final int[] idx = {0};

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(velocidadMs), e -> {
            if (idx[0] < texto.length()) {
                char c = texto.charAt(idx[0]);

                // --- Antes de agregar texto: limpieza inteligente ---
                if (contarLineasConsola() == lineasMaximasConsola) {
                    limpiarConBuffer();
                }

                consola.appendText(String.valueOf(c));

                // --- Sonido de tecleo ---
                if (typeSound != null && !Character.isWhitespace(c)) {
                    typeSound.play();
                }

                idx[0]++;
            }
        }));

        timeline.setCycleCount(texto.length());
        timeline.setOnFinished(e -> {
            PauseTransition pausa = new PauseTransition(Duration.millis(80));
            pausa.setOnFinished(ev -> procesarCola(velocidadMs));
            pausa.play();
        });

        timeline.play();
    }

    /**
     * Limpia solo el texto más viejo, conservando las últimas líneas
     * importantes.
     */
    private void limpiarConBuffer() {
        String[] lineas = consola.getText().split("\n");
        if (lineas.length <= BUFFER_LINEAS_RECIENTES) {
            return;
        }
        StringBuilder nuevoTexto = new StringBuilder("");
        for (int i = Math.max(0, lineas.length - BUFFER_LINEAS_RECIENTES); i < lineas.length; i++) {
            nuevoTexto.append(lineas[i]).append("\n");
        }
        consola.clear();
        consola.appendText(nuevoTexto.toString());

        consola.clear();
    }

    /**
     * Cuenta las líneas actuales en la consola
     */
    private int contarLineasConsola() {
        return consola.getText().split("\n").length;
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void inicializarJuego() {
        try {
            bancoN = new BancoNodos("resources/nodos.json");
            bancoPreguntas = new BancoPreguntas("resources/preguntas.json");

            GeneradorArbol gen = new GeneradorArbol();
            List<Nodo> lista = bancoN.getNodos();

            arbol = gen.construirArbol(lista);
            actual = arbol.getRaiz();

            if (lista.size() > 1) {
                Nodo ultimo = lista.remove(lista.size() - 1);
                lista.add(ultimo); // lo mantenemos al final, para colocarlo al fondo
            }

            bienvenida();
            calcularPosicionesArbol();
            mostrarNodoActualYPreguntar();

        } catch (Exception ex) {
            escribirConsola("[ERROR] No fue posible cargar recursos: " + ex.getMessage() + "\n", 50.0);
        }
    }

    public void bienvenida() {
        String mensaje = """       
        *** INICIANDO SIMULACIÓN DE INCIDENTE CIBERNÉTICO ***
        Año 2043. La corporación tecnológica NEONet ha sido víctima de un ciberataque masivo. 
        Un grupo de hackers desconocidos ha infiltrado la red principal, desplegando un malware 
        autoadaptativo capaz de comprometer servidores, interceptar datos y reescribir protocolos internos.
        Eres un Analista Forense Digital de la Unidad de Respuesta Inmediata (URI). 
        Tu misión es acceder al sistema comprometido, identificar los nodos afectados 
        y restaurar el control antes de que el malware alcance el núcleo de seguridad.                         
        Cada nodo representa un servidor o dispositivo dentro de la red corporativa. 
        Algunos están limpios, otros comprometidos, y unos pocos contienen rastros del atacante. 
        Tendrás que responder correctamente los desafíos de ciberseguridad para poder avanzar 
        y reestablecer el equilibrio en la red.
        Ten cuidado: las decisiones incorrectas pueden bloquear rutas o activar contramedidas.  
        El tiempo corre y la integridad del sistema se deteriora con cada segundo.
                                          
            > Ejecuta tus comandos con precisión.
            > Observa los patrones.
            > Protege el núcleo.
                         
                         *** SISTEMA: Acceso concedido... Conectando al entorno virtual... ***                 
        ¡Bienvenido a la Aventura Cibernética!
        Tu misión: recuperar el control de la red y detener el ataque.
        Comandos: 'izquierda', 'derecha', 'salir'. 
        Responde las preguntas para avanzar.

        """;
        escribirConsola(mensaje, 50); // velocidad de escritura (ms por caracter)
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
            escribirConsola("Saliendo... ¡Hasta la próxima!\n", 50);
            System.exit(0);
            return;
        }

        if (cmd.equals("izquierda") || cmd.equals("izq")) {
            if (actual.getIzquierda() != null) {
                actual = actual.getIzquierda();
                escribirConsola("-> Te desplazas a la izquierda.\n\n", 50);

                mostrarNodoActualYPreguntar();
            } else {
                escribirConsola("No hay nodo a la izquierda. Intenta otra opción.\n", 50);
            }
            return;
        }

        if (cmd.equals("derecha") || cmd.equals("der")) {
            if (actual.getDerecha() != null) {
                actual = actual.getDerecha();
                escribirConsola("-> Te desplazas a la derecha.\n\n", 50);

                mostrarNodoActualYPreguntar();
            } else {
                escribirConsola("No hay nodo a la derecha. Intenta otra opción.\n", 50);
            }
            return;
        }

        if (cmd.equals("volver")) {
            try {
                Nodo padre = actual.getPadre();
                if (padre != null) {
                    actual = padre;
                    escribirConsola("↩️  Regresaste al nodo anterior: " + actual.getNodo() + "\n\n", 50);

                    mostrarNodoActualYPreguntar();
                } else {
                    escribirConsola("No puedes volver, estás en la raíz.\n", 50);
                }
            } catch (Exception e) {
                escribirConsola("No se pudo ejecutar 'volver' (verifica que Nodo tenga getPadre()).\n", 50);
            }
            return;
        }

        escribirConsola("Comando no reconocido. Usa izquierda | derecha | volver | salir\n", 50);
    }

    private void comprobarRespuesta(String texto) {
        if (preguntaActual == null) {
            escribirConsola("No hay pregunta activa. Cambiando a modo comando.\n", 50);
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
            escribirConsola("¡Correcto! Ahora puedes elegir 'izquierda' o 'derecha', o volver.\n\n", 50);
            mode = Mode.COMANDO;
        } else {
            escribirConsola("Incorrecto. Intenta otra respuesta para este mismo reto.\n\n", 50);
            lanzarPreguntaLocal(true); // Vuelve a mostrar la misma pregunta
        }
    }

    // -------------------------
    // Mostrar nodo y lanzar pregunta o manejos especiales
    // -------------------------
    private void mostrarNodoActualYPreguntar() {
        if (actual == null) {
            escribirConsola("> Nodo actual: null\n", 50);
            mode = Mode.COMANDO;
            return;
        }

        String nombre = actual.getNodo() == null ? "" : actual.getNodo().toLowerCase();
        escribirConsola("> Estás en: " + nombre + "\n", 50);

        // --- Nodo comprometido ---
        if (nombre.contains("comprometido") || nombre.contains("infectado")
                || nombre.contains("infiltrada") || nombre.contains("malware")) {

            mostrarAlerta("⚠️ Nodo Comprometido",
                    "Has detectado un nodo comprometido.\nUtiliza tus conocimientos para restaurar la seguridad.",
                    AlertType.WARNING);

            miniWrapper.setVisible(true); // mostrar el mini-mapa
            dibujarMiniMapa();
        } // --- Nodo central seguro ---
        else if (nombre.contains("central seguro")) {
            miniWrapper.setVisible(false);
            mostrarAlerta("🎉 Victoria",
                    "¡Has asegurado el Nodo Central Seguro!\nLa red vuelve a estar protegida.",
                    AlertType.INFORMATION);
            mode = Mode.COMANDO;
            
            
            
            return;
        } // --- Nodo aislado ---
        else if (actual.getIzquierda() == null && actual.getDerecha() == null) {
            miniWrapper.setVisible(false);
            mostrarAlerta("Nodo Aislado",
                    "Este nodo está aislado. No hay rutas desde aquí.\nUsa 'volver' para retroceder.",
                    AlertType.INFORMATION);
            mode = Mode.COMANDO;
            return;
        } // --- Nodo normal ---
        else {
            miniWrapper.setVisible(false); //ocultar el mapa si no está comprometido
        }

        lanzarPreguntaLocal(true);
    }

    private void lanzarPreguntaLocal(boolean nuevaPregunta) {
        try {
            if (nuevaPregunta) {
                preguntaActual = bancoPreguntas.obtenerPreguntaAleatoria();
            }

            if (preguntaActual == null) {
                escribirConsola("[INFO] No hay preguntas disponibles. Avanza libremente.\n", 50);
                mode = Mode.COMANDO;
                return;
            }

            escribirConsola("\n--- Reto de Ciberseguridad ---\n", 50);
            escribirConsola(preguntaActual.getPregunta() + "\n", canvasWidth);
            List<String> opts = preguntaActual.getOpciones();
            if (opts != null && !opts.isEmpty()) {
                for (int i = 0; i < opts.size(); i++) {
                    escribirConsola(opts.get(i) + "\n", 50);
                }
                escribirConsola("Responde escribiendo la letra correspondiente a la opción.\n\n", 50);
            } else {
                escribirConsola("Responde escribiendo la respuesta.\n\n", 50);
            }
            mode = Mode.RESPONDIENDO;
        } catch (Exception e) {
            escribirConsola("[ERROR] No se pudo obtener pregunta: " + e.getMessage() + "\n", canvasWidth);
            mode = Mode.COMANDO;
        }
    }

    // -------------------------
    // Mini-mapa: calcular posiciones y dibujar
    // -------------------------
    private void calcularPosicionesArbol() {
        nodePositions.clear();
        Map<Integer, List<Nodo>> depthMap = new HashMap<>();
        int maxDepth = fillDepthMap(arbol.getRaiz(), 0, depthMap);
        Map<Nodo, Integer> inorderIndex = new HashMap<>();
        int[] counter = {0};
        inorderAssign(arbol.getRaiz(), inorderIndex, counter);

        int maxIndex = counter[0] == 0 ? 1 : counter[0];
        for (Map.Entry<Nodo, Integer> e : inorderIndex.entrySet()) {
            Nodo node = e.getKey();
            int idx = e.getValue();
            int depth = depthOfNode(arbol.getRaiz(), node, 0);
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
