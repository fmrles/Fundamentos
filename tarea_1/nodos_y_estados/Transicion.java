package tarea_1.nodos_y_estados;

public class Transicion {

    public static final int EPSILON = -1;
    public static final int VACIO = -2;

    private int transicion;

    public int getTransicion() {
        return transicion;
    }

    public void setTransicion(int tipo) {
        transicion = tipo;
    }

    public Transicion siguiente;
    public Transicion siguiente2;
    private int estado;
    private boolean visitado = false;

    public void setVisitado() {
        visitado = true;
    }

    public void setNoVisitado() {
        visitado = false;
    }

    public boolean esVisitado() {
        return visitado;
    }

    public void setEstado(int num) {
        estado = num;
    }

    public int getEstado() {
        return estado;
    }

    public void limpiarEstado() {
        siguiente = siguiente2 = null;
        estado = -1;
    }

    @Override
    public String toString() {
        return (char) transicion + " " + estado + " " + esVisitado();
    }

}
