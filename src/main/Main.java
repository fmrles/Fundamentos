package src.main;

import java.util.Scanner;

import src.afd.AFD;
import src.afd.ReconocerEr;
import src.afnd.AFND;

public class Main {

      public static void main(String[] args) {

            Scanner r = new Scanner(System.in);
            // Entradas de Datos
            System.out.print("Escribe la expresión regular: ");
            String er = r.nextLine();
            System.out.print("Escribe nombre Carpeta: ");
            String dirPath = r.nextLine();
            Directorio dir = new Directorio(dirPath);
            dir.crearDirectorio();

            System.out.println("er:" + er);
            // Creación AFND
            AFND afnd = new AFND(er);
            afnd.print();

            System.out.println(" ");
            // Creación AFD
            AFD afd = new AFD(afnd.getPar(), afnd.getAlfabeto());
            afd.printAFD();
            // Reconocimiento de Stings
            ReconocerEr rEr = new ReconocerEr(dir.getDir(), afd.getAfd(), afd.getFinalEstado(), afd.getAlfabeto());

            System.out.println();

            System.out.println("Tu expresion regular:" + er);
            System.out.println("Escribe un string para ser reconocido por la ER o escribe S para salir");
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
