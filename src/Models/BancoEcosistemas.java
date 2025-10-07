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
public class BancoEcosistemas {

    List<Ecosistema> ecosistemas;

    public BancoEcosistemas(String nombreArchivo) {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Ecosistema>>() {
            }.getType();

            // Cargar archivo desde resources
           InputStream input = getClass().getClassLoader().getResourceAsStream(nombreArchivo);
            if (input == null) {
                throw new IllegalArgumentException("Archivo no encontrado: " + nombreArchivo);
            }

            InputStreamReader reader = new InputStreamReader(input);
            ecosistemas = gson.fromJson(reader, listType);
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Ecosistema obtenerEcosistemaAleatorio() {
        if (ecosistemas == null || ecosistemas.isEmpty()) {
            throw new IllegalStateException("No hay preguntas disponibles. Revisa el JSON.");
        }
        int idx = (int) (Math.random() * ecosistemas.size());
        return ecosistemas.get(idx);
    }

    public List<Ecosistema> getEcosistemas() {
        return ecosistemas;
    }

}
