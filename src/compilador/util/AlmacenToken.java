package compilador.util;

import java.util.HashMap;
import java.util.Map;

public class AlmacenToken {
    private static Map<Short,String > tokens= new HashMap<>();


    public static void clear(){
        tokens.clear();
    }

    public static void add(short t, String nombre){
        tokens.put(t,nombre);
    }

    public static String getRepresentacion(short token){// no me deja retornar nada despues de la excepcion nunca lo habia visto asi
        if (tokens.get(token)!= null){
            return tokens.get(token);
        }
        throw new IllegalStateException("el token->" +token+ " no se encuentra en los tokens almacenados");
    }
}
