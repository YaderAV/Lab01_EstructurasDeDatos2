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
public class BancoNodos {

    List<Nodo> nodos;

    public BancoNodos(String nombreArchivo) {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Nodo>>() {
            }.getType();

            // Cargar archivo desde resources
           InputStream input = getClass().getClassLoader().getResourceAsStream(nombreArchivo);
            if (input == null) {
                throw new IllegalArgumentException("Archivo no encontrado: " + nombreArchivo);
            }

            InputStreamReader reader = new InputStreamReader(input);
            nodos = gson.fromJson(reader, listType);
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Nodo> getNodos() {
        return nodos;
    }
    
    

}
