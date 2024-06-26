package src.main;

import java.io.File;

public class Directorio {

      private File directorio;

      public Directorio(String path) {
            directorio = new File(path);
      }

      public void crearDirectorio() {

            if (!directorio.exists()) {
                  if (directorio.mkdir()) {
                        System.out.println("Los archivos se guardaran en: " + directorio.getAbsolutePath());
                  } else {
                        System.out.println("No se pudo crear el directorio");
                  }
            } else {
                  System.out.println("El directorio ya existe");
            }
      }

      public void printArchivosEnDir() {
            if (directorio.list() == null) {
                  System.out.println("No hay archivos en el directorio");
            } else {
                  for (String file : directorio.list()) {
                        System.out.println(file);
                  }
            }

      }

      public String getDir() {
            return directorio.getAbsolutePath();
      }
}
