package compilador.maquina_estado;

public class Contenedor_Inputs {
    public static int DESCARTABLE = 0, SALTO_LINEA = 1, LETRA_MINUSC = 2, E_EXP = 3, U_MINUSC = 4, L_MINUSC = 5,
            LETRA_MAYUS = 6, DIGITO = 7, GUION_B = 8, PORCENTAJE = 9, MENOR = 10, MAYOR = 11 ,IGUAL = 12,
            SUMA = 13, GUION = 14, MULTIPL = 15, DIV = 16, PARENT_A = 17, PARENT_C = 18,
            PUNTO = 19, COMA = 20, PUNTO_COMA = 21, COMILLA = 22, OTRO = 23, OR=24 ,AND=25, DOSP =26 ,EOF = 27;

    public static int TOTAL_INPUTS = 28;

   // clase contenedora de los numeros asignados a cada una de las entradas
    public static int charToInt(char input) {
        switch (input) {
            case '\t':
            case ' ':
                return DESCARTABLE;
            case '\n':
                return SALTO_LINEA;
            case 'u':
                return U_MINUSC;
            case 'l':
                return L_MINUSC;
            case '_':
                return GUION_B;
            case '%':
                return PORCENTAJE;
            case '<':
                return MENOR;
            case '>':
                return MAYOR;
            case '=':
                return IGUAL;
            case '+':
                return SUMA;
            case '-':
                return GUION;
            case '*':
                return MULTIPL;
            case '/':
                return DIV;
            case '(':
                return PARENT_A;
            case ')':
                return PARENT_C;
            case '.':
                return PUNTO;
            case ',':
                return COMA;
            case ';':
                return PUNTO_COMA;
            case '"':
                return COMILLA;
            case 'E':
                return E_EXP;
            case '|':
                return OR;
            case'&':
                return AND;
            case ':':
                return DOSP;

        }

        if (input >= 'a' && input <= 'z') return LETRA_MINUSC;
        if (input >= 'A' && input <= 'Z') return LETRA_MAYUS;
        if (input>= '0' && input <= '9') return DIGITO;

        return OTRO;
    }
   
}
