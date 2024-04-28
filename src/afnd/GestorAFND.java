package src.afnd;

import java.util.Stack;

import src.nodos_y_estados.ObjetoEstado;

public class GestorAFND {
	private final int MAX_AFND = 256;
	private ObjetoEstado[] arrEstadosAFND = null;
	private Stack<ObjetoEstado> pilaAFND = null;
	private int proxAsign = 0; // Es un contador que se utiliza para llevar la cuenta del próximo estado del
						// automata que se debe asignar.
	private int estadosAFND = 0; // Es un contador que se utiliza para llevar la cuenta del número de estados del
						// automata que se han asignado.

	public GestorAFND() {
		arrEstadosAFND = new ObjetoEstado[MAX_AFND];
		for (int i = 0; i < MAX_AFND; i++) {
			arrEstadosAFND[i] = new ObjetoEstado();
		}

		pilaAFND = new Stack<ObjetoEstado>();

	}

	public ObjetoEstado nuevoAFND() { // crea un nuevo estado del automata. Si hay estados en la pila, reutiliza uno
							// de ellos. Si no, crea un nuevo estado. Luego, limpia el estado del
							// automata, establece su estado y su transicion, y finalmente lo devuelve.
		ObjetoEstado AFND = null;
		if (pilaAFND.size() > 0) {
			AFND = pilaAFND.pop();
		} else {
			AFND = arrEstadosAFND[proxAsign];
			proxAsign++;
		}

		AFND.limpiarEstado();
		AFND.setEstado(estadosAFND++);
		AFND.setObjetoEstado(ObjetoEstado.EPSILON);

		return AFND;
	}

	public void descartarAFND(ObjetoEstado afndDescartado) { // descarta un estado del automata. Disminuye el contador
											// estadosAFND, limpia el estado del automata y lo
											// empuja a la pila.
		--estadosAFND;
		afndDescartado.limpiarEstado();
		pilaAFND.push(afndDescartado);
	}

}
