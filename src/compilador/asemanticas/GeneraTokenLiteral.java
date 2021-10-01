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
        System.out.println("genero Tokenl lit");
        int token = cFuente.simActual(); //Conversion implicita de char a ASCII.
        maquina.setVariablesSintactico((short) token, ""); //No tiene lexema.
    }

    }
