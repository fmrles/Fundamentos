package tarea_1.afnd;

import java.util.Stack;

import tarea_1.nodos_y_estados.Transicion;

public class GestorAFND {
	private final int MAX_AFND = 256; // Numero max de estados
	private Transicion[] arrEstadosAFND = null; // Almacena todos los estados del automata
	private Stack<Transicion> pilaAFND = null; // Es una pila que se utiliza para almacenar los estados del automata
									// que se han descartado y pueden ser reutilizados.
	private int proxAsign = 0; // Es un contador que se utiliza para llevar la cuenta del próximo estado del
						// automata que se debe asignar.
	private int estadosAFND = 0; // Es un contador que se utiliza para llevar la cuenta del número de estados del
						// automata que se han asignado.

	public GestorAFND() {
		arrEstadosAFND = new Transicion[MAX_AFND];
		for (int i = 0; i < MAX_AFND; i++) {
			arrEstadosAFND[i] = new Transicion();
		}

		pilaAFND = new Stack<Transicion>();

	}

	public Transicion nuevoAFND() { // crea un nuevo estado del automata. Si hay estados en la pila, reutiliza uno
							// de ellos. Si no, crea un nuevo estado. Luego, limpia el estado del
							// automata, establece su estado y su transicion, y finalmente lo devuelve.
		Transicion AFND = null;
		if (pilaAFND.size() > 0) {
			AFND = pilaAFND.pop();
		} else {
			AFND = arrEstadosAFND[proxAsign];
			proxAsign++;
		}

		AFND.limpiarEstado();
		AFND.setEstado(estadosAFND++);
		AFND.setTransicion(Transicion.EPSILON);

		return AFND;
	}

	public void descartarAFND(Transicion AFND_Descartado) { // descarta un estado del automata. Disminuye el contador
											// estadosAFND, limpia el estado del automata y lo
											// empuja a la pila.
		--estadosAFND;
		AFND_Descartado.limpiarEstado();
		pilaAFND.push(AFND_Descartado);
	}

}
