package compilador.util;

import java.io.*;

public class ManejadorArchivo {

    // esta clase es utilizada para leer desde el archivo

    public static String getfuente(String path) {
        File archivo;
        FileReader fr = null;
        BufferedReader br;
        StringBuilder builderFuente = new StringBuilder();

        try {
            // Apertura del fichero
            archivo = new File(path);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while ((linea = br.readLine()) != null)
                builderFuente.append(linea).append("\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cierre del fichero
            try {
                if (null != fr)
                    fr.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (builderFuente.length() == 0) {
            System.out.println("El archivo: '" + path + "' esta vacio.");
            return "";
        }
        return builderFuente.substring(0,builderFuente.length()-1); //Saca el '\n' hecho luego de leer la ultima linea.
    }

    public static void WriteArchivo(String nombre, String texto){
        FileWriter fichero = null;
        PrintWriter pw;
        try {
            //Apertura y escritura del archivo
            fichero = new FileWriter(nombre, false);
            pw = new PrintWriter(fichero);
            pw.println(texto);

        } catch (IOException ioEx){
            System.out.println(ioEx.getMessage());
        } finally {
            try {
                //Cierre del archivo
                if (null != fichero)
                    fichero.close();
            } catch (IOException ioEx) {
                System.out.println(ioEx.getMessage());
            }
        }
    }

    public static boolean existeArchivo(String path){
        File f = new File(path);
        return (!f.isDirectory() && f.exists());
    }


}
