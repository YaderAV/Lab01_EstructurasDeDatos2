/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Visuals;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author A. Vega
 */
public class EscenaMenu {

    private static final String FONT_FAMILY = "Consolas";
    private static final String THEME_COLOR = "#00b8ff"; // Un cian brillante
    private static final Color THEME_COLOR_PAINT = Color.web(THEME_COLOR);

    public static Scene crearEscena() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 70%, #0d1a26, #04080a);");

        // --- SECCIÓN SUPERIOR ---
        HBox topSection = new HBox(30);
        root.setTop(topSection);

        // --- SECCIÓN CENTRAL ---
        HBox centerSection = new HBox(30);
        centerSection.setAlignment(Pos.CENTER);
        centerSection.getChildren().addAll(createMainMenuPanel(), createSideIconsPanel());
        root.setCenter(centerSection);

        // --- SECCIÓN DERECHA (para el reloj funcional) ---
        HBox rightSection = new HBox();
        rightSection.setAlignment(Pos.TOP_RIGHT);

        final Text clockText = new Text(); // Creamos el nodo de texto para el reloj
        clockText.setFont(Font.font(FONT_FAMILY, 24));
        clockText.setFill(THEME_COLOR_PAINT);

        // Timeline para actualizar el reloj cada segundo
        Timeline clockTimeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // Formato con segundos
            clockText.setText(LocalTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));

        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();

        rightSection.getChildren().add(clockText);
        root.setRight(rightSection);

        return new Scene(root, 1280, 720);
    }

    /**
     * Crea el panel del menú principal
     */
    private static VBox createMainMenuPanel() {
        VBox panel = new VBox(5);
        panel.setPadding(new Insets(15));
        panel.setPrefWidth(350);

        Label startLabel = new Label("START");
        startLabel.setFont(Font.font(FONT_FAMILY, 16));
        startLabel.setTextFill(Color.WHITE);
        startLabel.setPadding(new Insets(0, 0, 10, 5));

        Button btnAdmin = createMenuButton("ABOUT US", FontAwesomeIcon.USER_SECRET);
        Button btnMonitor = createMenuButton("INICIAR SISTEMA", FontAwesomeIcon.DESKTOP);
        Button btnSettings = createMenuButton("SETTINGS", FontAwesomeIcon.SLIDERS);
        Button btnShutdown = createMenuButton("SHUTDOWN", FontAwesomeIcon.POWER_OFF);

        btnMonitor.setOnAction(e -> {
            Scene juego = new EscenaPrincipal().crearEscena();
            SceneManager.addScene("juego", juego);
            SceneManager.show("juego");
        });
        btnAdmin.setOnAction(e -> SceneManager.show("about"));
        btnSettings.setOnAction(e -> SceneManager.show("settings"));
        btnShutdown.setOnAction(e -> {
            Stage stage = SceneManager.getStage();
            if (stage != null) {
                stage.close();
            }
        });

        panel.getChildren().addAll(startLabel, btnAdmin, btnMonitor, btnSettings, btnShutdown);
        applyPanelStyle(panel);

        return panel;
    }

    /**
     * Crea el panel lateral con íconos grandes
     */
    private static VBox createSideIconsPanel() {
        VBox panel = new VBox(20);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(20));

        Text dateText = new Text("OCT 26, 2024");
        dateText.setFont(Font.font(FONT_FAMILY, 16));
        dateText.setFill(THEME_COLOR_PAINT);
        dateText.setTextAlignment(TextAlignment.CENTER);

        Button calendarButton = createIconButton(FontAwesomeIcon.CALENDAR);
        Button wifiButton = createIconButton(FontAwesomeIcon.WIFI);
        Button settingsButton1 = createIconButton(FontAwesomeIcon.COG);

        VBox dateBox = new VBox(5, calendarButton, dateText);
        dateBox.setAlignment(Pos.CENTER);

        panel.getChildren().addAll(dateBox, wifiButton, settingsButton1);
        return panel;
    }

    /**
     * Helper para crear un botón del menú con ícono y texto
     */
    private static Button createMenuButton(String text, FontAwesomeIcon icon) {
        FontAwesomeIconView iconView = new FontAwesomeIconView(icon);
        iconView.setSize("22px");
        iconView.setFill(THEME_COLOR_PAINT);

        Button button = new Button(text);
        button.setGraphic(iconView);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setContentDisplay(ContentDisplay.LEFT);
        button.setGraphicTextGap(15);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setFont(Font.font(FONT_FAMILY, 18));

        String style = """
            -fx-background-color: transparent;
            -fx-text-fill: %s;
            -fx-padding: 10 15;
            -fx-border-color: transparent;
            -fx-border-width: 0 0 0 3;
            -fx-cursor: hand;
        """.formatted(THEME_COLOR);

        String hoverStyle = style + "-fx-background-color: rgba(0, 184, 255, 0.2); -fx-border-color: %s;".formatted(THEME_COLOR);

        button.setStyle(style);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(style));

        return button;
    }

    /**
     * Helper para crear un botón solo con un ícono grande
     */
    private static Button createIconButton(FontAwesomeIcon icon) {
        FontAwesomeIconView iconView = new FontAwesomeIconView(icon);
        iconView.setSize("48px");
        iconView.setFill(THEME_COLOR_PAINT);

        Button button = new Button();
        button.setGraphic(iconView);

        String style = """
            -fx-background-color: rgba(10, 40, 50, 0.5);
            -fx-border-color: %s;
            -fx-border-width: 1;
            -fx-border-radius: 50;
            -fx-background-radius: 50;
            -fx-padding: 20;
            -fx-cursor: hand;
        """.formatted(THEME_COLOR);
        String hoverStyle = style + "-fx-effect: dropshadow(gaussian, %s, 20, 0.6, 0, 0);".formatted(THEME_COLOR);

        button.setStyle(style);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(style));

        return button;
    }

    /**
     * Aplica un estilo base a los paneles contenedores
     */
    private static void applyPanelStyle(Region region) {
        String style = """
            -fx-background-color: rgba(10, 40, 50, 0.4);
            -fx-border-color: %s;
            -fx-border-width: 1;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
        """.formatted(THEME_COLOR);
        region.setStyle(style);
    }
}
