package compilador.asemanticas;

import compilador.util.CodigoFuente;

public class ConcChar extends AccionSemantica {
// Contatena el caracter al final del String
	
	private final CodigoFuente codigo;
	
	public ConcChar(CodigoFuente codigo)
	{
	this.codigo= codigo;
	}	
	@Override
	
	public void ejecutar() {
		System.out.println(ANSI_BLUE+"ejecuto cChar"+ANSI_RESET);
		concatenaChar(codigo.simActual());
	}

}
