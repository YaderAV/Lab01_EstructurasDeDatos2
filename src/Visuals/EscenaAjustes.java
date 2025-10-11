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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author A. Vega
 */
public class EscenaAjustes {
    
    private static final String FONT_FAMILY = "Consolas";
    private static final String THEME_COLOR = "#00b8ff";
    private static final Color THEME_COLOR_PAINT = Color.web(THEME_COLOR);

    public static Scene crearEscena() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 70%, #0d1a26, #04080a);");

        VBox box = new VBox(25);
        box.setAlignment(Pos.CENTER);

        Text title = new Text("SETTINGS");
        title.setFont(Font.font(FONT_FAMILY, 40));
        title.setFill(THEME_COLOR_PAINT);

        Button fullScreenBtn = crearBoton("TOGGLE FULLSCREEN", FontAwesomeIcon.DESKTOP);
        fullScreenBtn.setOnAction(e -> {
            Stage stage = SceneManager.getStage();
            if (stage != null) {
                stage.setFullScreen(!stage.isFullScreen());
            }
        });

        Button backBtn = crearBoton("BACK TO MENU", FontAwesomeIcon.ARROW_LEFT);
        backBtn.setOnAction(e -> {
            Scene menu = new EscenaMenu().crearEscena();
            SceneManager.addScene("menu", menu);
            SceneManager.show("menu");
        });

        box.getChildren().addAll(title, fullScreenBtn, backBtn);
        root.setCenter(box);

        return new Scene(root, 1280, 720);
    }

    private static Button crearBoton(String texto, FontAwesomeIcon icono) {
        FontAwesomeIconView iconView = new FontAwesomeIconView(icono);
        iconView.setSize("20px");
        iconView.setFill(THEME_COLOR_PAINT);

        Button btn = new Button(texto, iconView);
        btn.setFont(Font.font(FONT_FAMILY, 18));
        btn.setAlignment(Pos.CENTER);
        btn.setGraphicTextGap(10);

        String baseStyle = """
            -fx-background-color: transparent;
            -fx-border-color: %s;
            -fx-border-width: 1;
            -fx-text-fill: %s;
            -fx-padding: 10 20;
            -fx-cursor: hand;
        """.formatted(THEME_COLOR, THEME_COLOR);

        String hoverStyle = baseStyle + "-fx-background-color: rgba(0, 184, 255, 0.2);";

        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));

        return btn;
    }
}
