package compilador;


public class Estado{
public static final int INICIAL = 0;
public static final int DETECCION_ID = 1;
public static final int DETECCION_PR = 2;
public static final int INICIO_COMENT = 3;
public static final int CUERPO_COMENT = 4;
public static final int COMP_MENOR = 5;
public static final int COMP_MAYOR = 6;
public static final int COMP_DISTINTO = 7;
public static final int SIGNO_IGUAL = 8;
public static final int CTE_PARTE_ENTERA = 9;
public static final int CTE_UI_SUF1 = 10;
public static final int CTE_UI_SUF2 = 11;
public static final int CTE_UI_SUF3 = 12;
public static final int CTE_PARTE_DECIM = 13;
public static final int CTE_PARTE_EXP_SIGNO = 14;
public static final int CTE_PARTE_EXP_VALOR = 15;
public static final int CADENA = 16;
public static final int FINAL = 17;

public static final int TOTAL_ESTADOS = 18;
}
