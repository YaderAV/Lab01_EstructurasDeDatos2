/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Visuals.EscenaPrincipal;
import Visuals.EscenaMenu;
import Visuals.SceneManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author A. Vega
 */
public class Main extends Application {

    @Override
public void start(Stage stage) {
    SceneManager.setStage(stage);

    Scene menu = EscenaMenu.crearEscena();
    Scene juego = new EscenaPrincipal().crearEscena();

    SceneManager.addScene("menu", menu);
    SceneManager.addScene("juego", juego);

    SceneManager.show("menu");
    stage.setTitle("Aventura Cibernética");
    stage.show();
}

    public static void main(String[] args) {
        launch(args);
    }
}
