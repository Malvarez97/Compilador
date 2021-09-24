package compilador;


import compilador.Asemanticas.*;

public class MaquinaEstados {
	private final TransicionEstados[][] maquinaEstados =
	        new TransicionEstados[Estado.TOTAL_ESTADOS][Input.TOTAL_INPUTS]; //[filas][columnas].
	    private final AnalizadorLex aLexico; //Permite agregar tokens a medida que se generan.
	    private final CuentaSaltoLinea cuentaSaltoLinea = new CuentaSaltoLinea(); //Permite saber la linea actual
	    private int estadoActual = Estado.INICIAL;

	    /**
	     * Constructor.
	     */
	    public MaquinaEstados(AnalizadorLex aLexico, CodigoFuente cFuente, TablaSimbolos tablaS,
	                          TablaPalabrasReserv tablaPR) {
	        this.aLexico = aLexico;

	        inicMaquinaEstados(cFuente, tablaS, tablaPR);
	    }
		// Cambiar el orden de la inicializacion
	    private void inicMaquinaEstados(CodigoFuente cFuente, TablaSimbolos tablaS, TablaPalabrasReserv tablaPR) {
	        /* Acciones semanticas */
	        InicStringVacio inicStringVacio = new InicStringVacio();
	        ConcChar concatenaChar = new ConcChar(cFuente);
	        TruncId truncaId = new TruncId(aLexico);
	        Retrocede_Fuente retrocedeFuente = new Retrocede_Fuente(cFuente);
	        GeneraTs generaTokenId = new GeneraTs(this, tablaS, Parser.ID);
	        GeneraTs generaTokenCadena = new GeneraTs(this, tablaS, Parser.CADENA);
	        GeneraPr generaTokenPR = new GeneraPr(this);
	        ConsumeChar consumeChar = new ConsumeChar();
	        GeneraTokenUINT generaTokenUINT = new GeneraTokenUINT(this, tablaS, Parser.CTE_UINT);
	        GeneraTokenDouble generaTokenDouble = new GeneraTokenDouble(this, tablaS, Parser.CTE_DOUBLE);
	        Notificacion notificaErrorLexico = new Notificacion("Simbolo no reconocido", aLexico, cFuente, true);
	        GeneraTp generar = new GeneraTp(this, AnalizadorLex.T_EOF);

	        /* Estados y transiciones */
	        inicTransiciones(Estado.INICIAL, Estado.INICIAL,
	            notificaErrorLexico); //Transiciones por defecto para cualquier simbolo no reconocido.
	        maquinaEstados[Estado.INICIAL][Input.DESCARTABLE] = new TransicionEstados(Estado.INICIAL); //Tab y espacio.
	        maquinaEstados[Estado.INICIAL][Input.SALTO_LINEA] = new TransicionEstados(Estado.INICIAL,
	            cuentaSaltoLinea); //Salto de linea.
	        inicCaminoLiterales(cFuente);
	        inicCaminoIds(inicStringVacio, concatenaChar, truncaId, generaTokenId, retrocedeFuente);
	        inicCaminoPRs(inicStringVacio, concatenaChar, generaTokenPR, retrocedeFuente);
	        inicCaminoComentario(cFuente, retrocedeFuente, notificaErrorLexico);
	        inicCaminoComparadores(cFuente, retrocedeFuente, consumeChar);
	        inicCaminoCtesNum(inicStringVacio, concatenaChar, retrocedeFuente, generaTokenUINT, consumeChar,
	            generaTokenDouble);
	        inicCaminoCadenas(cFuente, inicStringVacio, concatenaChar, generaTokenCadena);
	        maquinaEstados[Estado.INICIAL][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenEOF); //EOF

	    }

	    /**
	     * @return true si la maquina esta en el estado final, false si no lo esta.
	     */
	    public boolean estadoFinalAlcanzado() {
	        return estadoActual == Estado.FINAL;
	    }

	    public void reiniciar() {
	        estadoActual = 0;
	    }

	    public void setVariablesSintactico(short token, String lexema) {
	        aLexico.setVariablesSintactico(token, lexema);
	    }

	    /**
	     * @return la linea del codigo fuente en la que se encuentra actualmente la maquina de estados.
	     */
	    public int getLineaActual() {
	        return cuentaSaltoLinea.getCantLineas();
	    }

	    /**
	     * Ejecuta la accion semantica del estado y avanza al siguiente.
	     *
	     * @param charInput caracter leido.
	     */
	    public void transicionar(char charInput) {
	        int codigoInput = Input.charToInt(charInput); //Obtiene el codigo asociado al caracter leido.

	        TransicionEstados TransicionEstados = maquinaEstados[estadoActual][codigoInput];

	        estadoActual = TransicionEstados.siguienteEstado();

	        TransicionEstados.ejecutarAccionSemantica();
	    }

	    /**
	     * Solo se ejecuta al alcanzar el EOF del codigo fuente. El funcionamiento es el mismo que transicionar(char).
	     */
	    public void transicionarEOF() {
	        TransicionEstados TransicionEstados = maquinaEstados[estadoActual][Input.EOF];

	        TransicionEstados.ejecutarAccionSemantica();
	        aLexico.setVariablesSintactico(AnalizadorLex.T_EOF, ""); //Genera el token asociado al EOF ('0').
	        estadoActual = Estado.FINAL; //Finalizo ejecucion
	    }

	    /**
	     * Establece una transicion predeterminada para un estado en especifico.
	     *
	     * @param estadoOrigen       estado desde donde partira la transicion.
	     * @param estadoDestino      estado al cual se llega luego de la transicion.
	     * @param accionesSemanticas acciones semanticas a ejecutar al transicionar.
	     */
	    private void inicTransiciones(int estadoOrigen, int estadoDestino, AccionSemantica... accionesSemanticas) {
	        for (int input = 0; input < Input.TOTAL_INPUTS; input++)
	            maquinaEstados[estadoOrigen][input] = new TransicionEstados(estadoDestino, accionesSemanticas);
	    }


	    /**
	     * Transiciones asociadas a la deteccion de tokens literales.
	     */
	    private void inicCaminoLiterales(CodigoFuente cFuente) {
	        GeneraTokenLiteral generaTokenLiteral = new GeneraTokenLiteral(this, cFuente);
	        maquinaEstados[Estado.INICIAL][Input.SUMA] = new TransicionEstados(Estado.FINAL, generaTokenLiteral);
	        maquinaEstados[Estado.INICIAL][Input.GUION] = new TransicionEstados(Estado.FINAL, generaTokenLiteral);
	        maquinaEstados[Estado.INICIAL][Input.MULTIPL] = new TransicionEstados(Estado.FINAL, generaTokenLiteral);
	        maquinaEstados[Estado.INICIAL][Input.DIV] = new TransicionEstados(Estado.FINAL, generaTokenLiteral);
	        maquinaEstados[Estado.INICIAL][Input.LLAVE_A] = new TransicionEstados(Estado.FINAL, generaTokenLiteral);
	        maquinaEstados[Estado.INICIAL][Input.LLAVE_C] = new TransicionEstados(Estado.FINAL, generaTokenLiteral);
	        maquinaEstados[Estado.INICIAL][Input.PARENT_A] = new TransicionEstados(Estado.FINAL, generaTokenLiteral);
	        maquinaEstados[Estado.INICIAL][Input.PARENT_C] = new TransicionEstados(Estado.FINAL, generaTokenLiteral);
	        maquinaEstados[Estado.INICIAL][Input.COMA] = new TransicionEstados(Estado.FINAL, generaTokenLiteral);
	        maquinaEstados[Estado.INICIAL][Input.PUNTO_COMA] = new TransicionEstados(Estado.FINAL, generaTokenLiteral);
	    }

	    /**
	     * Transiciones asociadas a la deteccion de identificadores.
	     */
	    private void inicCaminoIds(AccionSemantica inicStringVacio, AccionSemantica concatenaChar, AccionSemantica truncaId,
	                               AccionSemantica generaTokenId, AccionSemantica retrocedeFuente) {
	        /* Estado 0 */
	        maquinaEstados[Estado.INICIAL][Input.D_MINUSC] = new TransicionEstados(Estado.DETECCION_ID, inicStringVacio,
	            concatenaChar);
	        maquinaEstados[Estado.INICIAL][Input.U_MINUSC] = new TransicionEstados(Estado.DETECCION_ID, inicStringVacio,
	            concatenaChar);
	        maquinaEstados[Estado.INICIAL][Input.I_MINUSC] = new TransicionEstados(Estado.DETECCION_ID, inicStringVacio,
	            concatenaChar);
	        maquinaEstados[Estado.INICIAL][Input.LETRA_MINUSC] = new TransicionEstados(Estado.DETECCION_ID, inicStringVacio,
	            concatenaChar);

	        /* Estado 1 */
	        //Inputs no reconocidos.
	        inicTransiciones(Estado.DETECCION_ID, Estado.FINAL, truncaId, generaTokenId, retrocedeFuente);
	        //Salto de linea. Cuenta una nueva linea. No devuelve el ultimo leido pq se descartaria de todas formas.
	        maquinaEstados[Estado.DETECCION_ID][Input.SALTO_LINEA] = new TransicionEstados(Estado.FINAL, truncaId,
	            generaTokenId, cuentaSaltoLinea);
	        //Letras minusculas.
	        maquinaEstados[Estado.DETECCION_ID][Input.D_MINUSC] = new TransicionEstados(Estado.DETECCION_ID, concatenaChar);
	        maquinaEstados[Estado.DETECCION_ID][Input.U_MINUSC] = new TransicionEstados(Estado.DETECCION_ID, concatenaChar);
	        maquinaEstados[Estado.DETECCION_ID][Input.I_MINUSC] = new TransicionEstados(Estado.DETECCION_ID, concatenaChar);
	        maquinaEstados[Estado.DETECCION_ID][Input.LETRA_MINUSC] = new TransicionEstados(Estado.DETECCION_ID,
	            concatenaChar);
	        //Guio bajo
	        maquinaEstados[Estado.DETECCION_ID][Input.DIGITO] = new TransicionEstados(Estado.DETECCION_ID, concatenaChar);
	        //Digitos
	        maquinaEstados[Estado.DETECCION_ID][Input.GUION_B] = new TransicionEstados(Estado.DETECCION_ID, concatenaChar);
	        //EOF. Voy directo al estado final. No hace falta devolver ultimo leido.
	        maquinaEstados[Estado.DETECCION_ID][Input.EOF] = new TransicionEstados(Estado.FINAL, truncaId, generaTokenId);
	    }

	    /**
	     * Transiciones asociadas a la deteccion de palabras reservadas.
	     */
	    private void inicCaminoPRs(AccionSemantica inicStringVacio, AccionSemantica concatenaChar,
	                               AccionSemantica generaTokenPR, AccionSemantica retrocedeFuente) {
	        /* Estado 0 */
	        maquinaEstados[Estado.INICIAL][Input.LETRA_MAYUS] = new TransicionEstados(Estado.DETECCION_PR, inicStringVacio,
	            concatenaChar);

	        /* Estado 1 */
	        //Inputs no reconocidos.
	        inicTransiciones(Estado.DETECCION_PR, Estado.FINAL, generaTokenPR, retrocedeFuente);
	        //Salto de linea. Cuenta una nueva linea. No devuelve el ultimo leido pq se descartaria de todas formas.
	        maquinaEstados[Estado.DETECCION_PR][Input.SALTO_LINEA] = new TransicionEstados(Estado.FINAL, generaTokenPR,
	            cuentaSaltoLinea);
	        //Letras mayusculas.
	        maquinaEstados[Estado.DETECCION_PR][Input.LETRA_MAYUS] = new TransicionEstados(Estado.DETECCION_PR,
	            concatenaChar);
	        //Guion bajo.
	        maquinaEstados[Estado.DETECCION_PR][Input.GUION_B] = new TransicionEstados(Estado.DETECCION_PR, concatenaChar);
	        //EOF. Voy directo al estado final. No hace falta devolver ultimo leido.
	        maquinaEstados[Estado.DETECCION_PR][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenPR);

	    }

	    /**
	     * Transiciones asociadas a la deteccion y descarte de comentarios unilinea.
	     */
	    private void inicCaminoComentario(CodigoFuente cFuente, AccionSemantica retrocedeFuente,
	                                      AccionSemantica notificaErrorLexico) {
	        /* Estado 0 */
	        maquinaEstados[Estado.INICIAL][Input.PORCENTAJE] = new TransicionEstados(Estado.INICIO_COMENT);

	        /* Estado 3 */
	        //Inputs invalidos. Hay que notificar error porque queda el '%' solo.
	        inicTransiciones(Estado.INICIO_COMENT, Estado.INICIAL, retrocedeFuente, notificaErrorLexico);
	        //Salto de linea. Cuenta una nueva linea. No devuelve el ultimo leido pq se descartaria de todas formas.
	        maquinaEstados[Estado.INICIO_COMENT][Input.SALTO_LINEA] = new TransicionEstados(Estado.INICIAL,
	            cuentaSaltoLinea);
	        //'%'
	        maquinaEstados[Estado.INICIO_COMENT][Input.PORCENTAJE] = new TransicionEstados(Estado.CUERPO_COMENT);
	        //EOF. Voy directo al estado final. No hace falta devolver ultimo leido. Hay que notificar error porque queda
	        // el '%' solo.
	        NotificaError errorEOFComentario = new NotificaError("Simbolo no reconocido", aLexico, cFuente, true);
	        maquinaEstados[Estado.INICIO_COMENT][Input.EOF] = new TransicionEstados(Estado.FINAL, errorEOFComentario);

	        /* Estado 4 */
	        //Inputs validos.
	        inicTransiciones(Estado.CUERPO_COMENT, Estado.CUERPO_COMENT);
	        /* Salto de linea (fin comentario). Cuenta una nueva linea */
	        maquinaEstados[Estado.CUERPO_COMENT][Input.SALTO_LINEA] = new TransicionEstados(Estado.INICIAL,
	            cuentaSaltoLinea);
	        //EOF. Voy directo al estado final. No hace falta devolver ultimo leido.
	        maquinaEstados[Estado.CUERPO_COMENT][Input.EOF] = new TransicionEstados(Estado.FINAL);
	    }

	    /**
	     * Transiciones asociadas a la deteccion de comparadores y token asignacion.
	     */
	    private void inicCaminoComparadores(CodigoFuente cFuente, AccionSemantica retrocedeFuente,
	                                        AccionSemantica consumeChar) {
	        GeneraTokenParticular generaTokenParticular;

	        /* Token '<': Estado 0 */
	        maquinaEstados[Estado.INICIAL][Input.MENOR] = new TransicionEstados(Estado.COMP_MENOR);

	        /* Estado 5 */
	        generaTokenParticular = new GeneraTokenParticular(this, (short) '<');
	        //Inputs no definidos. Comparacion por menor estricto.
	        inicTransiciones(Estado.COMP_MENOR, Estado.FINAL, retrocedeFuente, generaTokenParticular);
	        //Salto de linea. Comparacion por menor estricto.
	        maquinaEstados[Estado.COMP_MENOR][Input.SALTO_LINEA] = new TransicionEstados(Estado.FINAL, generaTokenParticular,
	            cuentaSaltoLinea);
	        //EOF. Comparacion por menor estricto.
	        maquinaEstados[Estado.COMP_MENOR][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenParticular);
	        //'='. Comparacion por menor e igual.
	        generaTokenParticular = new GeneraTokenParticular(this, Parser.COMP_MENOR_IGUAL);
	        maquinaEstados[Estado.COMP_MENOR][Input.IGUAL] = new TransicionEstados(Estado.FINAL, consumeChar,
	            generaTokenParticular);

	        /* Token '>': Estado 0 */
	        maquinaEstados[Estado.INICIAL][Input.MAYOR] = new TransicionEstados(Estado.COMP_MAYOR);

	        /* Estado 6 */
	        generaTokenParticular = new GeneraTokenParticular(this, (short) '>');
	        //Inputs no definidos. Comparacion por mayor estricto.
	        inicTransiciones(Estado.COMP_MAYOR, Estado.FINAL, retrocedeFuente, generaTokenParticular);
	        //Salto de linea. Comparacion por mayor estricto.
	        maquinaEstados[Estado.COMP_MAYOR][Input.SALTO_LINEA] = new TransicionEstados(Estado.INICIAL,
	            generaTokenParticular, cuentaSaltoLinea);
	        //EOF. Comparacion por mayor estricto.
	        maquinaEstados[Estado.COMP_MAYOR][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenParticular);
	        //'='. Comparacion por mayor e igual.
	        generaTokenParticular = new GeneraTokenParticular(this, Parser.COMP_MAYOR_IGUAL);
	        maquinaEstados[Estado.COMP_MAYOR][Input.IGUAL] = new TransicionEstados(Estado.FINAL, consumeChar,
	            generaTokenParticular);

	        /* Token '!': Estado 0 */
	        maquinaEstados[Estado.INICIAL][Input.ADMIRACION] = new TransicionEstados(Estado.COMP_DISTINTO);

	        /* Estado 7 */
	        //Inputs invalidos. El simbolo '!' por si solo no hace nada, solo puede estar acompañado por un '='.
	        NotificaError errorSimboloInvalido = new NotificaError(
	            "El simbolo '!' por si solo no tiene ninguna funcion en el lenguaje", aLexico, cFuente, false);
	        inicTransiciones(Estado.COMP_DISTINTO, Estado.INICIAL, retrocedeFuente, errorSimboloInvalido);
	        //Salto de linea. Misma situacion que sentencia anterior.
	        maquinaEstados[Estado.COMP_DISTINTO][Input.SALTO_LINEA] = new TransicionEstados(Estado.INICIAL, cuentaSaltoLinea,
	            errorSimboloInvalido);
	        //EOF. Misma situacion que sentencia anterior.
	        maquinaEstados[Estado.COMP_DISTINTO][Input.EOF] = new TransicionEstados(Estado.INICIAL, errorSimboloInvalido);
	        //'='. Comparacion por distincion.
	        generaTokenParticular = new GeneraTokenParticular(this, Parser.COMP_DISTINTO);
	        maquinaEstados[Estado.COMP_DISTINTO][Input.IGUAL] = new TransicionEstados(Estado.FINAL, consumeChar,
	            generaTokenParticular);

	        /* Token '=': Estado 0 */
	        maquinaEstados[Estado.INICIAL][Input.IGUAL] = new TransicionEstados(Estado.SIGNO_IGUAL);

	        /* Estado 8 */
	        generaTokenParticular = new GeneraTokenParticular(this, (short) '=');
	        //Inputs no definidos. Asignacion.
	        inicTransiciones(Estado.SIGNO_IGUAL, Estado.FINAL, retrocedeFuente, generaTokenParticular);
	        //Salto de linea. Asignacion
	        maquinaEstados[Estado.SIGNO_IGUAL][Input.SALTO_LINEA] = new TransicionEstados(Estado.FINAL,
	            generaTokenParticular, cuentaSaltoLinea);
	        //EOF. Asignacion
	        maquinaEstados[Estado.SIGNO_IGUAL][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenParticular);
	        //'='. Comparacion por igualdad.
	        generaTokenParticular = new GeneraTokenParticular(this, Parser.COMP_IGUAL);
	        maquinaEstados[Estado.SIGNO_IGUAL][Input.IGUAL] = new TransicionEstados(Estado.FINAL, consumeChar,
	            generaTokenParticular);
	    }

	    /**
	     * Transiciones asociadas a la deteccion de constantes numericas.
	     */
	    private void inicCaminoCtesNum(AccionSemantica inicStringVacio, AccionSemantica concatenaChar,
	                                   AccionSemantica retrocedeFuente, AccionSemantica generaTokenUINT,
	                                   AccionSemantica consumeChar, AccionSemantica generaTokenDouble) {
	        /* Acciones semanticas usadas */
	        ParseBaseDouble parseBaseDouble = new ParseBaseDouble();
	        NotificaWarning warningFaltaSufijo = new NotificaWarning(
	            "Falto el sufijo '_ui' luego del numero. El numero fue tomado como un UINT", aLexico);
	        NotificaWarning warningSufijoInvalido = new NotificaWarning(
	            "El sufijo encontrado luego del numero no se corresponde con el estipulado para un numero UINT. " +
	                "El mismo fue tomado como un UINT", aLexico);
	        NotificaWarning warningFaltaExponente = new NotificaWarning(
	            "Falto el exponente del numero DOUBLE. El exponente es 0 por defecto", aLexico);

	        /* Estado 0. */
	        maquinaEstados[Estado.INICIAL][Input.DIGITO] = new TransicionEstados(Estado.CTE_PARTE_ENTERA, inicStringVacio,
	            concatenaChar);
	        maquinaEstados[Estado.INICIAL][Input.PUNTO] = new TransicionEstados(Estado.CTE_PARTE_DECIM, inicStringVacio,
	            concatenaChar);

	        /* Estado 9 */
	        //Inputs no reconocidos. El lexico "da por hecho" que es un UINT, asi se evita dar problemas al sintactico,
	        // pero genera un warning.
	        inicTransiciones(Estado.CTE_PARTE_ENTERA, Estado.FINAL, generaTokenUINT, retrocedeFuente, warningFaltaSufijo);
	        //Salto de linea. No devuelve el ultimo leido pq se descartaria de todas formas. Genera un warning por falta
	        // de sufijo.
	        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.SALTO_LINEA] = new TransicionEstados(Estado.FINAL, generaTokenUINT,
	            cuentaSaltoLinea, warningFaltaSufijo);
	        //Digitos.
	        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.DIGITO] = new TransicionEstados(Estado.CTE_PARTE_ENTERA,
	            concatenaChar);
	        //Guion bajo. Salto a deteccion de sufijo para UIs.
	        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.GUION_B] = new TransicionEstados(Estado.CTE_UI_SUF1);
	        //Punto ('.'). Salto a deteccion de parte decimal de doubles.
	        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.PUNTO] = new TransicionEstados(Estado.CTE_PARTE_DECIM,
	            concatenaChar);
	        //Letra 'd' minuscula. Salto a deteccion de parte exponencial de doubles. Como ya detecte toda la base la
	        // parseo.
	        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.D_MINUSC] = new TransicionEstados(Estado.CTE_PARTE_EXP_SIGNO,
	            parseBaseDouble, consumeChar);
	        //EOF. Crea un UINT pero genera un warning por falta de sufijo.
	        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenUINT,
	            warningFaltaSufijo);

	        /* Estado 10 */
	        //Inputs invalidos. Crea un UINT pero genera un warning por falta de sufijo.
	        inicTransiciones(Estado.CTE_UI_SUF1, Estado.FINAL, retrocedeFuente, generaTokenUINT, warningSufijoInvalido);
	        //Salto de linea. Crea un UINT pero genera un warning por falta de sufijo.
	        maquinaEstados[Estado.CTE_UI_SUF1][Input.SALTO_LINEA] = new TransicionEstados(Estado.FINAL, generaTokenUINT,
	            cuentaSaltoLinea, warningSufijoInvalido);
	        //Letra 'u' minuscula.
	        maquinaEstados[Estado.CTE_UI_SUF1][Input.U_MINUSC] = new TransicionEstados(Estado.CTE_UI_SUF2);
	        //Cualquier letra minuscula. "Tomo" la letra recibida como parte del sufijo.
	        maquinaEstados[Estado.CTE_UI_SUF1][Input.LETRA_MINUSC] = new TransicionEstados(Estado.CTE_UI_SUF1);
	        maquinaEstados[Estado.CTE_UI_SUF1][Input.D_MINUSC] = new TransicionEstados(Estado.CTE_UI_SUF1);
	        maquinaEstados[Estado.CTE_UI_SUF1][Input.I_MINUSC] = new TransicionEstados(Estado.CTE_UI_SUF1);
	        //EOF. Crea un UINT pero genera un warning por falta de sufijo.
	        maquinaEstados[Estado.CTE_UI_SUF1][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenUINT,
	            warningSufijoInvalido);

	        /* Estado 11 */
	        //Inputs invalidos. Crea un UINT pero genera un warning por falta de sufijo.
	        inicTransiciones(Estado.CTE_UI_SUF2, Estado.FINAL, retrocedeFuente, generaTokenUINT, warningSufijoInvalido);
	        //Salto de linea. Crea un UINT pero genera un warning por falta de sufijo.
	        maquinaEstados[Estado.CTE_UI_SUF2][Input.SALTO_LINEA] = new TransicionEstados(Estado.FINAL, generaTokenUINT,
	            cuentaSaltoLinea, warningSufijoInvalido);
	        //Letra 'i' minuscula.
	        maquinaEstados[Estado.CTE_UI_SUF2][Input.I_MINUSC] = new TransicionEstados(Estado.CTE_UI_SUF3);
	        //Cualquier letra minuscula. "Tomo" la letra recibida como parte del sufijo.
	        maquinaEstados[Estado.CTE_UI_SUF2][Input.LETRA_MINUSC] = new TransicionEstados(Estado.CTE_UI_SUF2);
	        maquinaEstados[Estado.CTE_UI_SUF2][Input.U_MINUSC] = new TransicionEstados(Estado.CTE_UI_SUF2);
	        maquinaEstados[Estado.CTE_UI_SUF2][Input.D_MINUSC] = new TransicionEstados(Estado.CTE_UI_SUF2);
	        //EOF. Crea un UINT pero genera un warning por falta de sufijo.
	        maquinaEstados[Estado.CTE_UI_SUF2][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenUINT,
	            warningSufijoInvalido);

	        /* Estado 12 */
	        //Cualquier input es valido (salvo letras minusculas). Ya se leyo el sufijo completo, se genera el token y se
	        // retrocede el fuente una posicion.
	        inicTransiciones(Estado.CTE_UI_SUF3, Estado.FINAL, retrocedeFuente, generaTokenUINT);
	        //Salto de linea. No devuelve ultimo leido porque se descartaria.
	        maquinaEstados[Estado.CTE_UI_SUF3][Input.SALTO_LINEA] = new TransicionEstados(Estado.FINAL, generaTokenUINT,
	            cuentaSaltoLinea);
	        //Cualquier letra minuscula. "Tomo" la letra recibida como parte del sufijo.
	        maquinaEstados[Estado.CTE_UI_SUF3][Input.LETRA_MINUSC] = new TransicionEstados(Estado.CTE_UI_SUF3);
	        maquinaEstados[Estado.CTE_UI_SUF3][Input.D_MINUSC] = new TransicionEstados(Estado.CTE_UI_SUF3);
	        maquinaEstados[Estado.CTE_UI_SUF3][Input.I_MINUSC] = new TransicionEstados(Estado.CTE_UI_SUF3);
	        maquinaEstados[Estado.CTE_UI_SUF3][Input.U_MINUSC] = new TransicionEstados(Estado.CTE_UI_SUF3);
	        //EOF.
	        maquinaEstados[Estado.CTE_UI_SUF3][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenUINT);

	        /* Estado 13 */
	        //Inputs no reconocidos. Como ya detecte toda la base la parseo. No hay exponente, por lo que se puede
	        // generar el token.
	        inicTransiciones(Estado.CTE_PARTE_DECIM, Estado.FINAL, retrocedeFuente, parseBaseDouble, generaTokenDouble);
	        //Salto de linea. Como ya detecte toda la base la parseo. No hay exponente, por lo que se puede generar el
	        // token.
	        maquinaEstados[Estado.CTE_PARTE_DECIM][Input.SALTO_LINEA] = new TransicionEstados(Estado.FINAL, parseBaseDouble,
	            generaTokenDouble, cuentaSaltoLinea);
	        //Digitos
	        maquinaEstados[Estado.CTE_PARTE_DECIM][Input.DIGITO] = new TransicionEstados(Estado.CTE_PARTE_DECIM,
	            concatenaChar);
	        //Letra 'd' minuscula. Salto a deteccion de parte exponencial de doubles. Como ya detecte toda la base la
	        // parseo.
	        maquinaEstados[Estado.CTE_PARTE_DECIM][Input.D_MINUSC] = new TransicionEstados(Estado.CTE_PARTE_EXP_SIGNO,
	            parseBaseDouble, consumeChar);
	        //EOF. Como ya detecte toda la base la parseo. No hay exponente, por lo que se puede generar el token.
	        maquinaEstados[Estado.CTE_PARTE_DECIM][Input.EOF] = new TransicionEstados(Estado.FINAL, parseBaseDouble,
	            generaTokenDouble);

	        /* Estado 14 */
	        //Inputs no reconocidos. El exponente por defecto es 0, pero genera un warning. El n° ya esta completo, por
	        // lo que se puede generar el token.
	        inicTransiciones(Estado.CTE_PARTE_EXP_SIGNO, Estado.FINAL, retrocedeFuente, generaTokenDouble,
	            warningFaltaExponente);
	        //Salto de linea. El exponente por defecto es 0, pero genera un warning.
	        maquinaEstados[Estado.CTE_PARTE_EXP_SIGNO][Input.SALTO_LINEA] = new TransicionEstados(Estado.FINAL,
	            generaTokenDouble, warningFaltaExponente, cuentaSaltoLinea);
	        //'+'
	        maquinaEstados[Estado.CTE_PARTE_EXP_SIGNO][Input.SUMA] = new TransicionEstados(Estado.CTE_PARTE_EXP_VALOR,
	            concatenaChar);
	        //'-'
	        maquinaEstados[Estado.CTE_PARTE_EXP_SIGNO][Input.GUION] = new TransicionEstados(Estado.CTE_PARTE_EXP_VALOR,
	            concatenaChar);
	        //Digitos. El exponente sera positivo.
	        maquinaEstados[Estado.CTE_PARTE_EXP_SIGNO][Input.DIGITO] = new TransicionEstados(Estado.CTE_PARTE_EXP_VALOR,
	            concatenaChar);
	        //EOF.
	        maquinaEstados[Estado.CTE_PARTE_EXP_SIGNO][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenDouble,
	            warningFaltaExponente);

	        /* Estado 15 */
	        //Inputs no reconocidos.
	        inicTransiciones(Estado.CTE_PARTE_EXP_VALOR, Estado.FINAL, retrocedeFuente, generaTokenDouble);
	        //Salto de linea.
	        maquinaEstados[Estado.CTE_PARTE_EXP_VALOR][Input.SALTO_LINEA] = new TransicionEstados(Estado.FINAL,
	            generaTokenDouble, cuentaSaltoLinea);
	        //Digitos.
	        maquinaEstados[Estado.CTE_PARTE_EXP_VALOR][Input.DIGITO] = new TransicionEstados(Estado.CTE_PARTE_EXP_VALOR,
	            concatenaChar);
	        //EOF.
	        maquinaEstados[Estado.CTE_PARTE_EXP_VALOR][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenDouble);
	    }

	    /**
	     * Transiciones asociadas a la deteccion de constantes numericas. Nota: Ante un salto de linea, no se guarda ni el
	     * guion ni el propio salto de linea.
	     */
	    private void inicCaminoCadenas(CodigoFuente cFuente, AccionSemantica inicStringVacio, AccionSemantica concatenaChar,
	                                   AccionSemantica generaTokenCadena) {
	        /* Estado 0 */
	        maquinaEstados[Estado.INICIAL][Input.COMILLA] = new TransicionEstados(Estado.CADENA, inicStringVacio,
	            concatenaChar);

	        /* Estado 16 */
	        //Inputs validos. Cualquier caracter que se lee se mete en la cadena.
	        inicTransiciones(Estado.CADENA, Estado.CADENA, concatenaChar);
	        //'"'. Fin de cadena.
	        maquinaEstados[Estado.CADENA][Input.COMILLA] = new TransicionEstados(Estado.FINAL, concatenaChar,
	            generaTokenCadena);
	        //Hay un salto de linea.
	        CheckSaltoLinea checkSaltoLinea = new CheckSaltoLinea(cFuente, this);
	        maquinaEstados[Estado.CADENA][Input.SALTO_LINEA] = new TransicionEstados(Estado.CADENA, cuentaSaltoLinea,
	            checkSaltoLinea);
	        //EOF. Queda la cadena abierta, por lo que hay que notificar un error.
	        NotificaError errorCadenaAbierta = new NotificaError("Se llego al EOF y la cadena quedo abierta", aLexico,
	            cFuente, false);
	        maquinaEstados[Estado.CADENA][Input.EOF] = new TransicionEstados(Estado.FINAL, errorCadenaAbierta);
	    }
}
