package compilador.MaquinaEstados;


import compilador.*;
import compilador.Asemanticas.*;

public class MaquinaEstados {
		private final TransicionEstados[][] maquinaEstados = new TransicionEstados[Estado.TOTAL_ESTADOS][Input.TOTAL_INPUTS]; //[filas][columnas].
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
	    private void inicMaquinaEstados(CodigoFuente cFuente, TablaSimbolos tablaS, TablaPalabrasReserv tablaPR) { /// Inicializo Acciones semanticas


			GeneraULONG generatokenUl= new GeneraULONG (this, tablaS, (short)262 /*Parser.CTE_UINT*/);
			GeneraDouble generaDouble = new GeneraDouble(this, tablaS, (short)263 /*Parser.CTE_DOUBLE*/);
			NotError notificaELexico = new NotError("Simbolo no reconocido", aLexico, cFuente, true);
			GeneraTokenParticular generarEOF = new GeneraTokenParticular(this, AnalizadorLex.T_EOF);
			GeneraTokenTSimbolos generaTokenId = new GeneraTokenTSimbolos(this, tablaS, (short) 257/*Parser.ID*/);
	        GeneraTokenTSimbolos generaTokenCadena = new GeneraTokenTSimbolos(this, tablaS,(short)264 /*Parser.CADENA*/);
	        GeneraTPr generaTokenPR = new GeneraTPr(this);
	        IgnoraC consumeChar = new IgnoraC();
			InicStringVacio inicStringVacio = new InicStringVacio();
			ConcChar concatenaChar = new ConcChar(cFuente);
			TruncId truncaId = new TruncId(aLexico);
			Retrocede_Fuente retrocedeFuente = new Retrocede_Fuente(cFuente);


	        /* Estados y transiciones */
	        inicTransiciones(Estado.INICIAL, Estado.INICIAL, notificaELexico);
	        maquinaEstados[Estado.INICIAL][Input.DESCARTABLE] = new TransicionEstados(Estado.INICIAL);
	        maquinaEstados[Estado.INICIAL][Input.SALTO_LINEA] = new TransicionEstados(Estado.INICIAL,
	            cuentaSaltoLinea); //Salto de linea.
	        inicCaminoLiterales(cFuente);
	        inicCaminoIds(inicStringVacio, concatenaChar, truncaId, generaTokenId, retrocedeFuente);
	        inicCaminoPRs(inicStringVacio, concatenaChar, generaTokenPR, retrocedeFuente);
	        inicCaminoComentario(cFuente, retrocedeFuente, notificaELexico);
	        inicCaminoComparadores(cFuente, retrocedeFuente, consumeChar);
	        inicCaminoCtesNum(inicStringVacio, concatenaChar, retrocedeFuente, generatokenUl, consumeChar,
	            generaDouble);
	        inicCaminoCadenas(cFuente, inicStringVacio, concatenaChar, generaTokenCadena);
	        maquinaEstados[Estado.INICIAL][Input.EOF] = new TransicionEstados(Estado.FINAL, generarEOF);

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
	    public void cambiar(char charInput) {
			// simula las transiciones de estados
	        int codigoInput = Input.charToInt(charInput);

	        TransicionEstados TransicionEstados = maquinaEstados[estadoActual][codigoInput];

	        estadoActual = TransicionEstados.siguienteEstado();

	        TransicionEstados.ejecutarAccionSemantica();
	    }

	    /**
	     * Solo se ejecuta al alcanzar el EOF del codigo fuente. El funcionamiento es el mismo que transicionar(char).
	     */
	    public void cambiarEOF() {
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

	    private void inicCaminoLiterales(CodigoFuente cFuente) {
			// inicializo las transiciones asociadas de los identificadores
	        GeneraTokenLiteral generaTokenL= new GeneraTokenLiteral(this, cFuente);
	        maquinaEstados[Estado.INICIAL][Input.SUMA] = new TransicionEstados(Estado.FINAL, generaTokenL);
	        maquinaEstados[Estado.INICIAL][Input.GUION] = new TransicionEstados(Estado.FINAL, generaTokenL);
	        maquinaEstados[Estado.INICIAL][Input.MULTIPL] = new TransicionEstados(Estado.FINAL, generaTokenL);
	        maquinaEstados[Estado.INICIAL][Input.PARENT_A] = new TransicionEstados(Estado.FINAL, generaTokenL);
	        maquinaEstados[Estado.INICIAL][Input.PARENT_C] = new TransicionEstados(Estado.FINAL, generaTokenL);
	        maquinaEstados[Estado.INICIAL][Input.COMA] = new TransicionEstados(Estado.FINAL, generaTokenL);
	        maquinaEstados[Estado.INICIAL][Input.PUNTO_COMA] = new TransicionEstados(Estado.FINAL, generaTokenL);
	    }


	    private void inicCaminoIds(AccionSemantica inicStringVacio, AccionSemantica concatenaChar, AccionSemantica truncaId,
	                               AccionSemantica generaTokenId, AccionSemantica retrocedeFuente) {
	        // matriz en fila 0
	        maquinaEstados[Estado.INICIAL][Input.U_MINUSC] = new TransicionEstados(Estado.DETECCION_ID, inicStringVacio, concatenaChar);
	        maquinaEstados[Estado.INICIAL][Input.L_MINUSC] = new TransicionEstados(Estado.DETECCION_ID, inicStringVacio, concatenaChar);
	        maquinaEstados[Estado.INICIAL][Input.LETRA_MINUSC] = new TransicionEstados(Estado.DETECCION_ID, inicStringVacio, concatenaChar);

	        // matriz en fila 1 Deteccion de Id

	        inicTransiciones(Estado.DETECCION_ID, Estado.FINAL, truncaId, generaTokenId, retrocedeFuente);
			// Entradas no reconocidas por el compilador
	        maquinaEstados[Estado.DETECCION_ID][Input.SALTO_LINEA] = new TransicionEstados(Estado.FINAL, truncaId, generaTokenId, cuentaSaltoLinea);
			// Interaccion en salto de linea

			//Letras minusculas.
	        /**maquinaEstados[Estado.DETECCION_ID][Input.D_MINUSC] = new TransicionEstados(Estado.DETECCION_ID, concatenaChar);*/

	        maquinaEstados[Estado.DETECCION_ID][Input.U_MINUSC] = new TransicionEstados(Estado.DETECCION_ID, concatenaChar);
	        maquinaEstados[Estado.DETECCION_ID][Input.L_MINUSC] = new TransicionEstados(Estado.DETECCION_ID, concatenaChar);
	        maquinaEstados[Estado.DETECCION_ID][Input.LETRA_MINUSC] = new TransicionEstados(Estado.DETECCION_ID, concatenaChar);
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
	        // Estado 0
	        maquinaEstados[Estado.INICIAL][Input.LETRA_MAYUS] = new TransicionEstados(Estado.DETECCION_PR, inicStringVacio,
	            concatenaChar);

	        // Estado 1

	        inicTransiciones(Estado.DETECCION_PR, Estado.FINAL, generaTokenPR, retrocedeFuente);
			// Inputs no reconocidos

	        maquinaEstados[Estado.DETECCION_PR][Input.SALTO_LINEA] = new TransicionEstados(Estado.FINAL, generaTokenPR,
	            cuentaSaltoLinea);
			//Salto de linea. Cuento la nueva linea y descarto el ultimo valore leido

	        maquinaEstados[Estado.DETECCION_PR][Input.LETRA_MAYUS] = new TransicionEstados(Estado.DETECCION_PR,
	            concatenaChar);
			//Letras mayusculas.

	        maquinaEstados[Estado.DETECCION_PR][Input.GUION_B] = new TransicionEstados(Estado.DETECCION_PR, concatenaChar);
			//Guion bajo.

	        maquinaEstados[Estado.DETECCION_PR][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenPR);
			//EOF. Voy directo al estado final. No hace falta devolver ultimo leido.

	    }

	    /**
	     * Transiciones asociadas a la deteccion y descarte de comentarios unilinea.
	     */
	    private void inicCaminoComentario(CodigoFuente cFuente, AccionSemantica retrocedeFuente,
	                                      AccionSemantica notificaErrorLexico) {

			/// Estado 0
	        maquinaEstados[Estado.INICIAL][Input.DIV] = new TransicionEstados(Estado.COMENTARIO);// Comantario es comentario y division
			
	        // Estado 3 , posible division o comentario 

	        maquinaEstados[Estado.COMENTARIO][Input.DIV] = new TransicionEstados(Estado.COMENTARIO2);
			maquinaEstados[Estado.COMENTARIO][Input.OTRO] = new TransicionEstados(Estado.COMENTARIO2,retrocedeFuente,new GeneraTokenParticular(this,(short)Input.OTRO));
	        
			//Estado 4
			
	        maquinaEstados[Estado.COMENTARIO2][Input.OTRO] = new TransicionEstados(Estado.FINAL);// cuerpo del comentario 
			maquinaEstados[Estado.COMENTARIO2][Input.DIV]= new TransicionEstados(Estado.COMENTARIO3);// posible salida del comentario

	        NotError errorEOF = new NotError("Simbolo no reconocido por el compilador", aLexico, cFuente, true);
	        maquinaEstados[Estado.COMENTARIO][Input.EOF] = new TransicionEstados(Estado.FINAL, errorEOF);
			// si tengo un comentario y EOF retorno termino y retorno el error

			// Estado 5 
			
			maquinaEstados[Estado.COMENTARIO3][Input.OTRO]= new TransicionEstados(Estado.COMENTARIO2);// sigue siendo cuerpo del comentario 
	        maquinaEstados[Estado.COMENTARIO3][Input.DIV]= new TransicionEstados(Estado.INICIAL); // fin del comentario 
			maquinaEstados[Estado.COMENTARIO3][Input.EOF] = new TransicionEstados(Estado.FINAL);
			
	    }

	    /**
	     * Transiciones asociadas a la deteccion de comparadores y token asignacion.
	     */
	    private void inicCaminoComparadores(CodigoFuente cFuente, AccionSemantica retrocedeFuente,
	                                        AccionSemantica consumeChar) {
			GeneraTokenParticular tokenparticular;
			// Estado 0

			// Token '<':
			maquinaEstados[Estado.INICIAL][Input.MENOR] = new TransicionEstados(Estado.DISTYMENOR);// para mi esta mal

			// Token '>' o '='
			maquinaEstados[Estado.INICIAL][Input.MAYOR] = new TransicionEstados(Estado.COMPYMAYOR);
			maquinaEstados[Estado.INICIAL][Input.IGUAL] = new TransicionEstados(Estado.COMPYMAYOR);


			// Estado 6
			GeneraTokenParticular generaTokenPR = new GeneraTokenParticular(this,(short) '>') ;

			maquinaEstados[Estado.COMPYMAYOR][Input.IGUAL] = new TransicionEstados(Estado.FINAL,consumeChar);
			maquinaEstados[Estado.COMPYMAYOR][Input.OTRO] = new TransicionEstados(Estado.FINAL,retrocedeFuente,generaTokenPR);
			NotError errorEOF = new NotError("Simbolo no reconocido por el compilador", aLexico, cFuente, true);
			maquinaEstados[Estado.COMPYMAYOR][Input.EOF] = new TransicionEstados(Estado.FINAL, errorEOF);


			//Estado 7
			GeneraTokenParticular token = new GeneraTokenParticular(this,(short)'<');

			maquinaEstados[Estado.DISTYMENOR][Input.IGUAL] = new TransicionEstados(Estado.FINAL, consumeChar);
			maquinaEstados[Estado.DISTYMENOR][Input.MAYOR] = new TransicionEstados(Estado.FINAL, consumeChar);
			maquinaEstados[Estado.DISTYMENOR][Input.OTRO] = new TransicionEstados(Estado.FINAL, retrocedeFuente,token);
			maquinaEstados[Estado.DISTYMENOR][Input.EOF] = new TransicionEstados(Estado.COMPYMAYOR, errorEOF);


		}

	    /**
	     * Transiciones asociadas a la deteccion de constantes numericas.
	     */
	    private void inicCaminoCtesNum(AccionSemantica inicStringVacio, AccionSemantica concatenaChar,
	                                   AccionSemantica retrocedeFuente, AccionSemantica generaTokenULONG,
	                                   AccionSemantica consumeChar, AccionSemantica generaTokenDouble) {
	        // Inicializo los errores
	        ParseBaseDouble parseBaseDouble = new ParseBaseDouble();
	        NotWarning faltaSufijo = new NotWarning("Falto el sufijo 'ul' al final del numero, el numero fue tomado como ULONG ", aLexico);
	        NotWarning sufijoInvalido = new NotWarning("se encontro un sufijo que no es valido como ULONG, este se tomara como ULONG", aLexico);
	        NotWarning sinExponente = new NotWarning("no hay exponente, este sera tomado como 0 por defecto", aLexico);

	        //Estado 0
	        maquinaEstados[Estado.INICIAL][Input.DIGITO] = new TransicionEstados(Estado.D_U_1, inicStringVacio,
	            concatenaChar);
	        maquinaEstados[Estado.INICIAL][Input.PUNTO] = new TransicionEstados(Estado.D_U_4, inicStringVacio,
	            concatenaChar);

	        // Estado 12
	        //Con las salidas no reconocidas el compilador lo toma como ULONG y genera un warning
	        inicTransiciones(Estado.D_U_1, Estado.FINAL, generaTokenULONG, retrocedeFuente, faltaSufijo);
	        //si hay un salto de linea, genero Warning por falta
	        maquinaEstados[Estado.D_U_1][Input.SALTO_LINEA] = new TransicionEstados(Estado.FINAL, generaTokenULONG,
	            cuentaSaltoLinea, faltaSufijo);
	        //Digitos.
	        maquinaEstados[Estado.D_U_1][Input.DIGITO] = new TransicionEstados(Estado.D_U_1,
	            concatenaChar);

	        //Punto ('.'). Salto a deteccion de parte decimal de doubles.
	        maquinaEstados[Estado.D_U_1][Input.PUNTO] = new TransicionEstados(Estado.D_U_3,
	            concatenaChar);

			// U : empieza camino correcto ULONG
			maquinaEstados[Estado.D_U_1][Input.U_MINUSC] = new TransicionEstados(Estado.D_U_2);

			// OTRO
			maquinaEstados[Estado.D_U_1][Input.OTRO] = new TransicionEstados(Estado.FINAL,
					concatenaChar);


	        //EOF. Crea un UINT pero genera un warning por falta de sufijo.
	        maquinaEstados[Estado.D_U_1][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenULONG,
	            faltaSufijo);

	        // Estado 13
	        // Crea un ULONG  por defecto pero genera un warning por falta de sufijo.
	        inicTransiciones(Estado.D_U_2, Estado.FINAL, retrocedeFuente, generaTokenULONG, sufijoInvalido);
	        //Salto de linea. Crea un UINT pero genera un warning por falta de sufijo.
	        maquinaEstados[Estado.D_U_2][Input.SALTO_LINEA] = new TransicionEstados(Estado.FINAL, generaTokenULONG,
	            cuentaSaltoLinea, sufijoInvalido);
	        //Letra 'l' minuscula.
	        maquinaEstados[Estado.D_U_2][Input.L_MINUSC] = new TransicionEstados(Estado.D_U_6); // no hace nada ya que la parte ul del sufijo solo me importa para clasificar

			//Cualquier otra cosa, retorno numero como ULONG y pongo aviso el warning
	        maquinaEstados[Estado.D_U_2][Input.OTRO] = new TransicionEstados(Estado.FINAL,generaTokenULONG,sufijoInvalido);

	        //EOF. Crea un UINT pero genera un warning por falta de sufijo.
	        maquinaEstados[Estado.D_U_2][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenULONG, sufijoInvalido);

	        // Estado 17
			maquinaEstados[Estado.D_U_6][Input.OTRO] = new TransicionEstados(Estado.FINAL, generaTokenULONG, sufijoInvalido);
			maquinaEstados[Estado.D_U_6][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenULONG, sufijoInvalido);


			// Estado 14
			maquinaEstados[Estado.D_U_3][Input.DIGITO] = new TransicionEstados(Estado.D_U_3,concatenaChar);
			maquinaEstados[Estado.D_U_3][Input.E_EXP] = new TransicionEstados(Estado.D_U_4);// no se si le interesa o no
			maquinaEstados[Estado.D_U_3][Input.DIGITO] = new TransicionEstados(Estado.D_U_3,concatenaChar);
			maquinaEstados[Estado.D_U_3][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenULONG,sinExponente);
			maquinaEstados[Estado.D_U_3][Input.OTRO] = new TransicionEstados(Estado.FINAL,retrocedeFuente,generaTokenDouble);



			// Estado 15
			maquinaEstados[Estado.D_U_4][Input.DIGITO] = new TransicionEstados(Estado.D_U_4,concatenaChar);
			maquinaEstados[Estado.D_U_4][Input.SUMA] = new TransicionEstados(Estado.D_U_5,concatenaChar);
			maquinaEstados[Estado.D_U_4][Input.GUION] = new TransicionEstados(Estado.D_U_5,concatenaChar);
			maquinaEstados[Estado.D_U_4][Input.OTRO] = new TransicionEstados(Estado.FINAL,retrocedeFuente,generaTokenDouble);
			maquinaEstados[Estado.D_U_4][Input.EOF] = new TransicionEstados(Estado.FINAL, generaTokenDouble,sinExponente);

			// Estado 16
			maquinaEstados[Estado.D_U_5][Input.DIGITO] = new TransicionEstados(Estado.D_U_5,concatenaChar);
			maquinaEstados[Estado.D_U_5][Input.OTRO] = new TransicionEstados(Estado.FINAL, generaTokenDouble, retrocedeFuente, sinExponente);


			/** falta rellenar cadenas no reconocidas */
	    }

	    /**
	     * Transiciones asociadas a la deteccion de constantes numericas. Nota: Ante un salto de linea, no se guarda ni el
	     * guion ni el propio salto de linea.
	     */
	    private void inicCaminoCadenas(CodigoFuente cFuente, AccionSemantica inicStringVacio, AccionSemantica concatenaChar,
	                                   AccionSemantica generaTokenCadena) {
	        // Estado 0
	        maquinaEstados[Estado.INICIAL][Input.PORCENTAJE] = new TransicionEstados(Estado.UNALINEA, inicStringVacio,
	            concatenaChar);

	        // Estado 11
			maquinaEstados[Estado.UNALINEA][Input.OTRO] = new TransicionEstados(Estado.UNALINEA,
					concatenaChar);
			ChequeoSLinea salta = new ChequeoSLinea(this,cFuente);
			NotError saltoInvalido = new NotError("En esta seccion del codigo no se puede realizar un salto",aLexico,cFuente,false);
			maquinaEstados[Estado.UNALINEA][Input.PORCENTAJE] = new TransicionEstados(Estado.FINAL,generaTokenCadena,saltoInvalido,cuentaSaltoLinea);

	        //finaliza la cadena con porcentaje
	        maquinaEstados[Estado.UNALINEA][Input.PORCENTAJE] = new TransicionEstados(Estado.FINAL, concatenaChar,generaTokenCadena);
	        //Hay un salto de linea.
	        //EOF. Queda la cadena abierta, por lo que hay que notificar un error.
	        NotError errorCadenaAbierta = new NotError("Se llego al EOF y la cadena quedo abierta", aLexico,
	            cFuente, false);
	        maquinaEstados[Estado.UNALINEA][Input.EOF] = new TransicionEstados(Estado.FINAL, errorCadenaAbierta);
	    }
}
