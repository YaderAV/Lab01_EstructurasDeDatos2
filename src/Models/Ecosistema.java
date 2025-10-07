
package Models;

public class Ecosistema {
    private Ecosistema izquierda; 
    private Ecosistema derecha; 
    private final String ecosistema; 

    public Ecosistema(String nombre) {
        this.ecosistema = nombre;
    }

    public Ecosistema getIzquierda() {
        return izquierda;
    }

    public Ecosistema getDerecha() {
        return derecha;
    }


    public String getEcosistema() {
        return ecosistema;
    }
    
    
    public void setIzquierda(Ecosistema izquierda) {
        this.izquierda = izquierda;
    }

    public void setDerecha(Ecosistema derecha) {
        this.derecha = derecha;
    }
     
}
