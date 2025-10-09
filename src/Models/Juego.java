/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import javax.swing.JOptionPane;

/**
 *
 * @author A. Vega
 */
public class Juego {
    private Nodo actual;
     private MotorPreguntas motor;

    public Juego(Nodo raiz, MotorPreguntas motor) {
        this.actual = raiz;
        this.motor = motor;
    }

    public void iniciar() {
        JOptionPane.showMessageDialog(null, "¡Bienvenido a la Aventura Climática!");

        while (true) {
            // Mensaje del ecosistema
            JOptionPane.showMessageDialog(null, "Estás en: " + actual.getNodo());

            // Si ya es nodo de sostenibilidad → victoria
            if (actual.getNodo().equals("Nodo de Sostenibilidad")) {
                JOptionPane.showMessageDialog(null,
                        "¡Felicitaciones! Has aprendido sobre el cambio climático y cómo ayudar a salvar el planeta.");
                break;
            }

            // Lanzar preguntas hasta que acierte
            boolean acerto = false;
            while (!acerto) {
                acerto = motor.lanzarPregunta();
            }

            // Elegir camino
            String eleccion = JOptionPane.showInputDialog("Elige: izquierda / derecha (o escribe salir)");

            if (eleccion == null || eleccion.equalsIgnoreCase("salir")) {
                JOptionPane.showMessageDialog(null, "Has salido del juego.");
                break;
            }

            if (eleccion.equalsIgnoreCase("izquierda") && actual.getIzquierda() != null) {
                actual = actual.getIzquierda();
            } else if (eleccion.equalsIgnoreCase("derecha") && actual.getDerecha() != null) {
                actual = actual.getDerecha();
            } else {
                JOptionPane.showMessageDialog(null, "Camino inválido. Intenta otra vez.");
            }
        }
    }
}
