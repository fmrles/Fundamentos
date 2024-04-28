package src.nodos_y_estados;

public class ObjetoEstado { // Clase que representa un estado del automata

      public static final int EPSILON = -1;
      public static final int VACIO = -2;

      private int ObjetoEstado;

      public int getObjetoEstado() {
            return ObjetoEstado;
      }

      public void setObjetoEstado(int tipo) {
            ObjetoEstado = tipo;
      }

      public ObjetoEstado siguiente;
      public ObjetoEstado siguiente2;
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

}
