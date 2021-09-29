package compilador.Asemanticas;

import compilador.util.CodigoFuente;
import compilador.MaquinaEstados.MaquinaEstados;

public class GeneraTokenLiteral extends AccionSemantica{
    private MaquinaEstados maquina;
    private CodigoFuente cFuente;

    public GeneraTokenLiteral(MaquinaEstados maquinaEstados, CodigoFuente cFuente) {
        this.maquina = maquinaEstados;
        this.cFuente = cFuente;
    }

    @Override
    public void ejecutar() {
        int token = cFuente.simActual(); //Conversion implicita de char a ASCII.
        maquina.setVariablesSintactico((short) token, ""); //No tiene lexema.
    }

    }
