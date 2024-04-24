import java.util.Scanner;

import Tarea1.AFND.AFND;

public class Main {

      public static void main(String[] args) {

            Scanner r = new Scanner(System.in);

            System.out.print("Escribe la expresión regular: ");
            String er = r.nextLine();
            System.out.println("er:" + er);
            AFND afnd = new AFND(er);
            afnd.agregar_simbolo_union();
            afnd.postfijo();
            afnd.er_a_afnd();

            System.out.print("Escribe nombre Carpeta: ");

            String dirPath = r.nextLine();
            Directorio dir = new Directorio(dirPath);
            dir.crearDirectorio();
            dir.printArchivosEnDir();
            r.close();
      }
}
