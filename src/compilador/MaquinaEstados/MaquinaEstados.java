package compilador.MaquinaEstados;


import compilador.*;
import compilador.Asemanticas.*;
import compilador.SIMBOLOS.TablaSimbolos;
import compilador.util.CodigoFuente;
import compilador.util.TablaPalabrasReserv;

public class MaquinaEstados {
		private final AristaEstado[][] maquinaEstados = new AristaEstado[Estado.TOTAL_ESTADOS][Input.TOTAL_INPUTS]; //[filas][columnas].
	    private final AnalizadorLex aLexico; //Permite agregar tokens a medida que se generan.
	    private final CuentaSaltoLinea cuentaSaltoLinea = new CuentaSaltoLinea(); //Permite saber la linea actual
	    private int estadoActual = Estado.INICIAL;


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
			GeneraTokenLiteral tokenLiteral =new GeneraTokenLiteral(this,cFuente);


	        //  Inicializacion de estados y transiciones
	        inicTransiciones(Estado.INICIAL, Estado.INICIAL, notificaELexico); // Error por defecto del lexico
	        maquinaEstados[Estado.INICIAL][Input.DESCARTABLE] = new AristaEstado(Estado.INICIAL);
	        maquinaEstados[Estado.INICIAL][Input.SALTO_LINEA] = new AristaEstado(Estado.INICIAL,
	            cuentaSaltoLinea); //Salto de linea.
	        inicCaminoLiterales(cFuente);
	        inicCaminoIds(inicStringVacio, concatenaChar, truncaId, generaTokenId, retrocedeFuente);
	        inicCaminoPRs(inicStringVacio, concatenaChar, generaTokenPR, retrocedeFuente);
	        inicCaminoComentario(cFuente, retrocedeFuente, notificaELexico);
	        inicCaminoComparadores(cFuente, retrocedeFuente, consumeChar);
	        inicCaminoCtesNum(inicStringVacio, concatenaChar, retrocedeFuente, generatokenUl, consumeChar,
	            generaDouble);
	        inicCaminoCadenas(cFuente, inicStringVacio, concatenaChar, generaTokenCadena);
			inicCaminoOr(cFuente,consumeChar,retrocedeFuente,tokenLiteral);
			inicCaminoop(cFuente,consumeChar,retrocedeFuente,tokenLiteral);
			inicCaminoAnd(cFuente,consumeChar,retrocedeFuente,tokenLiteral);

	        maquinaEstados[Estado.INICIAL][Input.EOF] = new AristaEstado(Estado.FINAL, generarEOF);

	    }


	    public boolean esEstadoFinal() {
	        return estadoActual == Estado.FINAL;
	    } // retorna true cuando la maquina termine de leer

	    public void reiniciar() {
	        estadoActual = 0;
	    }// reinicia la maquina

	    public void setVariablesSintactico(short token, String lexema) {
	        aLexico.setVariablesSintactico(token, lexema); } // Retorna eñ lexema  la linea de codigo actual


	    public int getLineaActual() {
	        return cuentaSaltoLinea.getCantLineas();
	    } // retorna la linea de codigo en la cual estoy parado


	    public void cambiar(char input) { // simula las transiciones de estados
	        int codigoInput = Input.charToInt(input);

			AristaEstado AristaEstado = maquinaEstados[estadoActual][codigoInput];

	        estadoActual = AristaEstado.getSigEstado();

	        AristaEstado.ejecutar();
	    }


	    public void cambiarEOF() { //
			AristaEstado TransicionEstados = maquinaEstados[estadoActual][Input.EOF];
	        TransicionEstados.ejecutar();
	        aLexico.setVariablesSintactico(AnalizadorLex.T_EOF, ""); //Genera el token asociado al EOF ('0').
	        estadoActual = Estado.FINAL; //Finalizo ejecucion
	    }

	    // Inicializa las trancisiones de todos los estados por defecto que tiene esa fila de la matriz
	    private void inicTransiciones(int estadoOrigen, int estadoDestino, AccionSemantica... accionesSemanticas) {
	        for (int input = 0; input < Input.TOTAL_INPUTS; input++)
	            maquinaEstados[estadoOrigen][input] = new AristaEstado(estadoDestino, accionesSemanticas);
	    }

	// inicializo las transiciones asociadas de los identificadores
	    private void inicCaminoLiterales(CodigoFuente cFuente) {
	        GeneraTokenLiteral generaTokenL= new GeneraTokenLiteral(this, cFuente);
	        maquinaEstados[Estado.INICIAL][Input.SUMA] = new AristaEstado(Estado.FINAL, generaTokenL);
	        maquinaEstados[Estado.INICIAL][Input.GUION] = new AristaEstado(Estado.FINAL, generaTokenL);
	        maquinaEstados[Estado.INICIAL][Input.MULTIPL] = new AristaEstado(Estado.FINAL, generaTokenL);
	        maquinaEstados[Estado.INICIAL][Input.PARENT_A] = new AristaEstado(Estado.FINAL, generaTokenL);
	        maquinaEstados[Estado.INICIAL][Input.PARENT_C] = new AristaEstado(Estado.FINAL, generaTokenL);
	        maquinaEstados[Estado.INICIAL][Input.COMA] = new AristaEstado(Estado.FINAL, generaTokenL);
	        maquinaEstados[Estado.INICIAL][Input.PUNTO_COMA] = new AristaEstado(Estado.FINAL, generaTokenL);
	    }


	    private void inicCaminoIds(AccionSemantica inicStringVacio, AccionSemantica concatenaChar, AccionSemantica truncaId,
	                               AccionSemantica generaTokenId, AccionSemantica retrocedeFuente) {


	        // Estado 0
	        maquinaEstados[Estado.INICIAL][Input.U_MINUSC] = new AristaEstado(Estado.DETECCION_ID, inicStringVacio, concatenaChar);
	        maquinaEstados[Estado.INICIAL][Input.L_MINUSC] = new AristaEstado(Estado.DETECCION_ID, inicStringVacio, concatenaChar);
	        maquinaEstados[Estado.INICIAL][Input.LETRA_MINUSC] = new AristaEstado(Estado.DETECCION_ID, inicStringVacio, concatenaChar);

	        // Estado  1 Deteccion de Id
			// Entradas no reconocidas por el compilador
	        inicTransiciones(Estado.DETECCION_ID, Estado.FINAL, truncaId, generaTokenId, retrocedeFuente);
			// Interaccion en salto de linea
	        maquinaEstados[Estado.DETECCION_ID][Input.SALTO_LINEA] = new AristaEstado(Estado.FINAL, truncaId, generaTokenId, cuentaSaltoLinea);


			//Letras minusculas.
	        maquinaEstados[Estado.DETECCION_ID][Input.U_MINUSC] = new AristaEstado(Estado.DETECCION_ID, concatenaChar);
	        maquinaEstados[Estado.DETECCION_ID][Input.L_MINUSC] = new AristaEstado(Estado.DETECCION_ID, concatenaChar);
	        maquinaEstados[Estado.DETECCION_ID][Input.LETRA_MINUSC] = new AristaEstado(Estado.DETECCION_ID, concatenaChar);
			// DIGITOS
	        maquinaEstados[Estado.DETECCION_ID][Input.DIGITO] = new AristaEstado(Estado.DETECCION_ID, concatenaChar);
	        //GUION BAJO
	        maquinaEstados[Estado.DETECCION_ID][Input.GUION_B] = new AristaEstado(Estado.DETECCION_ID, concatenaChar);
	        //EOF. Voy directo al estado final. No hace falta devolver ultimo leido.
	        maquinaEstados[Estado.DETECCION_ID][Input.EOF] = new AristaEstado(Estado.FINAL, truncaId, generaTokenId);
	    }


	    private void inicCaminoPRs(AccionSemantica inicStringVacio, AccionSemantica concatenaChar,
	                               AccionSemantica generaTokenPR, AccionSemantica retrocedeFuente) {
	        // Estado 0
	        maquinaEstados[Estado.INICIAL][Input.LETRA_MAYUS] = new AristaEstado(Estado.DETECCION_PR, inicStringVacio,
	            concatenaChar);

	        // Estado 1
			// Entradas que no fueron reconocidas
	        inicTransiciones(Estado.DETECCION_PR, Estado.FINAL, generaTokenPR, retrocedeFuente);

			//Salto de linea. Cuento la nueva linea y descarto el ultimo valore leido
	        maquinaEstados[Estado.DETECCION_PR][Input.SALTO_LINEA] = new AristaEstado(Estado.FINAL, generaTokenPR,
	            cuentaSaltoLinea);

  			//Letras mayusculas.
	        maquinaEstados[Estado.DETECCION_PR][Input.LETRA_MAYUS] = new AristaEstado(Estado.DETECCION_PR,
	            concatenaChar);

			//Guion bajo.
	        maquinaEstados[Estado.DETECCION_PR][Input.GUION_B] = new AristaEstado(Estado.DETECCION_PR, concatenaChar);

			//EOF. Voy directo al estado final. No hace falta devolver ultimo leido.
	        maquinaEstados[Estado.DETECCION_PR][Input.EOF] = new AristaEstado(Estado.FINAL, generaTokenPR);


	    }


	    private void inicCaminoComentario(CodigoFuente cFuente, AccionSemantica retrocedeFuente,
	                                      AccionSemantica notificaErrorLexico) {

			/// Estado 0
	        maquinaEstados[Estado.INICIAL][Input.DIV] = new AristaEstado(Estado.COMENTARIO);//inicio transicion de comentario o division
			
	        // Estado 3 , posible division o comentario
			GeneraTokenParticular token = new GeneraTokenParticular(this,(short)'/');
			inicTransiciones(Estado.COMENTARIO, Estado.FINAL, retrocedeFuente,token);// si viene otra cosa genero el /

			maquinaEstados[Estado.COMENTARIO][Input.DIV] = new AristaEstado(Estado.COMENTARIO2);


			//Estado 4

			inicTransiciones(Estado.COMENTARIO, Estado.COMENTARIO); // cuerpo del comentario, no realizo ninguna accion

			maquinaEstados[Estado.COMENTARIO2][Input.DIV]= new AristaEstado(Estado.COMENTARIO3);// posible salida del comentario

	        NotError errorEOF = new NotError("Simbolo no reconocido por el compilador", aLexico, cFuente, true);
	        maquinaEstados[Estado.COMENTARIO][Input.EOF] = new AristaEstado(Estado.FINAL, errorEOF);
			// si tengo un comentario y EOF retorno termino y retorno el error

			// Estado 5 
			inicTransiciones(Estado.COMENTARIO3, Estado.COMENTARIO2); // sigue siendo cuerpo del comentario
	        maquinaEstados[Estado.COMENTARIO3][Input.DIV]= new AristaEstado(Estado.INICIAL); // fin del comentario

			NotWarning wareof =new NotWarning("se termino la ejecucion dentro de un comentario",aLexico);
			maquinaEstados[Estado.COMENTARIO3][Input.EOF] = new AristaEstado(Estado.FINAL);
			
	    }


	    private void inicCaminoComparadores(CodigoFuente cFuente, AccionSemantica retrocedeFuente,
	                                        AccionSemantica consumeChar) {
			GeneraTokenParticular tokenparticular;
			// Estado 0

			// Token '<':
			maquinaEstados[Estado.INICIAL][Input.MENOR] = new AristaEstado(Estado.DISTYMENOR);// para mi esta mal

			// Token '>' o '='
			maquinaEstados[Estado.INICIAL][Input.MAYOR] = new AristaEstado(Estado.COMPYMAYOR);
			maquinaEstados[Estado.INICIAL][Input.IGUAL] = new AristaEstado(Estado.COMPYMAYOR);


			// Estado 6
			GeneraTokenParticular generaTokenPR = new GeneraTokenParticular(this,(short) '>') ;
			inicTransiciones(Estado.COMPYMAYOR, Estado.FINAL,retrocedeFuente,generaTokenPR);

			maquinaEstados[Estado.COMPYMAYOR][Input.IGUAL] = new AristaEstado(Estado.FINAL,consumeChar);
			NotError errorEOF = new NotError("Simbolo no reconocido por el compilador", aLexico, cFuente, true);
			maquinaEstados[Estado.COMPYMAYOR][Input.EOF] = new AristaEstado(Estado.FINAL, errorEOF);


			//Estado 7
			GeneraTokenParticular token = new GeneraTokenParticular(this,(short)'<');
			inicTransiciones(Estado.DISTYMENOR, Estado.FINAL, retrocedeFuente, retrocedeFuente,token);

			maquinaEstados[Estado.DISTYMENOR][Input.IGUAL] = new AristaEstado(Estado.FINAL, consumeChar);
			maquinaEstados[Estado.DISTYMENOR][Input.MAYOR] = new AristaEstado(Estado.FINAL, consumeChar);

			maquinaEstados[Estado.DISTYMENOR][Input.EOF] = new AristaEstado(Estado.COMPYMAYOR, errorEOF);

		}


	    private void inicCaminoCtesNum(AccionSemantica inicStringVacio, AccionSemantica concatenaChar,
	                                   AccionSemantica retrocedeFuente, AccionSemantica generaTokenULONG,
	                                   AccionSemantica consumeChar, AccionSemantica generaTokenDouble) {



			// Inicializo los errores
	        ParseBaseDouble parseBaseDouble = new ParseBaseDouble();
	        NotWarning faltaSufijo = new NotWarning("Falto el sufijo 'ul' al final del numero, el numero fue tomado como ULONG ", aLexico);
	        NotWarning sufijoInvalido = new NotWarning("se encontro un sufijo que no es valido como ULONG, este se tomara como ULONG", aLexico);
	        NotWarning sinExponente = new NotWarning("no hay exponente, este sera tomado como 0 por defecto", aLexico);

	        //Estado 0
	        maquinaEstados[Estado.INICIAL][Input.DIGITO] = new AristaEstado(Estado.D_U_1, inicStringVacio,
	            concatenaChar);
	        maquinaEstados[Estado.INICIAL][Input.PUNTO] = new AristaEstado(Estado.D_U_4, inicStringVacio,
	            concatenaChar);

	        // Estado 12
	        //Con las salidas no reconocidas el compilador lo toma como ULONG y genera un warning
	        inicTransiciones(Estado.D_U_1, Estado.FINAL, generaTokenULONG, retrocedeFuente, faltaSufijo);
	        //si hay un salto de linea, genero Warning por falta
	        maquinaEstados[Estado.D_U_1][Input.SALTO_LINEA] = new AristaEstado(Estado.FINAL, generaTokenULONG,
	            cuentaSaltoLinea, faltaSufijo);
	        //Digitos.
	        maquinaEstados[Estado.D_U_1][Input.DIGITO] = new AristaEstado(Estado.D_U_1,
	            concatenaChar);

	        //Punto ('.'). Salto a deteccion de parte decimal de doubles.
	        maquinaEstados[Estado.D_U_1][Input.PUNTO] = new AristaEstado(Estado.D_U_3,
	            concatenaChar);

			// U : empieza camino correcto ULONG
			maquinaEstados[Estado.D_U_1][Input.U_MINUSC] = new AristaEstado(Estado.D_U_2);

			// OTRO
			maquinaEstados[Estado.D_U_1][Input.OTRO] = new AristaEstado(Estado.FINAL,
					concatenaChar);


	        //EOF. Crea un UINT pero genera un warning por falta de sufijo.
	        maquinaEstados[Estado.D_U_1][Input.EOF] = new AristaEstado(Estado.FINAL, generaTokenULONG,
	            faltaSufijo);

	        // Estado 13

	        // Crea un ULONG  por defecto pero genera un warning por falta de sufijo.
	        inicTransiciones(Estado.D_U_2, Estado.FINAL, retrocedeFuente, generaTokenULONG, sufijoInvalido);
	        //Salto de linea. Crea un UINT pero genera un warning por falta de sufijo.
	        maquinaEstados[Estado.D_U_2][Input.SALTO_LINEA] = new AristaEstado(Estado.FINAL, generaTokenULONG,
	            cuentaSaltoLinea, sufijoInvalido);
	        //Letra 'l' minuscula.
	        maquinaEstados[Estado.D_U_2][Input.L_MINUSC] = new AristaEstado(Estado.D_U_6); // no hace nada ya que la parte ul del sufijo solo me importa para clasificar

			//Cualquier otra cosa, retorno numero como ULONG y pongo aviso el warning
	        maquinaEstados[Estado.D_U_2][Input.OTRO] = new AristaEstado(Estado.FINAL,generaTokenULONG,sufijoInvalido);

	        //EOF. Crea un UINT pero genera un warning por falta de sufijo.
	        maquinaEstados[Estado.D_U_2][Input.EOF] = new AristaEstado(Estado.FINAL, generaTokenULONG, sufijoInvalido);

	        // Estado 17
			inicTransiciones(Estado.D_U_6, Estado.FINAL, retrocedeFuente, generaTokenULONG, sufijoInvalido);

			maquinaEstados[Estado.D_U_6][Input.OTRO] = new AristaEstado(Estado.FINAL, generaTokenULONG, sufijoInvalido);
			maquinaEstados[Estado.D_U_6][Input.EOF] = new AristaEstado(Estado.FINAL, generaTokenULONG, sufijoInvalido);


			// Estado 14
			inicTransiciones(Estado.D_U_6, Estado.FINAL, retrocedeFuente, generaTokenDouble,sufijoInvalido);
			maquinaEstados[Estado.D_U_3][Input.DIGITO] = new AristaEstado(Estado.D_U_3,concatenaChar);
			maquinaEstados[Estado.D_U_3][Input.E_EXP] = new AristaEstado(Estado.D_U_4);// no se si le interesa o no
			maquinaEstados[Estado.D_U_3][Input.DIGITO] = new AristaEstado(Estado.D_U_3,concatenaChar);
			maquinaEstados[Estado.D_U_3][Input.EOF] = new AristaEstado(Estado.FINAL, generaTokenDouble,sinExponente);
			maquinaEstados[Estado.D_U_3][Input.OTRO] = new AristaEstado(Estado.FINAL,retrocedeFuente,generaTokenDouble,sinExponente);



			// Estado 15
			inicTransiciones(Estado.D_U_4, Estado.FINAL, retrocedeFuente, generaTokenDouble, sufijoInvalido);

			maquinaEstados[Estado.D_U_4][Input.DIGITO] = new AristaEstado(Estado.D_U_4,concatenaChar);
			maquinaEstados[Estado.D_U_4][Input.SUMA] = new AristaEstado(Estado.D_U_5,concatenaChar);
			maquinaEstados[Estado.D_U_4][Input.GUION] = new AristaEstado(Estado.D_U_5,concatenaChar);
			maquinaEstados[Estado.D_U_4][Input.OTRO] = new AristaEstado(Estado.FINAL,retrocedeFuente,generaTokenDouble);
			maquinaEstados[Estado.D_U_4][Input.EOF] = new AristaEstado(Estado.FINAL, generaTokenDouble,sinExponente);

			// Estado 16
			inicTransiciones(Estado.D_U_5, Estado.FINAL,parseBaseDouble, retrocedeFuente, generaTokenDouble);
			maquinaEstados[Estado.D_U_5][Input.DIGITO] = new AristaEstado(Estado.D_U_5,concatenaChar);
			maquinaEstados[Estado.D_U_5][Input.OTRO] = new AristaEstado(Estado.FINAL, generaTokenDouble,retrocedeFuente, sinExponente);

	    }


	    private void inicCaminoCadenas(CodigoFuente cFuente, AccionSemantica inicStringVacio, AccionSemantica concatenaChar,
	                                   AccionSemantica generaTokenCadena) {
	        // Estado 0
	        maquinaEstados[Estado.INICIAL][Input.PORCENTAJE] = new AristaEstado(Estado.UNALINEA, inicStringVacio,
	            concatenaChar);

	        // Estado 11
			maquinaEstados[Estado.UNALINEA][Input.OTRO] = new AristaEstado(Estado.UNALINEA,
					concatenaChar);
			ChequeoSLinea salta = new ChequeoSLinea(this,cFuente);
			NotError saltoInvalido = new NotError("En esta seccion del codigo no se puede realizar un salto",aLexico,cFuente,false);
			maquinaEstados[Estado.UNALINEA][Input.PORCENTAJE] = new AristaEstado(Estado.FINAL,generaTokenCadena,saltoInvalido,cuentaSaltoLinea);

	        //finaliza la cadena con porcentaje
	        maquinaEstados[Estado.UNALINEA][Input.PORCENTAJE] = new AristaEstado(Estado.FINAL, concatenaChar,generaTokenCadena);
	        //Hay un salto de linea.
	        //EOF. Queda la cadena abierta, por lo que hay que notificar un error.
	        NotError errorCadenaAbierta = new NotError("Se llego al EOF y la cadena quedo abierta", aLexico,
	            cFuente, false);
	        maquinaEstados[Estado.UNALINEA][Input.EOF] = new AristaEstado(Estado.FINAL, errorCadenaAbierta);
	    }

		private void inicCaminoOr(CodigoFuente cfuente,AccionSemantica consumeChar,Retrocede_Fuente retrocedeFuente,GeneraTokenLiteral generatokenliteral){

			//Estado 0
			maquinaEstados[Estado.INICIAL][Input.OR] = new AristaEstado(Estado.OR);


			// Estado 9
			// si viene una sola aviso con un Warning pero lo tomo como un or
			NotError error = new NotError("error en la palabra Or, tiene un solo |",aLexico,cfuente,true);
			inicTransiciones(Estado.OR, Estado.FINAL,retrocedeFuente,error);

			maquinaEstados[Estado.OR][Input.OR] = new AristaEstado(Estado.FINAL,generatokenliteral,consumeChar);




		}
	private void inicCaminoAnd(CodigoFuente cfuente,AccionSemantica consumeChar,Retrocede_Fuente retrocedeFuente,GeneraTokenLiteral generatokenp){


		//Estado 0
		maquinaEstados[Estado.INICIAL][Input.AND] = new AristaEstado(Estado.AND);


		// Estado 8

		NotError error = new NotError("error en la palabra And, tiene un solo &",aLexico,cfuente,true);
		inicTransiciones(Estado.AND, Estado.FINAL,retrocedeFuente,error);

		maquinaEstados[Estado.OR][Input.OR] = new AristaEstado(Estado.FINAL,generatokenp,consumeChar);
	}

	private void inicCaminoop(CodigoFuente cfuente, AccionSemantica consumeChar,Retrocede_Fuente retrocedeFuente,GeneraTokenLiteral generatoken){

		//Estado 0
		   maquinaEstados[Estado.INICIAL][Input.DOSP] = new AristaEstado(Estado.ASIGNACION);


		   //Estado 10
			NotError error = new NotError("error en la asginacion, ",aLexico,cfuente,true);
			inicTransiciones(Estado.AND, Estado.FINAL,retrocedeFuente,error);
			maquinaEstados[Estado.ASIGNACION][Input.IGUAL] = new AristaEstado(Estado.FINAL,consumeChar,generatoken);


	}

}
