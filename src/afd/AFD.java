package src.afd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import src.nodos_y_estados.Par;
import src.nodos_y_estados.ObjetoEstado;

import java.util.Queue;
import java.util.Set;

public class AFD {
      private Par par;
      private String[] alfabeto;
      private Map<Set<Integer>, Integer> map;
      private Set<Integer> tempSet;
      private Queue<Integer> queue = new LinkedList<>();
      private List<Character[]> afd = new ArrayList<>();
      private List<Character> finalEstado = new ArrayList<>();

      private int estado = 'A';

      public AFD(Par par, String[] alfabeto) {
            this.par = par;
            this.alfabeto = alfabeto;
            map = new HashMap<>();
            System.out.println("--------AFD--------");
            System.out.print("Sigma={");
            for (int i = 1; i < alfabeto.length - 1; i++) {
                  System.out.print(alfabeto[i]);
                  if (i < alfabeto.length - 2) {
                        System.out.print(",");
                  }
            }
            System.out.println("}");
            createDFA();
      }

      public List<Character[]> getAfd() {
            List<Character[]> erAfd = new ArrayList<>();
            for (Character[] ch : afd) {
                  if (getSet(ch[0]) == null || getSet(ch[0]).isEmpty()) {
                        continue;
                  } else {
                        Character[] newch = new Character[ch.length];
                        for (int i = 0; i < ch.length; i++) {
                              if (ch[i] == null)
                                    continue;
                              Set<Integer> set = getSet(ch[i]);
                              if (set == null || set.isEmpty())
                                    newch[i] = null;
                              else
                                    newch[i] = ch[i];
                        }
                        erAfd.add(newch);
                  }
            }
            return erAfd;
      }

      public List<Character> getFinalEstado() {
            return finalEstado;
      }

      public String[] getAlfabeto() {
            return alfabeto;
      }

      public void printAFD() {
            System.out.println("");
            System.out.println("K, Estado Inicial y Estado Final= ");
            System.out.println("↓");
            for (Entry<Set<Integer>, Integer> entry : map.entrySet()) {
                  if (entry.getValue() == -1) {
                        continue;
                  }
                  System.out.println((char) entry.getValue().intValue() + " = " + entry.getKey()
                              + (esInicio(entry.getKey()) ? " Inicio " : "")
                              + (esFinal(entry.getKey()) ? " Final " : ""));
            }
            System.out.println("--------AFD--------");
      }

      private boolean esInicio(Set<Integer> set) {
            for (Integer integer : set) {
                  if (integer == par.nodoInicio.getEstado())
                        return true;
            }
            return false;
      }

      private boolean esFinal(Set<Integer> set) {
            for (Integer integer : set) {
                  if (integer == par.nodoFinal.getEstado()) {
                        finalEstado.add((char) map.get(set).intValue());
                        return true;
                  }
            }
            return false;
      }

      public void createDFA() {
            System.out.println("");
            System.out.println("Delta=");
            for (int i = 0; i < alfabeto.length - 1; i++) {
                  System.out.print(" " + alfabeto[i]);
            }
            tempSet = new HashSet<>();
            Set<Integer> start = mover(par.nodoInicio, -1);
            map.put(start, estado);
            queue.add(estado++);
            while (!queue.isEmpty()) {
                  Character[] adfLine = new Character[alfabeto.length - 1];
                  int character = queue.poll();
                  System.out.println();
                  System.out.print((char) character + " ");
                  adfLine[0] = (char) character;
                  Set<Integer> set = getSet(character);
                  for (int i = 1; i < alfabeto.length - 1; i++) {
                        tempSet = new HashSet<>();
                        Set<Integer> midset = new HashSet<>();
                        for (Integer integer : set) {
                              ObjetoEstado ObjetoEstado = getObjetoEstado(par.nodoInicio, integer);
                              revisitar();
                              if (ObjetoEstado == null) {
                                    continue;
                              } else if ((char) ObjetoEstado.getObjetoEstado() == alfabeto[i].charAt(0)) {
                                    midset.add(ObjetoEstado.siguiente.getEstado());
                              }
                        }
                        for (Integer integer : midset) {
                              ObjetoEstado ObjetoEstado = getObjetoEstado(par.nodoInicio, integer);
                              revisitar();
                              mover(ObjetoEstado, -1);
                        }
                        Integer c = getCharacter(tempSet);
                        if (c == null) {
                              if (tempSet.isEmpty()) {
                                    map.put(tempSet, -1);
                                    System.out.print("_" + " ");
                                    adfLine[i] = null;
                              } else {
                                    queue.add(estado);
                                    System.out.print((char) estado + " ");
                                    adfLine[i] = (char) estado;
                                    map.put(tempSet, estado++);
                              }
                        } else {
                              if (c == -1) {
                                    System.out.print("_" + " ");
                                    adfLine[i] = null;
                              } else {
                                    adfLine[i] = (char) c.intValue();
                                    System.out.print((char) c.intValue() + " ");

                              }
                        }
                  }
                  afd.add(adfLine);
            }
            System.out.println("");
      }

      private Set<Integer> mover(ObjetoEstado nodoInicio, int i) {
            connect(nodoInicio, i);
            revisitar();
            return tempSet;
      }

      private void connect(ObjetoEstado ObjetoEstado, int i) {
            if (ObjetoEstado == null || ObjetoEstado.esVisitado()) {
                  return;
            }
            ObjetoEstado.setVisitado();
            tempSet.add(ObjetoEstado.getEstado());
            if (ObjetoEstado.getObjetoEstado() == -1 || ObjetoEstado.getObjetoEstado() == i) {
                  connect(ObjetoEstado.siguiente, i);
                  connect(ObjetoEstado.siguiente2, i);
            } else {
                  return;
            }
      }

      private Integer getCharacter(Set<Integer> set) {
            return map.get(set);
      }

      private void revisitar(ObjetoEstado ObjetoEstado) {
            if (ObjetoEstado == null || !ObjetoEstado.esVisitado()) {
                  return;
            }
            ObjetoEstado.setNoVisitado();
            revisitar(ObjetoEstado.siguiente);
            revisitar(ObjetoEstado.siguiente2);
      }

      private void revisitar() {
            par.nodoInicio.setNoVisitado();
            revisitar(par.nodoInicio.siguiente);
            revisitar(par.nodoInicio.siguiente2);
      }

      private ObjetoEstado getObjetoEstado(ObjetoEstado ObjetoEstado, Integer estadoInicio) {
            if (ObjetoEstado == null || ObjetoEstado.esVisitado())
                  return null;
            ObjetoEstado.setVisitado();
            if (ObjetoEstado.getEstado() == estadoInicio)
                  return ObjetoEstado;
            if (ObjetoEstado.getEstado() > estadoInicio)
                  return null;
            ObjetoEstado temp1 = getObjetoEstado(ObjetoEstado.siguiente, estadoInicio);
            ObjetoEstado temp2 = getObjetoEstado(ObjetoEstado.siguiente2, estadoInicio);
            if (temp1 != null)
                  return temp1;
            if (temp2 != null)
                  return temp2;
            return null;
      }

      private Set<Integer> getSet(int character) {
            for (Entry<Set<Integer>, Integer> m : map.entrySet()) {
                  if (m.getValue() == character)
                        return m.getKey();
            }
            return null;
      }

}
