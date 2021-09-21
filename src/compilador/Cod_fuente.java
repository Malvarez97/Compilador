package compilador;

public class Cod_fuente {
	private final String codFuente;
	
	private int posActual = 0 ;
	
	public Cod_fuente(String fuente) {
		this.codFuente=fuente ;
	}
	
	public void adelantar() {// Avanza en el String 
		if (eof()) return ;
		posActual ++ ;
	}
	
	public void volver() { // retrocede en uno en el archivo 
		if(posActual>=1) {
			posActual--;
		}
	}
	
	public boolean eof() { // retorna si estamos o no en el final del archivo 
		return (posActual==codFuente.length());
	}
	
	public char simbActual(){
		return codFuente.charAt(posActual);
	}
	
	public char simbAnt() {
		if (posActual==0 ) {
			throw new IllegalStateException("no es posible leer el simbolo anterior.");
		}
		return codFuente.charAt(posActual-1);
	}

}
