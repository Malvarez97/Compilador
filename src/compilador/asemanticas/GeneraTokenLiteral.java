package compilador.asemanticas;

import compilador.util.CodigoFuente;
import compilador.maquina_estado.MaquinaEstados;

public class GeneraTokenLiteral extends AccionSemantica{

    private MaquinaEstados maquina;
    private CodigoFuente cFuente;

    public GeneraTokenLiteral(MaquinaEstados maquinaEstados, CodigoFuente cFuente) {
        this.maquina = maquinaEstados;
        this.cFuente = cFuente;
    }

    @Override
    public void ejecutar() {
        System.out.println(ANSI_BLUE+"genero Tokenl lit"+ANSI_RESET);
        int token = cFuente.simActual();
        maquina.setVariablesSintactico((short) token, ""); //No tiene lexema.
    }

    }
