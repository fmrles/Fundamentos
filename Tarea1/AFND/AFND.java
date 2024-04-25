package Tarea1.AFND;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import Tarea1.nodos_y_estados.Par;

public class AFND {
      // private int restate = 0;

      private String er;
      private String erUnion;
      private String erPostfijo;

      private String[] letra;
      private Par par;

      // Poner Clase Imprimir

      public AFND(String er) { // Constructor de la clase AFND
            this.er = er;
            this.erUnion = null;
            this.erPostfijo = null;
            Set<Character> temp = new HashSet<>(); // Crea un conjunto temp para almacenar los caracteres únicos en er
                                                   // que son letras.
            for (int i = 0; i < this.er.length(); i++) { //
                  if (es_letra(this.er.charAt(i))) {
                        temp.add(this.er.charAt(i));
                  }
            }
            letra = new String[temp.size() + 2]; // Crea un array de String letra con una longitud de temp.size() + 2.

            Object[] tempObj = temp.toArray(); // Convierte el conjunto temp en un array de objetos tempObj.
            int i = 0;
            letra[i] = "";
            for (; i < tempObj.length; i++) { //
                  letra[i + 1] = (char) tempObj[i] + "";
            }
            letra[i + 1] = "EPSILON"; // Llena el array letra con los caracteres únicos en er y añade una cadena vacía
                                      // al principio y “EPSILON” al final.

      }

      public String agregar_simbolo_union() {
            int longitud = er.length(); // longitud de la expresion regular
            if (longitud == 1) { // si la longitud es 1, no se puede agregar simbolo union
                  System.out.println("e.r con simbolo union:" + er);
                  erUnion = er;
                  return er;
            } // Si la longitud de er es mayor que 1, crea un nuevo array de caracteres
              // cadena_retorno con una longitud de 2 * length + 2.
            int longitud_cadena_retorno = 0;
            char cadena_retorno[] = new char[2 * longitud + 2];
            char primero, segundo = '0';
            for (int i = 0; i < longitud - 1; i++) { // Recorre la expresion regular y agrega el simbolo union en los
                                                     // lugares correctos.
                  primero = er.charAt(i);
                  segundo = er.charAt(i + 1);
                  cadena_retorno[longitud_cadena_retorno++] = primero;
                  if (primero != '(' && primero != '|' && es_letra(segundo)) {
                        cadena_retorno[longitud_cadena_retorno++] = '.';
                  } else if (segundo == '(' && primero != '|' && primero != '(') {
                        cadena_retorno[longitud_cadena_retorno++] = '.';
                  }
            }
            cadena_retorno[longitud_cadena_retorno++] = segundo; // Agrega el ultimo caracter de la expresion regular.
            String erString = new String(cadena_retorno, 0, longitud_cadena_retorno); // Convierte el array de
                                                                                      // caracteres cadena_retorno en un
                                                                                      // string.
            System.out.println("Expresion regular con simbolo union: " + erString);
            System.out.println();
            erUnion = erString;
            return erString;
      }

      private boolean es_letra(char check) { // Verifica si el caracter check es una letra.
            if (check >= 'a' && check <= 'z' || check >= 'A' && check <= 'Z') {
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
                  if (es_letra(ch)) { // Si ch es una letra, agrega ch a la expresion regular postfija.
                        erSalida = erSalida + ch;
                        ch = erUnion.charAt(ubicacion++);
                  } else { // Si ch no es una letra, compara la relevancia de ch con la relevancia del
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

      public void er_a_afnd() {
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

}
