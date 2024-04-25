package tarea_1.afnd;

import tarea_1.nodos_y_estados.Par;

public class ConstruirAFND {
	private GestorAFND GestorAFND = null;

	public ConstruirAFND() {
		GestorAFND = new GestorAFND();
	}

	public Par construirKleene(Par parIn) { // Este método toma un par de nodos y construye un cierre de Kleene
								// (operador ‘*’)
		Par parOut = new Par();
		parOut.nodoInicio = GestorAFND.nuevoAFND();
		parOut.nodoFinal = GestorAFND.nuevoAFND();

		parOut.nodoInicio.siguiente = parIn.nodoInicio;
		parIn.nodoFinal.siguiente = parOut.nodoFinal;

		parOut.nodoInicio.siguiente2 = parOut.nodoFinal;
		parIn.nodoFinal.siguiente2 = parIn.nodoInicio;

		parIn.nodoInicio = parOut.nodoInicio;
		parIn.nodoFinal = parOut.nodoFinal;

		return parOut;
	}

	public Par construirMas(Par parIn) { // Este método toma un par de nodos y construye un cierre positivo (operador
								// ‘+’)
		Par parOut = new Par();

		parOut.nodoInicio = GestorAFND.nuevoAFND();
		parOut.nodoFinal = GestorAFND.nuevoAFND();

		parOut.nodoInicio.siguiente = parIn.nodoInicio;
		parIn.nodoFinal.siguiente = parOut.nodoFinal;

		parIn.nodoFinal.siguiente2 = parOut.nodoInicio;

		parIn.nodoInicio = parOut.nodoInicio;
		parIn.nodoFinal = parOut.nodoFinal;

		return parOut;
	}

	public Par construirAFNDParaCaracterIndividual(char c) { // Este método toma un carácter y construye el automata
											// que acepta solo ese carácter.
		Par parOut = new Par();
		parOut.nodoInicio = GestorAFND.nuevoAFND();
		parOut.nodoFinal = GestorAFND.nuevoAFND();
		parOut.nodoInicio.siguiente = parOut.nodoFinal;
		parOut.nodoInicio.setTransicion(c);

		return parOut;
	}

	public Par construirAFNDParaOR(Par izq, Par der) { // Este método toma dos pares de nodos y construye un automata
										// que acepta la expresión regular que representa el par de
										// nodos izquierdo o la expresión regular que representa el
										// par de nodos derecho.
		Par par = new Par();
		par.nodoInicio = GestorAFND.nuevoAFND();
		par.nodoFinal = GestorAFND.nuevoAFND();

		par.nodoInicio.siguiente = izq.nodoInicio;
		par.nodoInicio.siguiente2 = der.nodoInicio;

		izq.nodoFinal.siguiente = par.nodoFinal;
		der.nodoFinal.siguiente = par.nodoFinal;

		return par;
	}

	public Par construirAFNDParaConcatenacion(Par izq, Par der) { // Este método toma dos pares de nodos y construye
												// un automata que acepta la concatenación de la
												// expresión regular
		Par parOut = new Par();
		parOut.nodoInicio = izq.nodoInicio;
		parOut.nodoFinal = der.nodoFinal;

		izq.nodoFinal.siguiente = der.nodoInicio;

		return parOut;
	}

}
