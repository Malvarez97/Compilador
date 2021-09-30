package compilador;


import compilador.MaquinaEstados.MaquinaEstados;
import compilador.TablaSimbolos.TablaSimbolos;
import compilador.util.CodigoFuente;
import compilador.util.TablaPalabrasReserv;

import java.util.ArrayList;
import java.util.List;

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

	    public int tokengenerado(){
	        while (!maquinaEstados.esEstadoFinal()){
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
