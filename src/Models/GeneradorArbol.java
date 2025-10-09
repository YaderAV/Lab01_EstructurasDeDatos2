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

    private BancoNodos ecosistemas;
    private Arbol arbol;
    private Random random = new Random();

    public Nodo construirArbol(List<Nodo> ecosistemas) {
        if (ecosistemas == null || ecosistemas.isEmpty()) {
            return null;
        }

        Nodo raiz = new Nodo(ecosistemas.get(0).getNodo());
        for (int i = 1; i < ecosistemas.size(); i++) {
            insertar(raiz, new Nodo(ecosistemas.get(i).getNodo()));
        }

        return raiz;
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
                nuevo.setPadre(actual);
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
