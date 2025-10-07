/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Visuals;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author A. Vega
 */
public class SceneManager {

    private final Stage stage;
    private final StackPane root;
    private final ImageView background;

    public SceneManager(Stage stage) {
        this.stage = stage;
        this.background = new ImageView();
        this.background.setFitWidth(1280);
        this.background.setFitHeight(720);
        this.background.setPreserveRatio(false);

        this.root = new StackPane(background);
        stage.setScene(new Scene(root, 1280, 720));
    }

    public void setInitialScene() {
        
    }

}
