package compilador.util;

import compilador.AnalizadorLex;

import java.util.ArrayList;
import java.util.List;

public  class Notificacion {

    private static final List<String> errores = new ArrayList<>();
    private static final List<String> warnings =new ArrayList<>();

    public Notificacion(String simbolo_no_reconocido, AnalizadorLex aLexico, CodigoFuente cFuente, boolean b) {
    }


    public static void addError(int linea, String error) {
        errores.add("linea   "+linea+"  ->  "+error);
    }


    public static String getErrores() { // retorno un String con los errores concatenados

        if(errores.isEmpty()) return "no hay Errores";
        StringBuilder contenedor = new StringBuilder();
        for (String error : errores) {
            contenedor.append(error).append("\n");
        }
        return contenedor.toString();
    }
    public static String getWarnings() { // retorno un String con los errores concatenados

        if(warnings.isEmpty()) return "no hay Warnings";
        StringBuilder contenedor = new StringBuilder();
        for (String war : warnings) {
            contenedor.append(war).append("\n");
        }
        return contenedor.toString();
    }


    public static String getResultado() {
        return (String)"Errores"+'\n'+ getErrores() + '\n'+"Warnings"+'\n'+getWarnings();
    }


    public static boolean hayErrores() {
        return errores.size() > 0;
    }

    public static void addWarnings(int linea, String war) {
        warnings.add("en la linea "+linea+"-> hay un Warning"+war);
    }


}
