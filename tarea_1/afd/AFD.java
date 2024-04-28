package tarea_1.afd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import tarea_1.nodos_y_estados.Transicion;
import tarea_1.nodos_y_estados.Par;

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
            System.out.println();
            System.out.println("--------AFD--------");
            System.out.print("Sigma={");
            for (int i = 1; i < alfabeto.length - 1; i++) {
                  System.out.print(alfabeto[i]);
                  if (i < alfabeto.length - 2) {
                        System.out.print(",");
                  }
            }
            System.out.println("}");

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
            tempSet = new HashSet<>();
            Set<Integer> start = move(par.nodoInicio, -1);
            map.put(start, estado);
            queue.add(estado++);
            while (!queue.isEmpty()) {
                  Character[] adfLine = new Character[alfabeto.length - 1];
                  int character = queue.poll();
                  adfLine[0] = (char) character;
                  Set<Integer> set = getSet(character);
                  for (int i = 1; i < alfabeto.length - 1; i++) {
                        tempSet = new HashSet<>();
                        Set<Integer> midset = new HashSet<>();
                        for (Integer integer : set) {
                              Transicion transicion = getTransicion(par.nodoInicio, integer);
                              revisit();
                              if (transicion == null) {
                                    continue;
                              } else if ((char) transicion.getTransicion() == alfabeto[i].charAt(0)) {
                                    midset.add(transicion.siguiente.getEstado());
                              }
                        }
                        for (Integer integer : midset) {
                              Transicion transicion = getTransicion(par.nodoInicio, integer);
                              revisit();
                              move(transicion, -1);
                        }
                        Integer c = getCharacter(tempSet);
                        if (c == null) {
                              if (tempSet.isEmpty()) {
                                    map.put(tempSet, -1);
                                    adfLine[i] = null;
                              } else {
                                    queue.add(estado);
                                    adfLine[i] = (char) estado;
                                    map.put(tempSet, estado++);
                              }
                        } else {
                              if (c == -1) {
                                    adfLine[i] = null;
                              } else {
                                    adfLine[i] = (char) c.intValue();

                              }
                        }
                  }
                  afd.add(adfLine);
            }
      }

      private Set<Integer> move(Transicion nodoInicio, int i) {
            connect(nodoInicio, i);
            revisit();
            return tempSet;
      }

      private void connect(Transicion transicion, int i) {
            if (transicion == null || transicion.esVisitado()) {
                  return;
            }
            transicion.setVisitado();
            tempSet.add(transicion.getEstado());
            if (transicion.getTransicion() == -1 || transicion.getTransicion() == i) {
                  connect(transicion.siguiente, i);
                  connect(transicion.siguiente2, i);
            } else {
                  return;
            }
      }

      private Integer getCharacter(Set<Integer> set) {
            return map.get(set);
      }

      private void revisit(Transicion transicion) {
            if (transicion == null || !transicion.esVisitado()) {
                  return;
            }
            transicion.setNoVisitado();
            revisit(transicion.siguiente);
            revisit(transicion.siguiente2);
      }

      private void revisit() {
            par.nodoInicio.setNoVisitado();
            revisit(par.nodoInicio.siguiente);
            revisit(par.nodoInicio.siguiente2);
      }

      private Transicion getTransicion(Transicion transicion, Integer estadoInicio) {
            if (transicion == null || transicion.esVisitado())
                  return null;
            transicion.setVisitado();
            if (transicion.getEstado() == estadoInicio)
                  return transicion;
            if (transicion.getEstado() > estadoInicio)
                  return null;
            Transicion temp1 = getTransicion(transicion.siguiente, estadoInicio);
            Transicion temp2 = getTransicion(transicion.siguiente2, estadoInicio);
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
