package compilador.util;

import java.util.HashMap;
import java.util.Map;

public class TablaPalabrasReserv {
    private static final Map<String, Short> palabrasReserv = new HashMap<>();

    public static void incializarReservadas(){
        palabrasReserv.clear();
        palabrasReserv.put("IF",(short)0);
        palabrasReserv.put("THEN",(short)1);
        palabrasReserv.put("ELSE",(short)2);
        palabrasReserv.put("ENDIF",(short)3);
        palabrasReserv.put("PRINT",(short)4);
        palabrasReserv.put("FUNC",(short)5);
        palabrasReserv.put("RETURN",(short)6);
        palabrasReserv.put("BEGIN",(short)7);
        palabrasReserv.put("END",(short)8);
        palabrasReserv.put("BREAK",(short)9);
        palabrasReserv.put("DOUBLE",(short)10);
        palabrasReserv.put("REPEAT",(short)11);

    }

    public static boolean esReservada(String palabra) {
        return palabrasReserv.containsKey(palabra);
    }

    public static short getToken(String palabra) {
        return palabrasReserv.get(palabra);
    }

    public static void agregar(String palabra, short token) {
        palabrasReserv.putIfAbsent(palabra,token);
    }

    public static void clear() {
        palabrasReserv.clear();
    }
}

