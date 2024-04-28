
package tarea_1.afnd;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import tarea_1.nodos_y_estados.Par;
import tarea_1.nodos_y_estados.Transicion;

public class AFND {

      private int restate = 0;

      private String er;
      private String erUnion;
      private String erPostfijo;

      private String[] alfabeto;
      private Par par;

      public AFND(String er) { // Constructor de la clase AFND
            this.er = er;
            this.erUnion = null;
            this.erPostfijo = null;
            Set<Character> temp = new HashSet<>(); // Crea un conjunto temp para almacenar los caracteres únicos en er
                                                   // que son letras.
            for (int i = 0; i < this.er.length(); i++) { //
                  if (esValido(this.er.charAt(i))) {
                        temp.add(this.er.charAt(i));
                  }
            }
            alfabeto = new String[temp.size() + 2]; // Crea un array de String alfabeto con una
            // longitud de temp.size() + 2.

            Object[] tempObj = temp.toArray(); // Convierte el conjunto temp en un array de objetos tempObj.
            int i = 0;
            alfabeto[i] = "";
            for (; i < tempObj.length; i++) { //
                  alfabeto[i + 1] = (char) tempObj[i] + "";
            }
            alfabeto[i + 1] = "EPSILON"; // Llena el array alfabeto con los caracteres únicos
            // en er y añade una cadena vacía
            // al principio y “EPSILON” al final.
      }

      public Par getPar() {
            return par;
      }

      public String[] getAlfabeto() {
            return alfabeto;
      }

      public String agregarSimboloUnion() {
            int longitud = er.length(); // longitud de la expresion regular
            if (longitud == 1) { // si la longitud es 1, no se puede agregar simbolo union
                  System.out.println("e.r con simbolo union:" + er);
                  erUnion = er;
                  return er;
            } // Si la longitud de er es mayor que 1, crea un nuevo array de caracteres
              // cadena_retorno con una longitud de 2 * length + 2.
            int longitudCadenaRetorno = 0;
            char cadenaRetorno[] = new char[2 * longitud + 2];
            char primero, segundo = '0';
            for (int i = 0; i < longitud - 1; i++) { // Recorre la expresion regular y agrega el simbolo union en los
                                                     // lugares correctos.
                  primero = er.charAt(i);
                  segundo = er.charAt(i + 1);
                  cadenaRetorno[longitudCadenaRetorno++] = primero;
                  if (primero != '(' && primero != '|' && esValido(segundo)) {
                        cadenaRetorno[longitudCadenaRetorno++] = '.';
                  } else if (segundo == '(' && primero != '|' && primero != '(') {
                        cadenaRetorno[longitudCadenaRetorno++] = '.';
                  }
            }
            cadenaRetorno[longitudCadenaRetorno++] = segundo; // Agrega el ultimo caracter de la expresion regular.
            String erString = new String(cadenaRetorno, 0, longitudCadenaRetorno); // Convierte el array de
                                                                                   // caracteres cadena_retorno en un
                                                                                   // string.
            System.out.println("Expresion regular con simbolo union: " + erString);
            System.out.println();
            erUnion = erString;
            return erString;
      }

      private boolean esValido(char check) { // Verifica si el caracter check es una alfabeto.
            if (check >= 'a' && check <= 'z' || check >= 'A' && check <= 'Z' || check >= '0' && check <= '9') {
                  return true;
            }
            return false;
      }

      public String postfijo() {
            erUnion = erUnion + "#"; // Agrega el simbolo # al final de la expresion regular.

            Stack<Character> s = new Stack<>();
            char ch = '#', ch1, op;
            s.push(ch);
            String erSalida = "";
            int ubicacion = 0;
            ch = erUnion.charAt(ubicacion++); // Inicializa ch con el primer caracter de la expresion regular.
            while (!s.empty()) {
                  if (esValido(ch)) { // Si ch es una alfabeto, agrega ch a la expresion regular postfija.
                        erSalida = erSalida + ch;
                        ch = erUnion.charAt(ubicacion++);
                  } else { // Si ch no es una alfabeto, compara la relevancia de ch con la relevancia del
                           // caracter en la cima de la pila.
                        ch1 = s.peek();
                        if (rp(ch1) < re(ch)) { // Si la relevancia de ch1 es menor que la relevancia de ch, agrega ch a
                                                // la pila.
                              s.push(ch);
                              ch = erUnion.charAt(ubicacion++);
                        } else if (rp(ch1) > re(ch)) { // Si la relevancia de ch1 es mayor que la relevancia de ch, saca
                                                       // el
                                                       // caracter en la cima de la pila y agrega el caracter a la
                                                       // expresion
                                                       // regular postfija.
                              op = s.pop();
                              erSalida = erSalida + op;
                        } else { // Si la relevancia de ch1 es igual a la relevancia de ch, saca el caracter en
                                 // la cima
                                 // de la pila y agrega el caracter a la expresion regular postfija.
                              op = s.pop();
                              if (op == '(')
                                    ch = erUnion.charAt(ubicacion++);
                        }
                  }
            }
            System.out.println("postfijo: " + erSalida);
            System.out.println();
            erPostfijo = erSalida;
            return erSalida;
      }

      private int rp(char c) { // Funcion que retorna la relevancia en la pila de un caracter c.
            switch (c) {
                  case '#':
                        return 0;
                  case '(':
                        return 1;
                  case '*':
                        return 7;
                  case '+':
                        return 7;
                  case '.':
                        return 5;
                  case '|':
                        return 3;
                  case ')':
                        return 8;
            }
            return -1;
      }

      private int re(char c) { // Funcion que retorna la relevancia entrante de un caracter c.
            switch (c) {
                  case '#':
                        return 0;
                  case '(':
                        return 8;
                  case '*':
                        return 6;
                  case '+':
                        return 6;
                  case '.':
                        return 4;
                  case '|':
                        return 2;
                  case ')':
                        return 1;
            }
            return -1;
      }

      public void erToAfnd() {
            par = new Par();
            Par temp = new Par();
            Par der, izq;
            ConstruirAFND constructor = new ConstruirAFND();
            char ch[] = erPostfijo.toCharArray();
            Stack<Par> pila = new Stack<>();
            for (char c : ch) {
                  switch (c) {
                        case '|':
                              der = pila.pop();
                              izq = pila.pop();
                              par = constructor.construirAFNDParaOR(izq, der);
                              pila.push(par);
                              break;
                        case '*':
                              temp = pila.pop();
                              par = constructor.construirKleene(temp);
                              pila.push(par);
                              break;
                        case '+':
                              temp = pila.pop();
                              par = constructor.construirMas(temp);
                              pila.push(par);
                              break;
                        case '.':
                              der = pila.pop();
                              izq = pila.pop();
                              par = constructor.construirAFNDParaConcatenacion(izq, der);
                              pila.push(par);
                              break;
                        default:
                              par = constructor.construirAFNDParaCaracterIndividual(c);
                              pila.push(par);
                              break;
                  }
            }
      }

      public void print() {
            restate(this.par.nodoInicio);
            revisit(this.par.nodoInicio);
            System.out.println("--------AFND--------");
            System.out.println();
            System.out.print("Sigma={");
            for (int i = 1; i < alfabeto.length - 1; i++) {
                  System.out.print(alfabeto[i]);
                  if (i < alfabeto.length - 2) {
                        System.out.print(",");
                  }
            }
            System.out.print("}");

            System.out.println("");
            System.out.println("K y Delta:");

            printAFND(this.par.nodoInicio);

            revisit(this.par.nodoInicio);

            System.out.println("s=q" + (this.par.nodoInicio).getEstado());
            System.out.println("F=q" + (this.par.nodoFinal).getEstado());
            System.out.println("--------AFND--------");
      }

      private void restate(Transicion startAFND) { //
            if (startAFND == null || startAFND.esVisitado()) {
                  return;
            }
            startAFND.setVisitado();
            startAFND.setEstado(restate++);
            restate(startAFND.siguiente);
            restate(startAFND.siguiente2);
      }

      private void revisit(Transicion startAFND) {
            if (startAFND == null || !startAFND.esVisitado()) {
                  return;
            }
            startAFND.setNoVisitado();
            revisit(startAFND.siguiente);
            revisit(startAFND.siguiente2);
      }

      private void printAFND(Transicion startAFND) {

            if (startAFND == null || startAFND.esVisitado()) {
                  return;
            } else {
                  startAFND.setVisitado();

                  printAfndDelta(startAFND);
                  if (startAFND.siguiente != null) {
                        System.out.println("");
                  }
                  printAFND(startAFND.siguiente);
                  printAFND(startAFND.siguiente2);
            }

      }

      private void printAfndDelta(Transicion nodo) {
            if (nodo.siguiente != null) {
                  System.out.print("q" + nodo.getEstado());
                  if (nodo.getTransicion() == -1) {
                        for (int i = 0; i < alfabeto.length - 2; i++) {
                              System.out.print("");
                        }
                        if (nodo.siguiente2 != null) {
                              System.out.print(",_," + "(q" + nodo.siguiente.getEstado() + ";q"
                                          + nodo.siguiente2.getEstado() + ")");
                        } else {
                              System.out.print(",_,q" + nodo.siguiente.getEstado());
                        }
                  } else {
                        int index = getIndex("" + (char) nodo.getTransicion());
                        for (int i = 0; i < alfabeto.length - 1; i++) {
                              if (i != index) {
                                    System.out.print("");
                              } else {
                                    if (nodo.siguiente2 != null) {
                                          System.out.print("," + "(" + (char) nodo.getTransicion() + ",q"
                                                      + nodo.siguiente.getEstado() + ";q"
                                                      + nodo.siguiente2.getEstado() + ")");
                                    } else {
                                          System.out.print("," + (char) nodo.getTransicion() + ",q"
                                                      + nodo.siguiente.getEstado());
                                    }
                              }
                        }
                  }
            } else {
                  System.out.print(("q" + nodo.getEstado() + "  "));
                  System.out.print("  ");
                  System.out.print("  ");
                  System.out.println("");
            }
      }

      private int getIndex(String ch) {
            for (int i = 0; i < alfabeto.length; i++) {
                  if (alfabeto[i].equals(ch))
                        return i - 1;
            }
            return -1;
      }
}