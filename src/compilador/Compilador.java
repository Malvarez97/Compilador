package compilador;

import compilador.SIMBOLOS.TablaSimbolos;
import compilador.util.ManejadorArchivo;

public class Compilador {
    private static TablaSimbolos ts = new TablaSimbolos();

    public static void compilar(String nombrearchivo){
        if (!ManejadorArchivo.existeArchivo(nombrearchivo)){ System.out.println("el archivo no existe");}


        }
}
