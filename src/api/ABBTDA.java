package api;

public interface ABBTDA {
        void inicializarArbol();
        void agregarElem(int x);
        void eliminarElem(int x);
        int raiz();
        ABBTDA hijoIzq();
        ABBTDA hijoDer();
        boolean arbolVacio();
}
