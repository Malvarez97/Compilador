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
		concatenaChar(codigo.simActual());
	}

}
