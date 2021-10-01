package compilador;

import compilador.maquina_estado.MaquinaEstados;
import compilador.simbolo.TablaSimbolos;
import compilador.util.CodigoFuente;
import compilador.util.TablaPalabrasReserv;

public class AnalizadorLex {
	private CodigoFuente cod_Fuente;
	private final MaquinaEstados maquinaEstados;
	public static final short T_EOF = 0;
	public short ultimoTokenGenerado = -1;
	public String ultimoLexemaGenerado;
	
	
	 public AnalizadorLex(CodigoFuente fuente, TablaSimbolos tablaS){
	        this.cod_Fuente = fuente;
	        this.maquinaEstados = new MaquinaEstados(this, fuente,tablaS, iniciarTpalabrasR());
	    }

	    public void setVariablesSintactico(short token, String lexema){
	        this.ultimoTokenGenerado = token;
	        this.ultimoLexemaGenerado = lexema;
	    }

	    public int getLineaActual(){
	        return maquinaEstados.getLineaActual();
	    }

	    private TablaPalabrasReserv iniciarTpalabrasR(){

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
			System.out.println(ultimoTokenGenerado);
	        maquinaEstados.reiniciar();
	        return ultimoTokenGenerado;
	    }


}
