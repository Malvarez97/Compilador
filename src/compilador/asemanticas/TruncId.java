package compilador.asemanticas;

import compilador.AnalizadorLex;
import compilador.util.Notificacion;

public class TruncId  extends AccionSemantica {
	
	private final static int maxString = 20;
	private final AnalizadorLex lexico;
	
	public TruncId(AnalizadorLex lexico) {
		this.lexico=lexico;
	}
	
	public void ejecutar() {
		System.out.println(ANSI_BLUE+"trunco id"+ANSI_RESET);
		if(maxString < tamString()) {
			truncaString(maxString);
			Notificacion.addWarnings(lexico.getLineaActual(),null );
		}
	}
}
