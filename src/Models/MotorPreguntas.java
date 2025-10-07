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
public class MotorPreguntas {
    private BancoPreguntas banco;
 
    public MotorPreguntas(BancoPreguntas banco) {
        this.banco = banco;
    }

    public boolean lanzarPregunta() {
        Pregunta p = banco.obtenerPreguntaAleatoria();

        String opciones = String.join(" / ", p.getOpciones());
        String respuestaJugador = JOptionPane.showInputDialog(
            p.getPregunta() + "\nOpciones: " + opciones
        );

        if (respuestaJugador == null) return false; // si cancela o cierra

        if (respuestaJugador.trim().equalsIgnoreCase(p.getRespuesta())) {
            JOptionPane.showMessageDialog(null, "¡Correcto!");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Incorrecto, intenta otra vez.");
            return false;
        }
    }
}
