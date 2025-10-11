/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Visuals;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author A. Vega
 */
public class EscenaAboutUs {
     private static final String FONT_FAMILY = "Consolas";
    private static final String THEME_COLOR = "#00b8ff";
    private static final Color THEME_COLOR_PAINT = Color.web(THEME_COLOR);

    public static Scene crearEscena() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 70%, #0d1a26, #04080a);");

        // --- CONTENIDO CENTRAL ---
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));

        Label title = new Label("ABOUT US");
        title.setFont(Font.font(FONT_FAMILY, 30));
        title.setTextFill(THEME_COLOR_PAINT);

        Text description = new Text("""
                Este proyecto fue desarrollado como parte del curso de Estructuras de Datos II.
                Simula un ataque cibernético en el que los jugadores deben restaurar la seguridad
                de una red comprometida navegando entre nodos críticos.
                
                Integrantes:
                - Integrante 1
                - Integrante 2
                - Integrante 3
                - Integrante 4
                
                Universidad XYZ - 2025
        """);
        description.setFont(Font.font(FONT_FAMILY, 16));
        description.setFill(Color.WHITE);
        description.setWrappingWidth(800);

        Button backButton = createButton("VOLVER AL MENÚ", FontAwesomeIcon.ARROW_LEFT);
        
        
        backButton.setOnAction(e -> {
        Scene menu = new EscenaMenu().crearEscena();
            SceneManager.addScene("menu", menu);
            SceneManager.show("menu");
        });

        content.getChildren().addAll(title, description, backButton);
        applyPanelStyle(content);
        root.setCenter(content);

        return new Scene(root, 1280, 720);
    }

    private static Button createButton(String text, FontAwesomeIcon icon) {
        FontAwesomeIconView iconView = new FontAwesomeIconView(icon);
        iconView.setSize("18px");
        iconView.setFill(THEME_COLOR_PAINT);

        Button button = new Button(text, iconView);
        button.setFont(Font.font(FONT_FAMILY, 18));
        button.setTextFill(THEME_COLOR_PAINT);
        button.setContentDisplay(ContentDisplay.LEFT);
        button.setAlignment(Pos.CENTER);
        button.setPadding(new Insets(10, 20, 10, 20));
        button.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: %s;
            -fx-border-width: 1;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            -fx-cursor: hand;
        """.formatted(THEME_COLOR));

        String hoverStyle = """
            -fx-background-color: rgba(0, 184, 255, 0.2);
            -fx-border-color: %s;
            -fx-text-fill: white;
        """.formatted(THEME_COLOR);

        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: %s;
            -fx-border-width: 1;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            -fx-cursor: hand;
        """.formatted(THEME_COLOR)));

        return button;
    }

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
