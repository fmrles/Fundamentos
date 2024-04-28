
package src.afnd;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import src.nodos_y_estados.Par;
import src.nodos_y_estados.ObjetoEstado;

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
            for (int i = 0; i < this.er.length(); i++) {
                  if (esValido(this.er.charAt(i))) {
                        temp.add(this.er.charAt(i));
                  }
            }
            alfabeto = new String[temp.size() + 2];
            Object[] tempObj = temp.toArray();
            int i = 0;
            alfabeto[i] = "";
            for (; i < tempObj.length; i++) { //
                  alfabeto[i + 1] = (char) tempObj[i] + "";
            }
            alfabeto[i + 1] = "EPSILON"; // Llena el array alfabeto con los caracteres únicos
            // en er y añade una cadena vacía
            // al principio y “EPSILON” al final.
            agregarSimboloUnion();
            postfijo();
            erToAfnd();
      }

      public Par getPar() {
            return par;
      }

      public String[] getAlfabeto() {
            return alfabeto;
      }

      public String agregarSimboloUnion() {
            int longitud = er.length();
            if (longitud == 1) {
                  System.out.println("e.r con simbolo union:" + er);
                  erUnion = er;
                  return er;
            }
            int longitudCadenaRetorno = 0;
            char cadenaRetorno[] = new char[2 * longitud + 2];
            char primero, segundo = '0';
            for (int i = 0; i < longitud - 1; i++) {
                  primero = er.charAt(i);
                  segundo = er.charAt(i + 1);
                  cadenaRetorno[longitudCadenaRetorno++] = primero;
                  if (primero != '(' && primero != '|' && esValido(segundo)) {
                        cadenaRetorno[longitudCadenaRetorno++] = '.';
                  } else if (segundo == '(' && primero != '|' && primero != '(') {
                        cadenaRetorno[longitudCadenaRetorno++] = '.';
                  }
            }
            cadenaRetorno[longitudCadenaRetorno++] = segundo;
            String erString = new String(cadenaRetorno, 0, longitudCadenaRetorno);
            System.out.println("Expresion regular con simbolo union: " + erString);
            System.out.println();
            erUnion = erString;
            return erString;
      }

      private boolean esValido(char check) { // Verifica si el caracter check es valido.
            if (check >= 'a' && check <= 'z' || check >= 'A' && check <= 'Z' || check >= '0' && check <= '9') {
                  return true;
            }
            return false;
      }

      public String postfijo() { // Funcion que convierte la expresion regular a postfijo.
            erUnion = erUnion + "#";

            Stack<Character> s = new Stack<>();
            char ch = '#', ch1, op;
            s.push(ch);
            String erSalida = "";
            int ubicacion = 0;
            ch = erUnion.charAt(ubicacion++);
            while (!s.empty()) {
                  if (esValido(ch)) {
                        erSalida = erSalida + ch;
                        ch = erUnion.charAt(ubicacion++);
                  } else {
                        ch1 = s.peek();
                        if (rp(ch1) < re(ch)) {
                              s.push(ch);
                              ch = erUnion.charAt(ubicacion++);
                        } else if (rp(ch1) > re(ch)) {
                              op = s.pop();
                              erSalida = erSalida + op;
                        } else {
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

      public void erToAfnd() { // Funcion que convierte la expresion regular a un AFND.
            par = new Par();
            Par temp = new Par();
            Par der, izq;
            ConstructorDelAFND constructor = new ConstructorDelAFND();
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
            revisitar(this.par.nodoInicio);
            System.out.println("--------AFND--------");
            System.out.print("Sigma={");
            for (int i = 1; i < alfabeto.length - 1; i++) {
                  System.out.print(alfabeto[i]);
                  if (i < alfabeto.length - 2) {
                        System.out.print(",");
                  }
            }
            System.out.print("}");
            System.out.println(" ");
            System.out.println("");
            System.out.println("K y Delta:");
            System.out.println("↓");
            printAFND(this.par.nodoInicio);

            revisitar(this.par.nodoInicio);
            System.out.println(" ");
            System.out.println("Estado Inicial=q" + (this.par.nodoInicio).getEstado());
            System.out.println("Estado Final=q" + (this.par.nodoFinal).getEstado());
            System.out.println("--------AFND--------");
      }

      private void restate(ObjetoEstado startAFND) { // Funcion que reestablece los estados del AFND.
            if (startAFND == null || startAFND.esVisitado()) {
                  return;
            }
            startAFND.setVisitado();
            startAFND.setEstado(restate++);
            restate(startAFND.siguiente);
            restate(startAFND.siguiente2);
      }

      private void revisitar(ObjetoEstado startAFND) { // Funcion que revisita los nodos del AFND.
            if (startAFND == null || !startAFND.esVisitado()) {
                  return;
            }
            startAFND.setNoVisitado();
            revisitar(startAFND.siguiente);
            revisitar(startAFND.siguiente2);
      }

      private void printAFND(ObjetoEstado startAFND) {
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

      private void printAfndDelta(ObjetoEstado nodo) {
            if (nodo.siguiente != null) {
                  System.out.print("q" + nodo.getEstado());
                  if (nodo.getObjetoEstado() == -1) {
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
                        int index = getIndex("" + (char) nodo.getObjetoEstado());
                        for (int i = 0; i < alfabeto.length - 1; i++) {
                              if (i != index) {
                                    System.out.print("");
                              } else {
                                    if (nodo.siguiente2 != null) {
                                          System.out.print("," + "(" + (char) nodo.getObjetoEstado() + ",q"
                                                      + nodo.siguiente.getEstado() + ";q"
                                                      + nodo.siguiente2.getEstado() + ")");
                                    } else {
                                          System.out.print("," + (char) nodo.getObjetoEstado() + ",q"
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

      private int getIndex(String ch) { // Funcion que retorna el indice de un caracter ch en el array alfabeto.
            for (int i = 0; i < alfabeto.length; i++) {
                  if (alfabeto[i].equals(ch))
                        return i - 1;
            }
            return -1;
      }
}