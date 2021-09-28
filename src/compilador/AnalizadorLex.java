package compilador;


import compilador.MaquinaEstados.MaquinaEstados;

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

	    public int produceToken(){
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

	public static class Notificacion {

		private static final List<String> errores = new ArrayList<>();
		private static final List<String> warnings =new ArrayList<>();

		public Notificacion(String simbolo_no_reconocido, AnalizadorLex aLexico, CodigoFuente cFuente, boolean b) {
		}


		public static void addError(int linea, String error) {
			errores.add("linea "+linea+"->"+error);
		}


		public static String getErrores() { // retorno un String con los errores concatenados

			if(errores.isEmpty()) return "no hay Errores";
			StringBuilder contenedor = new StringBuilder();
			for (String error : errores) {
				contenedor.append(error).append("\n");
			}
			return contenedor.toString();
		}
		public static String getWarnings() { // retorno un String con los errores concatenados

			if(warnings.isEmpty()) return "no hay Warnings";
			StringBuilder contenedor = new StringBuilder();
			for (String war : warnings) {
				contenedor.append(war).append("\n");
			}
			return contenedor.toString();
		}


		public static String getResultado() {
			return (String)"Errores"+'\n'+ getErrores() + '\n'+"Warnings"+'\n'+getWarnings();
		}


		public static boolean hayErrores() {
			return errores.size() > 0;
		}

		public static void addWarnings(int linea, String war) {
			warnings.add("linea"+linea+"->"+"war");
		}


	}
}
