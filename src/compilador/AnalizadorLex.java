package compilador;


import compilador.MaquinaEstados.MaquinaEstados;

public class AnalizadorLex {
	private final CodigoFuente cod_Fuente;
	private final MaquinaEstados maquinaEstados;
	public static final short T_EOF = 0;
	public short ultimoTokenGenerado = -1;
	public String ultimoLexemaGenerado;
	
	
	 public AnalizadorLex(CodigoFuente cFuente, TablaSimbolos tablaS){
	        this.cod_Fuente = cFuente;
	        this.maquinaEstados = new MaquinaEstados(this, cFuente,tablaS, inicTPR());
	    }

	    public void setVariablesSintactico(short token, String lexema){
	        this.ultimoTokenGenerado = token;
	        this.ultimoLexemaGenerado = lexema;
	    }

	    public int getLineaActual(){
	        return maquinaEstados.getLineaActual();
	    }

	    private TablaPalabrasReserv inicTPR(){

	        return null;
	    }

	    public int produceToken(){
	        while (!maquinaEstados.estadoFinalAlcanzado()){
	            if (cod_Fuente.eof()) maquinaEstados.cambiarEOF();
	            else {
	                maquinaEstados.cambiar(cod_Fuente.simActual());
	                cod_Fuente.adelanta();
	            }
	        }
	        maquinaEstados.reiniciar();
	        return ultimoTokenGenerado;
	    }
}
