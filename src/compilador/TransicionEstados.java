package compilador;


import compilador.Asemanticas.AccionSemantica;

import java.util.Arrays;
import java.util.List;

public class TransicionEstados {
    private final int siguienteEstado;
    private final List<AccionSemantica> accionesSemanticas;

    public TransicionEstados(int siguienteEstado, AccionSemantica... accionesSemanticas) {
        this.siguienteEstado = siguienteEstado;
        this.accionesSemanticas = Arrays.asList(accionesSemanticas);
    }

    public int siguienteEstado() {
        return siguienteEstado;
    }

    public void ejecutarAccionSemantica() {
        for (AccionSemantica accionSemantica : accionesSemanticas)
            accionSemantica.ejecutar();
    }
}