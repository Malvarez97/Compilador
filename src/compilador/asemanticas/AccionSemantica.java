package compilador.asemanticas;

public abstract class AccionSemantica {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	private static StringBuilder sTemporal= new StringBuilder("");
	private static double base = Double.NEGATIVE_INFINITY; // mirar por que ...
	
	
	public void inicString() { // vacio el string en caso de que tenga basura, dejandolo vacio 
		sTemporal.delete(0,sTemporal.length());	
	}
	
	public  void extraerChar() {
		if(sTemporal.length()==0) throw new IllegalStateException("String temporal vacio");
		sTemporal.delete(sTemporal.length()-1, sTemporal.length());
	}
	
	public void  concatenaChar (char nuevo) {
		sTemporal.append(nuevo);
	}
	
	public int tamString() {
		return sTemporal.length();
	}
	
	public void truncaString(int limite) {
		sTemporal.delete(limite, sTemporal.length()-1); // elimina los char en las ubicaciones que ya fueron almacenadas
	}
	
	public String getString() {
		return sTemporal.toString();
	}
	
	public boolean doubInicializazdo() {
		return (base != Double.NEGATIVE_INFINITY);
	}
	
	public double getBaseDouble() {
		return base;
	}
	public void setBaseDouble(double valor) {
		base=valor;
	}
	public boolean baseInicializada(){return base!= Double.NEGATIVE_INFINITY;}// revisar
	
	public abstract void ejecutar();
}
