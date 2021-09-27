package compilador.MaquinaEstados;

import compilador.Asemanticas.AccionSemantica;

import java.util.ArrayList;
import java.util.List;

public class AristaEstado {// Esta clase modela las acciones semanticas que deberan realizarse cada vez que se realize un movimiento en el grafo
	
	private final int sigEstado;
	private final List<AccionSemantica> acciones= new ArrayList<>();


	public AristaEstado(int sigEstado) {
		this.sigEstado = sigEstado;
	}
	public int getSigEstado(){ return this.sigEstado ;}
	public void ejecutar(){ // ejecuta todas las acciones semanticas del estado
		for (AccionSemantica accion : acciones)
			accion.ejecutar();
	}
}
