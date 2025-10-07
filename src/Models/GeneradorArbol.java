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

    private BancoEcosistemas ecosistemas;
    private Arbol arbol;
    private Random random = new Random();

    public Ecosistema construirArbol(List<Ecosistema> ecosistemas) {
        if (ecosistemas.isEmpty()) {
            return null;
        }

        // Tomamos el primero como raíz
        Ecosistema raiz = new Ecosistema(ecosistemas.get(0).getEcosistema());
        // Insertamos el resto aleatoriamente
        for (int i = 1; i < ecosistemas.size(); i++) {
            insertar(raiz, new Ecosistema(ecosistemas.get(i).getEcosistema()));
        }

        return raiz;
    }

    private void insertar(Ecosistema actual, Ecosistema nuevo) {
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
            } else {
                insertar(actual.getIzquierda(), nuevo);
            }
        } else {
            // Insertar a la derecha
            if (actual.getDerecha() == null) {
                actual.setDerecha(nuevo);
            } else {
                insertar(actual.getDerecha(), nuevo);
            }
        }

    }
}
