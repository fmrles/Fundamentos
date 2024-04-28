package tarea_1.fundamentos_tarea_1;

import java.util.Scanner;

import tarea_1.afnd.AFND;
import tarea_1.afd.AFD;
import tarea_1.afd.ReconocerEr;

public class Main {

      public static void main(String[] args) {

            Scanner r = new Scanner(System.in);

            System.out.print("Escribe la expresión regular: ");
            String er = r.nextLine();
            System.out.print("Escribe nombre Carpeta: ");
            String dirPath = r.nextLine();
            Directorio dir = new Directorio(dirPath);
            dir.crearDirectorio();
            System.out.println("er:" + er);
            AFND afnd = new AFND(er);
            afnd.agregarSimboloUnion();
            afnd.postfijo();
            afnd.erToAfnd();
            afnd.print();

            AFD afd = new AFD(afnd.getPar(), afnd.getAlfabeto());
            afd.createDFA();
            afd.printAFD();

            ReconocerEr rEr = new ReconocerEr(dir.getDir(), afd.getAfd(), afd.getFinalEstado(), afd.getAlfabeto());

            System.out.println();
            System.out.println("Tu expresion regular:" + er);
            System.out.println("Escribe un string para ser reconocido por la ER ó escribe S para salir");
            while (r.hasNextLine()) {
                  String string = r.nextLine();
                  if (string.equals("S")) {

                        break;
                  } else {
                        rEr.reconocer(string);
                  }
                  System.out.println();
                  System.out.println("Escribe un string para ser reconocido por la ER ó escribe S para salir");
            }
            System.out.println("Strings Reconocidas:");
            dir.printArchivosEnDir();
            r.close();

      }
}
