package compilador.util;

public class CodigoFuente {
	
	private String codFuente;
	private int posicion =0;
	
	
	public CodigoFuente(String fuente) {
		this.codFuente = fuente;
	}
	
	public void retrocede() {
		if ( posicion>0) {
			posicion--;
		}
	}
	public boolean eof() {
		return posicion==codFuente.length();
	}
	
	public void adelanta() {
		if(!eof()) {
			posicion++;
		}
	}
	
	public char simActual() {
		return codFuente.charAt(posicion);
	}
	
	public char simAnterior() {
		if (posicion ==0) throw new IllegalStateException ("no se puede leer el simbolo anterior por que te encontras en el inicio");
		return codFuente.charAt(posicion-1);
	}
	
	
}
