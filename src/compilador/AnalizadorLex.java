package compilador;

public class AnalizadorLex {
	private final CodigoFuente Cod_Fuente;
	private final MaquinaEstados maquinaEstados;
	public static final short T_EOF = 0;
	public short ultimoTokenGenerado = -1;
	public String ultimoLexemaGenerado;
	
	
	public AnalizadorLex(CodigoFuente cod_fuente, TablaSimbolos tablaSimb) {
		
	}
}
