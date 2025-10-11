/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 *
 * @author A. Vega
 */
public class BancoPreguntas {

    private List<Pregunta> preguntas;

    public BancoPreguntas(String nombreArchivo) {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Pregunta>>() {
            }.getType();

            // misma lógica de la clase BancoNodos
           InputStream input = getClass().getClassLoader().getResourceAsStream(nombreArchivo);
            if (input == null) {
                throw new IllegalArgumentException("Archivo no encontrado: " + nombreArchivo);
            }

            InputStreamReader reader = new InputStreamReader(input);
            preguntas = gson.fromJson(reader, listType);
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Pregunta obtenerPreguntaAleatoria() {
        if (preguntas == null || preguntas.isEmpty()) {
            throw new IllegalStateException("No hay preguntas disponibles. Revisa el JSON.");
        }
        int idx = (int) (Math.random() * preguntas.size()); /* generamos un indice "aleatorio" con el accederemos
        a una pregunta de la lista de preguntas*/
        return preguntas.get(idx);
    }
    
  
}
