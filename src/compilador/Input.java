package compilador;

public class Input {
    public static final int DESCARTABLE = 0, SALTO_LINEA = 1, LETRA_MINUSC = 2, D_MINUSC = 3, U_MINUSC = 4, I_MINUSC = 5,
            LETRA_MAYUS = 6, DIGITO = 7, GUION_B = 8, PORCENTAJE = 9, MENOR = 10, MAYOR = 11, ADMIRACION = 12, IGUAL = 13,
            SUMA = 14, GUION = 15, MULTIPL = 16, DIV = 17, LLAVE_A = 18, LLAVE_C = 19, PARENT_A = 20, PARENT_C = 21,
            PUNTO = 22, COMA = 23, PUNTO_COMA = 24, COMILLA = 25, OTRO = 26, EOF = 27;

    public static final int TOTAL_INPUTS = 28;

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
            case 'i':
                return I_MINUSC;
            case '_':
                return GUION_B;
            case '%':
                return PORCENTAJE;
            case '<':
                return MENOR;
            case '>':
                return MAYOR;
            case '!':
                return ADMIRACION;
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
            case '{':
                return LLAVE_A;
            case '}':
                return LLAVE_C;
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
        }

        if (inputChar >= 'a' && inputChar <= 'z') return LETRA_MINUSC;
        if (inputChar >= 'A' && inputChar <= 'Z') return LETRA_MAYUS;
        if (inputChar >= '0' && inputChar <= '9') return DIGITO;

        return OTRO;
    }
   
}
