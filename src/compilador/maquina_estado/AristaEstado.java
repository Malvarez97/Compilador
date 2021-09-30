package compilador.maquina_estado;

import compilador.asemanticas.AccionSemantica;

import java.util.Arrays;
import java.util.List;

public class AristaEstado {// Esta clase modela las acciones semanticas que deberan realizarse cada vez que se realize un movimiento en el grafo
	
	private final int sigEstado;
	private List<AccionSemantica> acciones;


	public AristaEstado(int sigEstado ,AccionSemantica... acciones)
	{
		this.sigEstado = sigEstado;
		this.acciones= Arrays.asList(acciones);
	}
	public int getSigEstado(){ return this.sigEstado ;}
	public void ejecutar(){ // ejecuta todas las acciones semanticas del estado
		for (AccionSemantica accion : acciones)
			accion.ejecutar();
	}
}
