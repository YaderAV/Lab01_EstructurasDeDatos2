
package Models;

import Models.Ecosistema;

public class Arbol {
    private Ecosistema raiz;

    public Arbol() {
        this.raiz = null; 
    }
    
    public void imprimirArbol (Ecosistema nodo, String prefijo){
        if (nodo == null) {
        return;
    }

    // Imprime el nodo actual con indentación
    System.out.println(prefijo + "• " + nodo.getEcosistema());

    // Llama recursivamente aumentando la sangría
    imprimirArbol(nodo.getIzquierda(), prefijo + "   ");
    imprimirArbol(nodo.getDerecha(), prefijo + "   ");
    }

    public Ecosistema getRaiz() {
        return raiz;
    }
    
    
}
    