/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.List;
import java.util.Random;

/**
 *
 * @author A. Vega
 */
public class GeneradorArbol {
    
    private Random random = new Random();
    private Arbol arbol;
    public Arbol construirArbol(List<Nodo> ecosistemas) {
        if (ecosistemas == null || ecosistemas.isEmpty()) {
            return null;
        }
        //Recorre la lista, el nodo raíz siempre es el mismo. 
        Nodo raiz = new Nodo(ecosistemas.get(0).getNodo());
        arbol = new Arbol(raiz);
        for (int i = 1; i < ecosistemas.size(); i++) {
            insertar(raiz, new Nodo(ecosistemas.get(i).getNodo())); // llamado recursivo para ubicar los nodos
        }

        return arbol; 
    }

    private void insertar(Nodo actual, Nodo nuevo) {
        // Aleatoriamente decide izquierda o derecha

        // Si el árbol está vacío (caso base)
        if (actual == null) {
            actual = nuevo;
            return;
        }
        // Decide aleatoriamente izquierda o derecha
        if (random.nextBoolean()) {
            // Insertar a la izquierda
            if (actual.getIzquierda() == null) {
                actual.setIzquierda(nuevo);
                nuevo.setPadre(actual); // asignamos cual es su nodo padre para saber a donde volver. 
            } else {
                insertar(actual.getIzquierda(), nuevo);
            }
        } else {
            // Insertar a la derecha
            if (actual.getDerecha() == null) {
                actual.setDerecha(nuevo);
                nuevo.setPadre(actual);
            } else {
                insertar(actual.getDerecha(), nuevo);
            }
        }

    }
}
