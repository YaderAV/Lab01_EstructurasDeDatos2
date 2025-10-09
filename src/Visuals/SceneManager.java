/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Visuals;

import javafx.scene.media.AudioClip;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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
    private static final Duration FADE_DURATION = Duration.millis(500);
    private static final Duration FLASH_DURATION = Duration.millis(200);

    // Ruta al sonido del efecto
    private static final String SOUND_PATH = "C:\\Users\\USUARIO\\Documents\\NetBeansProjects\\Lab01_EstructurasDeDatos2\\src\\resources\\transition.wav"; 

    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    public static void addScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    public static void show(String name) {
        if (mainStage == null) return;
        Scene nextScene = scenes.get(name);
        if (nextScene == null) {
            System.err.println("❌ Escena no encontrada: " + name);
            return;
        }

        // Si no hay escena previa, mostrar directamente con fade-in
        if (mainStage.getScene() == null) {
            mainStage.setScene(nextScene);
            fadeIn(nextScene);
            reproducirSonido();
            return;
        }

        // Guardar escena actual
        Scene currentScene = mainStage.getScene();

        // Fade out de la escena actual
        FadeTransition fadeOut = new FadeTransition(FADE_DURATION, currentScene.getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            // 🔊 reproducir sonido
            reproducirSonido();

            // 🔆 flash verde estilo CRT
            StackPane flashLayer = new StackPane();
            flashLayer.setStyle("-fx-background-color: #00FF66;");
            flashLayer.setOpacity(0);

            Pane nextRoot = (Pane) nextScene.getRoot();
            StackPane wrapper = new StackPane(nextRoot, flashLayer);
            Scene transitionScene = new Scene(wrapper, nextScene.getWidth(), nextScene.getHeight(), Color.BLACK);
            mainStage.setScene(transitionScene);

            // Animación del flash verde
            FadeTransition flash = new FadeTransition(FLASH_DURATION, flashLayer);
            flash.setFromValue(0.0);
            flash.setToValue(1.0);
            flash.setAutoReverse(true);
            flash.setCycleCount(2);

            flash.setOnFinished(ev -> fadeIn(transitionScene));
            flash.play();
        });

        fadeOut.play();
    }

    private static void fadeIn(Scene scene) {
        FadeTransition fadeIn = new FadeTransition(FADE_DURATION, scene.getRoot());
        scene.getRoot().setOpacity(0);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    private static void reproducirSonido() {
        try {
            File file = new File(SOUND_PATH);
            if (file.exists()) {
                AudioClip clip = new AudioClip(file.toURI().toString());
                clip.setVolume(0.6); // volumen entre 0.0 y 1.0
                clip.play();
            } else {
                System.err.println("⚠️ No se encontró el sonido de transición en: " + SOUND_PATH);
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al reproducir sonido de transición: " + e.getMessage());
        }
    }

    public static Stage getStage() {
        return mainStage;
    }
}
