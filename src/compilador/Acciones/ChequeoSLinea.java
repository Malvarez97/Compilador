package compilador.Acciones;

import compilador.asemanticas.AccionSemantica;
import compilador.util.CodigoFuente;
import compilador.maquina_estado.MaquinaEstados;

public class ChequeoSLinea extends AccionSemantica {
    private MaquinaEstados maquina ;
    private CodigoFuente codigo;

    public ChequeoSLinea(MaquinaEstados maquina, CodigoFuente codigo) {
        this.maquina = maquina;
        this.codigo = codigo;
    }

    @Override
    public void ejecutar() {

    }
}
