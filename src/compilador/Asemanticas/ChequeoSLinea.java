package compilador.Asemanticas;

import compilador.util.CodigoFuente;
import compilador.MaquinaEstados.MaquinaEstados;

public class ChequeoSLinea extends AccionSemantica{
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
