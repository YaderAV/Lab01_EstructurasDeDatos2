
package Models;

public class Nodo {
    private Nodo izquierda; 
    private Nodo derecha; 
    private Nodo padre; 
    private final String nodo; 

    public Nodo(String nombre) {
        this.nodo = nombre;
        this.padre = null;
    }

    public Nodo getIzquierda() {
        return izquierda;
    }

    public Nodo getDerecha() {
        return derecha;
    }


    public String getNodo() {
        return nodo;
    }

    public Nodo getPadre() {
        return padre;
    }
    
    
    public void setIzquierda(Nodo izquierda) {
        this.izquierda = izquierda;
    }

    public void setDerecha(Nodo derecha) {
        this.derecha = derecha;
    }

    public void setPadre(Nodo padre) {
        this.padre = padre;
    }
     
}
