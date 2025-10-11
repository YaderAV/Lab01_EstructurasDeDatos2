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
            }.getType(); /* se guarda el tipo de la lista para que posteriormente 
            la liberia sea capaz de reconocer el tipo de objeto que quiere guardar 
            e identifique todos sus atributos*/

            // Cargar archivo desde la ruta enviada desde la llamada del método
           InputStream input = getClass().getClassLoader().getResourceAsStream(nombreArchivo);
           /* traducimos la secuencia de bytes del json a texto para que sea legible por la libreria*/
            if (input == null) {
                throw new IllegalArgumentException("Archivo no encontrado: " + nombreArchivo);
            }
            
            InputStreamReader reader = new InputStreamReader(input); /* le pasamos la secuencia de carácteres
            al reader para que los interprete*/
            nodos = gson.fromJson(reader, listType); /*guardamos en la lista de nodos la información de los objetos 
            obtenida desde el archivo*/
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Nodo> getNodos() {
        return nodos;
    }
    
    

}
