/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Visuals.EscenaAboutUs;
import Visuals.EscenaAjustes;
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
        Scene about = EscenaAboutUs.crearEscena();
        Scene settings = EscenaAjustes.crearEscena();

        SceneManager.addScene("menu", menu);
        SceneManager.addScene("about", about);
        SceneManager.addScene("settings", settings);

        SceneManager.show("menu");
        stage.setTitle("Aventura Cibernética");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
