/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author A. Vega
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    private void switchSceneWithFade(Stage stage, Parent newRoot) {
    Scene scene = stage.getScene();
    Parent oldRoot = scene.getRoot();

    // Fade out current scene
    FadeTransition fadeOut = new FadeTransition(Duration.millis(800), oldRoot);
    fadeOut.setFromValue(1.0);
    fadeOut.setToValue(0.0);

    fadeOut.setOnFinished(event -> {
        // Replace root and fade in the new scene
        scene.setRoot(newRoot);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), newRoot);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    });

    fadeOut.play();
}

    

    @Override
    public void start(Stage stage) throws Exception {
        Button bt = new Button("Click me");
        Button exit = new Button("Exit");
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #020b22, #0a122f);");
        Text title = new Text("CYBER SECURITY");
        title.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/Forresten.otf"), 80));
        title.setFill(Color.web("#FFFFFF"));
        title.setEffect(new DropShadow(30, Color.web("#00FFFF"))); // Neon glow
        Rectangle frame = new Rectangle(800, 150);
        frame.setArcWidth(10);
        frame.setArcHeight(10);
        frame.setFill(Color.web("#00000020")); // Transparent black
        frame.setStroke(Color.web("#FFFFFF40"));
        frame.setStrokeWidth(2);

        ImageView arrowIcon = new ImageView(new javafx.scene.image.Image(getClass().getResourceAsStream("/resources/White arrow.png")));
        arrowIcon.setFitWidth(70);
        arrowIcon.setFitHeight(40);

        Button arrowButton = new Button();
        arrowButton.setGraphic(arrowIcon);
        arrowButton.setStyle("""
    -fx-background-color: transparent;
    -fx-cursor: hand;""");



        AnchorPane overlay = new AnchorPane(arrowButton);
        AnchorPane.setTopAnchor(arrowButton, 20.0);
        AnchorPane.setRightAnchor(arrowButton, 20.0);
        root.getChildren().add(overlay);

        StackPane box = new StackPane(frame, title);
        Label brand = new Label("THYNK UNLIMITED");
        brand.setTextFill(Color.web("#FFFFFF"));
        brand.setFont(Font.font("Poppins", FontWeight.BOLD, 18));

        VBox content = new VBox(20, brand, box);
        content.setAlignment(Pos.CENTER);
        root.getChildren().add(content);

        exit.setOnAction(e -> System.exit(0));
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                System.out.println(" ... ");
            }

        });
        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
        stage.setTitle("Ciberseguridad");
        stage.show();
    }

}
