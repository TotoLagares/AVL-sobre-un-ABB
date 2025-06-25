package imp;
import api.ABBTDA;

public class ABB implements ABBTDA {

    class NodoABB {
        int info;
        ABBTDA hijoIzq;
        ABBTDA hijoDer;
        int altura;
    }

    NodoABB raiz;

    public void inicializarArbol() {
        raiz = null;
    }

    private int altura(ABBTDA a) {
        if (a.arbolVacio()) return 0;
        return ((ABB) a).raiz.altura;
    }

    private int factorBalance(NodoABB nodo) {
        if (nodo == null) return 0;
        return altura(nodo.hijoDer) - altura(nodo.hijoIzq);
    }

    private NodoABB rotacionIzquierda(NodoABB x) {
        NodoABB y = ((ABB) x.hijoDer).raiz;
        NodoABB T2 = ((ABB) y.hijoIzq).raiz;

        y.hijoIzq = new ABB();
        ((ABB) y.hijoIzq).raiz = x;
        x.hijoDer = new ABB();
        ((ABB) x.hijoDer).raiz = T2;

        x.altura = 1 + max(altura(x.hijoIzq), altura(x.hijoDer));
        y.altura = 1 + max(altura(y.hijoIzq), altura(y.hijoDer));

        return y;
    }

    private NodoABB rotacionDerecha(NodoABB y) {
        NodoABB x = ((ABB) y.hijoIzq).raiz;
        NodoABB T2 = ((ABB) x.hijoDer).raiz;

        x.hijoDer = new ABB();
        ((ABB) x.hijoDer).raiz = y;
        y.hijoIzq = new ABB();
        ((ABB) y.hijoIzq).raiz = T2;

        y.altura = 1 + max(altura(y.hijoIzq), altura(y.hijoDer));
        x.altura = 1 + max(altura(x.hijoIzq), altura(x.hijoDer));

        return x;
    }


    private NodoABB agregarNodo(NodoABB nodo, int x) {
        if (nodo == null) {
            NodoABB nuevo = new NodoABB();
            nuevo.info = x;
            nuevo.hijoIzq = new ABB();
            nuevo.hijoIzq.inicializarArbol();
            nuevo.hijoDer = new ABB();
            nuevo.hijoDer.inicializarArbol();
            nuevo.altura = 0;
            return nuevo;
        }

        if (x < nodo.info) {
            nodo.hijoIzq.agregarElem(x);
        } else if (x > nodo.info) {
            nodo.hijoDer.agregarElem(x);
        } else {
            return nodo;
        }

        nodo.altura = 1 + max(altura(nodo.hijoIzq), altura(nodo.hijoDer));

        int balance = factorBalance(nodo);

        if (balance > 1 && x > nodo.hijoDer.raiz()) {
            return rotacionIzquierda(nodo);
        }

        if (balance < -1 && x < nodo.hijoIzq.raiz()) {
            return rotacionDerecha(nodo);
        }

        if (balance > 1 && x < nodo.hijoDer.raiz()) {
            ABB derecho = (ABB) nodo.hijoDer;
            derecho.raiz = rotacionDerecha(derecho.raiz);
            return rotacionIzquierda(nodo);
        }

        if (balance < -1 && x > nodo.hijoIzq.raiz()) {
            ABB izquierdo = (ABB) nodo.hijoIzq;
            izquierdo.raiz = rotacionIzquierda(izquierdo.raiz);
            return rotacionDerecha(nodo);
        }
        return nodo;
    }


    public void agregarElem(int x) {
        raiz = agregarNodo(raiz, x);
    }

    private NodoABB eliminarNodo(NodoABB nodo, int x) {
        if (nodo == null) return null;

        if (x < nodo.info) {
            nodo.hijoIzq = new ABB();
            ((ABB) nodo.hijoIzq).raiz = eliminarNodo(((ABB) nodo.hijoIzq).raiz, x);
        } else if (x > nodo.info) {
            nodo.hijoDer = new ABB();
            ((ABB) nodo.hijoDer).raiz = eliminarNodo(((ABB) nodo.hijoDer).raiz, x);
        } else {

            if (nodo.hijoIzq.arbolVacio() && nodo.hijoDer.arbolVacio()) {
                return null;
            }

            if (!nodo.hijoIzq.arbolVacio()) {
                int mayor = mayor(nodo.hijoIzq);
                nodo.info = mayor;
                nodo.hijoIzq = new ABB();
                ((ABB) nodo.hijoIzq).raiz = eliminarNodo(((ABB) nodo.hijoIzq).raiz, mayor);
            } else {
                int menor = menor(nodo.hijoDer);
                nodo.info = menor;
                nodo.hijoDer = new ABB();
                ((ABB) nodo.hijoDer).raiz = eliminarNodo(((ABB) nodo.hijoDer).raiz, menor);
            }
        }

        nodo.altura = 1 + max(altura(nodo.hijoIzq), altura(nodo.hijoDer));

        int balance = factorBalance(nodo);

        if (balance > 1 && factorBalance(((ABB) nodo.hijoIzq).raiz) >= 0) {
            return rotacionDerecha(nodo);
        }
        if (balance > 1 && factorBalance(((ABB) nodo.hijoIzq).raiz) < 0) {
            ABB izquierdo = (ABB) nodo.hijoIzq;
            izquierdo.raiz = izquierdo.rotacionIzquierda(izquierdo.raiz);
            return rotacionDerecha(nodo);
        }
        if (balance < -1 && factorBalance(((ABB) nodo.hijoDer).raiz) <= 0) {
            return rotacionIzquierda(nodo);
        }
        if (balance < -1 && factorBalance(((ABB) nodo.hijoDer).raiz) > 0) {
            ABB derecho = (ABB) nodo.hijoDer;
            derecho.raiz = derecho.rotacionDerecha(derecho.raiz);
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    public void eliminarElem(int x) {
        raiz = eliminarNodo(raiz, x);
    }

    public int raiz() {
        return raiz.info;
    }

    public ABBTDA hijoIzq() {
        if (raiz == null) return null;
        return raiz.hijoIzq;
    }

    public ABBTDA hijoDer() {
        if (raiz == null) return null;
        return raiz.hijoDer;
    }

    public boolean arbolVacio() {
        return raiz == null;
    }

    private int mayor(ABBTDA a) {
        if (a.hijoDer().arbolVacio())
            return a.raiz();
        else
            return mayor(a.hijoDer());
    }

    private int menor(ABBTDA a) {
        if (a.hijoIzq().arbolVacio())
            return a.raiz();
        else
            return menor(a.hijoIzq());
    }
    private int max(int a, int b) {
        return (a > b) ? a : b;
    }
}

