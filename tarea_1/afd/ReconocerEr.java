package tarea_1.afd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ReconocerEr {
      private List<Character[]> afd;
      private List<Character[]> rEr = new ArrayList<>();
      private String[] alfabeto;
      private List<Character> estadoTerminal;
      private Set<Set<Character>> totalSet = new HashSet<>();
      private String path;
      private File file;

      private Map<Character, Character> map = new HashMap<>();

      public ReconocerEr(String path, List<Character[]> afd, List<Character> estadoTerminal, String[] alfabeto) {
            this.path = path;
            this.afd = afd;
            this.estadoTerminal = estadoTerminal;
            this.alfabeto = alfabeto;
            minimi();
            merge();

      }

      private void minimi() {
            init(totalSet);
            int contador = 0;
            while (true) {
                  if (contador == totalSet.size())
                        break;
                  else
                        contador = 0;
                  Set<Set<Character>> copiaDeTotalSet = new HashSet<>(totalSet);
                  for (Set<Character> set : copiaDeTotalSet) {
                        if (esIndivisible(set)) {
                              contador++;
                              continue;
                        } else {
                              minimi(set);
                        }
                  }
            }
      }

      private void minimi(Set<Character> estado) {
            totalSet.remove(estado);
            Map<String, String> map = new HashMap<>();
            for (Character character : estado) {
                  String aString = "";
                  for (int i = 1; i < alfabeto.length - 1; i++) {
                        aString += mover(character, alfabeto[i].charAt(0)) + "";
                  }
                  String tempSet = map.get(aString);
                  if (tempSet == null) {
                        map.put(aString, character + "");
                  } else {
                        tempSet += character;
                        map.put(aString, tempSet);
                  }
            }
            for (String str : map.values()) {
                  Set<Character> set = new HashSet<>();
                  for (int i = 0; i < str.length(); i++)
                        set.add(str.charAt(i));
                  totalSet.add(set);
            }
      }

      private Character mover(Character oriEstado, char input) {
            for (Character[] characters : afd) {
                  if (characters[0] != oriEstado)
                        continue;
                  else {
                        int index = getIndex(input);
                        return characters[index] == null ? null : characters[index];
                  }
            }
            return null;
      }

      private int getIndex(char input) {
            for (int i = 1; i < alfabeto.length - 1; i++) {
                  if (alfabeto[i].charAt(0) == input)
                        return i;
            }
            return -1;
      }

      private boolean esIndivisible(Set<Character> set) {
            if (set.size() == 1)
                  return true;
            else {
                  for (int i = 1; i < alfabeto.length - 1; i++) {
                        Set<Character> temp = new HashSet<>();
                        for (Character c : set) {
                              temp.add(mover(c, alfabeto[i].charAt(0)));
                        }
                        if (enTotalSet(temp))
                              continue;
                        else {
                              return false;
                        }
                  }
            }
            return true;
      }

      private boolean enTotalSet(Set<Character> temp) {
            if (temp.isEmpty())
                  return true;
            Set<Integer> indexs = new HashSet<>();
            for (Character character : temp) {
                  indexs.add(getSetNumero(character));
            }
            return indexs.size() == 1;
      }

      private Integer getSetNumero(Character character) {
            int i = 0;
            for (Set<Character> a : totalSet) {
                  for (Character b : a) {
                        if (b.equals(character))
                              return i;
                  }
                  i++;
            }
            return -1;
      }

      private void init(Set<Set<Character>> totalSet) {
            Set<Character> terminal = new HashSet<>();
            Set<Character> noTerminal = new HashSet<>();
            for (Character[] characters : afd) {
                  if (esEstadoFinal(characters[0]))
                        terminal.add(characters[0]);
                  else
                        noTerminal.add(characters[0]);
            }
            totalSet.add(noTerminal);
            totalSet.add(terminal);
      }

      private boolean esEstadoFinal(Character character) {
            for (Character estado : estadoTerminal) {
                  if (estado.equals(character))
                        return true;
            }
            return false;
      }

      public void reconocer(String str) {
            Character estadoAlcanzable = Character.valueOf('A');
            for (int i = 0; i < str.length(); i++) {
                  int index = getIndex(str.charAt(i));
                  if (index == -1) {
                        StringBuilder error = new StringBuilder();
                        error.append(str);
                        error.append(" no es reconocido \r\n");
                        for (int q = 0; q < i; q++)
                              error.append(" ");
                        error.append("↑");
                        System.err.println(error.toString());
                        return;
                  } else {
                        estadoAlcanzable = mover(estadoAlcanzable, index);
                        if (estadoAlcanzable == null) {
                              StringBuilder error = new StringBuilder();
                              error.append(str);
                              error.append(" no es reconocido \r\n");
                              for (int q = 0; q < i; q++)
                                    error.append(" ");
                              error.append("↑");
                              System.err.println(error.toString());
                              return;
                        }
                  }
            }
            if (esEstadoFinal(estadoAlcanzable)) {
                  System.out.println(str + " es reconocido");
                  guardarEnDir(str);
            } else {
                  System.err.println(str + " no es reconocido");
            }
      }

      private void guardarEnDir(String recoString) {
            file = new File(path, recoString + ".txt");
            try {
                  if (file.createNewFile()) {
                        System.out.println("String reconocido guardada en: " + file.getAbsolutePath());
                  } else {
                        System.out.println(
                                    "El String ya fue identificado como reconocido en: " + file.getAbsolutePath());
                  }
            } catch (IOException e) {
                  e.printStackTrace();
            }
      }

      private Character mover(Character oriEstado, int index) {
            for (Character[] characters : rEr) {
                  if (characters[0] != oriEstado)
                        continue;
                  else {
                        return characters[index];
                  }
            }
            return null;
      }

      public void merge() {
            for (Set<Character> characters : totalSet) {
                  if (characters.size() == 1)
                        continue;
                  else {
                        int i = 0;
                        char key = 0;
                        for (Character ch : characters) {
                              if (i++ == 0)
                                    key = ch;
                              else
                                    map.put(ch, key);
                        }
                  }
            }
            List<Character[]> tempERE = new ArrayList<>();
            for (Character[] characters : afd) {
                  if (ignorar(characters[0])) {
                        estadoTerminal.remove(characters[0]);
                        continue;
                  } else {
                        Character[] newChar = new Character[characters.length];
                        int i = 0;
                        for (Character ch : characters) {
                              if (necesitaReemplazar(ch))
                                    newChar[i] = map.get(ch);
                              else {
                                    newChar[i] = ch;
                              }
                              i++;
                        }
                        tempERE.add(newChar);
                  }
            }
            List<Character> estadoFinal = new ArrayList<>();
            for (Character[] ch : tempERE) {
                  if (estadoFinal.contains(ch[0]))
                        continue;
                  else {
                        estadoFinal.add(ch[0]);
                        rEr.add(ch);
                  }
            }
      }

      private boolean necesitaReemplazar(Character ch) {
            Character v = map.get(ch);
            return v != null;
      }

      private boolean ignorar(Character character) {
            for (Entry<Character, Character> m : map.entrySet()) {
                  if (m.getKey().equals(character))
                        return true;
            }
            return false;
      }
}
