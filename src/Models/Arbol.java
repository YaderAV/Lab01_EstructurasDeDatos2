package Models;

public class Arbol {

    private Nodo raiz;

    public Arbol(Nodo raiz) {
        this.raiz = raiz;
    }

    public void imprimirArbol(Nodo nodo, String prefijo) {
        if (nodo == null) {
            return;
        }

        // Imprime el nodo actual con indentación
        System.out.println(prefijo + "• " + nodo.getNodo());

        // Llama recursivamente aumentando la sangría
        imprimirArbol(nodo.getIzquierda(), prefijo + "   ");
        imprimirArbol(nodo.getDerecha(), prefijo + "   ");
    }

    public Nodo getRaiz() {
        return raiz;
    }

    public boolean ultimoNivelLleno() {
        boolean lleno = false;

        return lleno;
    }

}
