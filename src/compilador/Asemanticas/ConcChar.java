package compilador.Asemanticas;

import compilador.Asemanticas.AccionSemantica;
import compilador.CodigoFuente;

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
