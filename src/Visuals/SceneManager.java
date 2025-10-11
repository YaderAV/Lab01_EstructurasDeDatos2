/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Visuals;

import javafx.scene.media.AudioClip;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author A. Vega
 */
public class SceneManager {

    private static Stage mainStage;
    private static final Map<String, Scene> scenes = new HashMap<>();
    private static final Map<String, Runnable> onShowMap = new HashMap<>();

    private static final Duration FADE_DURATION = Duration.millis(500);
    private static final Duration FLASH_DURATION = Duration.millis(200);

    
;
    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    public static void addScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    //permite registrar una acción que se ejecutará justo después de mostrar la escena
    public static void addScene(String name, Scene scene, Runnable onShow) {
        scenes.put(name, scene);
        if (onShow != null) {
            onShowMap.put(name, onShow);
        }
    }

    public static void show(String name) {
         if (mainStage == null) return;

    Scene nextScene = scenes.get(name);
    if (nextScene == null) {
        System.err.println("Escena no encontrada: " + name);
        return;
    }

    // Si no hay escena actual, simplemente inicializa
    if (mainStage.getScene() == null) {
        mainStage.setScene(nextScene);
        fadeIn(nextScene, () -> runOnShow(name));
        reproducirSonido();
        return;
    }

    Scene currentScene = mainStage.getScene();

    // --- Transición de salida ---
    FadeTransition fadeOut = new FadeTransition(FADE_DURATION, currentScene.getRoot());
    fadeOut.setFromValue(1.0);
    fadeOut.setToValue(0.0);

    fadeOut.setOnFinished(e -> {
        reproducirSonido();

        // --- Capa de flash (efecto verde rápido) ---
        StackPane flashLayer = new StackPane();
        flashLayer.setStyle("-fx-background-color: #00FF66;");
        flashLayer.setMouseTransparent(true);
        flashLayer.setOpacity(0);

        // --- Crea un contenedor intermedio sin asumir tipo ---
        Parent nextRoot = nextScene.getRoot();
        StackPane wrapper = new StackPane();
        wrapper.getChildren().addAll(nextRoot, flashLayer);

        Scene transitionScene = new Scene(
            wrapper,
            nextScene.getWidth() > 0 ? nextScene.getWidth() : mainStage.getWidth(),
            nextScene.getHeight() > 0 ? nextScene.getHeight() : mainStage.getHeight(),
            Color.BLACK
        );

        mainStage.setScene(transitionScene);

        // --- Efecto flash ---
        FadeTransition flash = new FadeTransition(FLASH_DURATION, flashLayer);
        flash.setFromValue(0.0);
        flash.setToValue(1.0);
        flash.setAutoReverse(true);
        flash.setCycleCount(2);

        flash.setOnFinished(ev -> fadeIn(transitionScene, () -> runOnShow(name)));
        flash.play();
    });

    fadeOut.play();
    }

    private static void fadeIn(Scene scene, Runnable after) {
        FadeTransition fadeIn = new FadeTransition(FADE_DURATION, scene.getRoot());
        scene.getRoot().setOpacity(0);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setOnFinished(e -> {
            if (after != null) {
                after.run();
            }
        });
        fadeIn.play();
    }

    private static void runOnShow(String name) {
        Runnable r = onShowMap.get(name);
        if (r != null) {
            try {
                r.run();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

   private static void reproducirSonido() {
    try {
        URL soundUrl = SceneManager.class.getResource("/resources/transition.wav"); 

        if (soundUrl != null) {
            AudioClip clip = new AudioClip(soundUrl.toString());
            clip.setVolume(0.6);
            clip.play();
        } else {
            System.err.println("No se encontró el sonido de transición (resources/transition.wav)");
        }
    } catch (Exception e) {
        System.err.println("Error al reproducir sonido de transición: " + e.getMessage());
    }
}

    public static Stage getStage() {
        return mainStage;
    }
}
