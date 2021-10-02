package compilador;


import compilador.simbolo.TablaSimbolos;
import compilador.util.CodigoFuente;

import compilador.util.*;


import java.io.File;
import java.net.URL;

public class Compilador {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    private static  final TablaSimbolos tabla =new TablaSimbolos();

    // Inicializo reservadas
    private static void inicPalabrasReseradas() {
        TablaPalabrasReserv.clear();
        TablaPalabrasReserv.agregar("IF", (short) 257/*Parser.UINT*/);
        TablaPalabrasReserv.agregar("THEN", (short) 258/*Parser.UINT*/);
        TablaPalabrasReserv.agregar("ELSE", (short) 259/*Parser.UINT*/);
        TablaPalabrasReserv.agregar("ENDIF", (short) 260/*Parser.UINT*/);
        TablaPalabrasReserv.agregar("PRINT", (short) 261/*Parser.UINT*/);
        TablaPalabrasReserv.agregar("FUNC", (short) 262/*Parser.UINT*/);
        TablaPalabrasReserv.agregar("RETURN", (short) 263/*Parser.UINT*/);
        TablaPalabrasReserv.agregar("BEGIN", (short) 264/*Parser.UINT*/);
        TablaPalabrasReserv.agregar("BREAK", (short) 265/*Parser.UINT*/);
        TablaPalabrasReserv.agregar("ULONG", (short) 266/*Parser.UINT*/);
        TablaPalabrasReserv.agregar("POST", (short) 267/*Parser.UINT*/);
        TablaPalabrasReserv.agregar("TRY", (short) 268/*Parser.UINT*/);
        TablaPalabrasReserv.agregar("CATCH", (short) 269/*Parser.UINT*/);
        TablaPalabrasReserv.agregar("ASIGNACION", (short) 270/*Parser.UINT*/);
        TablaPalabrasReserv.agregar("END", (short) 271/*Parser.UINT*/);
        TablaPalabrasReserv.agregar("DOUBLE", (short) 272/*Parser.UINT*/);
        TablaPalabrasReserv.agregar("REPEAT", (short) 273/*Parser.UINT*/);
        System.out.println("Palabras reservadas bien inicizalizadas..");
    }
    // Inicializo Tokens

    private static void inicTokens (){
        AlmacenToken.clear();
        AlmacenToken.add(AnalizadorLex.T_EOF,"EOF");
        AlmacenToken.add((short) '<',"<");
        AlmacenToken.add((short) '>',">");
        AlmacenToken.add((short) '=',"=");
        AlmacenToken.add(/*Parser.COMP_MENOR_IGUAL*/(short)274,"<=");
        AlmacenToken.add(/*Parser.COMP_MAYOR_IGUAL*/(short)275,">=");
        AlmacenToken.add(/*Parser.COMP_DISTINTO*/(short)276,"<>");
        AlmacenToken.add(/*Parser.COMP_IGUAL*/(short)277,"==");
        AlmacenToken.add(/*Parser.COMP_IGUAL*/(short)277,"==");
        AlmacenToken.add(/*Parser.COMP_IGUAL*/(short)277,"||");
        AlmacenToken.add(/*Parser.COMP_IGUAL*/(short)277,"&&");

    }
    public void imprimirFinal(){
        System.out.println();
        System.out.println("------------------------------------------------------");
        System.out.println("Tabla de Simbolos");
        System.out.print(tabla.toString());
        System.out.println("------------------------------------------------------");
        System.out.println(Notificacion.getErrores()+ "Errores de la compilacion");
        System.out.println("------------------------------------------------------");
        System.out.println(Notificacion.getWarnings()+"Warnings");
        System.out.println("----------------------------------------------------");

    }
    public void ejecutar(String patshSrc){
        TablaSimbolos ts  = new TablaSimbolos();
        // empieza a compilar
        if(ManejadorArchivo.existeArchivo(patshSrc)){
            // existe el archivo pruebo
            inicTokens();
            inicPalabrasReseradas();
            CodigoFuente codigo = new CodigoFuente(ManejadorArchivo.getfuente(patshSrc));
            AnalizadorLex lexico = new AnalizadorLex(codigo,ts);
            consumidor(lexico);
            imprimirFinal();

        }
        else {
            System.out.println("no existe el archivo :   " + patshSrc);
        }
    }
    private void consumidor(AnalizadorLex lexico){
        while (lexico.tokengenerado()!=0) {
               System.out.println(ANSI_RED+ lexico.tokengenerado()+ANSI_RESET);
                }
    }
}
