package compilador.Asemanticas;

import compilador.CodigoFuente;
import compilador.MaquinaEstados.MaquinaEstados;

public class GeneraTL extends AccionSemantica{
    private MaquinaEstados maquina;
    private CodigoFuente cFuente;

    public GeneraTL(MaquinaEstados maquinaEstados, CodigoFuente cFuente) {
        this.maquina = maquinaEstados;
        this.cFuente = cFuente;
    }

    @Override
    public void ejecutar() {
        int token = cFuente.simActual(); //Conversion implicita de char a ASCII.
        maquina.setVariablesSintactico((short) token, ""); //No tiene lexema.
    }

    }
