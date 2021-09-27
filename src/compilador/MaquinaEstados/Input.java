package compilador.MaquinaEstados;

public class Input {
    public static int DESCARTABLE = 0, SALTO_LINEA = 1, LETRA_MINUSC = 2, D_MINUSC = 3, U_MINUSC = 4, L_MINUSC = 5,
            LETRA_MAYUS = 6, DIGITO = 7, GUION_B = 8, PORCENTAJE = 9, MENOR = 10, MAYOR = 11,  E_EXP =12 ,IGUAL = 13,
            SUMA = 14, GUION = 15, MULTIPL = 16, DIV = 17, PARENT_A = 18, PARENT_C = 19,
            PUNTO = 20, COMA = 21, PUNTO_COMA = 22, COMILLA = 23, OTRO = 24, EOF = 25;

    public static int TOTAL_INPUTS = 26;

    /**
     * Devuelve el codigo asociado al input recibido.
     *
     * @param inputChar caracter leido.
     * @return el codigo asociado al caracter leido.
     */
    public static int charToInt(char inputChar) {
        switch (inputChar) {
            case '\t':
            case ' ':
                return DESCARTABLE;
            case '\n':
                return SALTO_LINEA;
            case 'd':
                return D_MINUSC;
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
        }

        if (inputChar >= 'a' && inputChar <= 'z') return LETRA_MINUSC;
        if (inputChar >= 'A' && inputChar <= 'Z') return LETRA_MAYUS;
        if (inputChar >= '0' && inputChar <= '9') return DIGITO;

        return OTRO;
    }
   
}
