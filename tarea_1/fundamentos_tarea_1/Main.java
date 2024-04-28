package tarea_1.fundamentos_tarea_1;

//import java.io.File;
//import java.io.IOException;
import java.util.Scanner;

import tarea_1.afnd.AFND;
import tarea_1.afd.AFD;

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

            dir.printArchivosEnDir();
            r.close();

      }
}
