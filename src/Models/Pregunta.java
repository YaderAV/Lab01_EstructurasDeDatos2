/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.List;

/**
 *
 * @author USUARIO
 */
public class Pregunta {
    private String pregunta;
    private List<String> opciones; 
    private String respuesta; 

    public String getPregunta() {
        return pregunta;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public void setOpciones(List<String> opciones) {
        this.opciones = opciones;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

  
     @Override
    public String toString() {
        return pregunta + " " + opciones.toString();
    }
    
    
}
