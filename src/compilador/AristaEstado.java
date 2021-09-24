package compilador;

import compilador.Asemanticas.AccionSemantica;

import java.util.ArrayList;
import java.util.List;

public class AristaEstado {// Esta clase modela las acciones semanticas que deberan realizarse cada vez que se realize un movimiento en el grafo
	
	private final int sigEstado;
	private final List<AccionSemantica> accciones= new ArrayList<>();


	public AristaEstado(int sigEstado) {
		this.sigEstado = sigEstado;
	}
}
