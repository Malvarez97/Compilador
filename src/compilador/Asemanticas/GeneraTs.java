package compilador.Asemanticas;

import compilador.MaquinaEstados.MaquinaEstados;
import compilador.TablaSimbolos;

/**
 * Si la TS contiene una entrada con el lexema actual, incrementa su numero de referencias en uno. Si no la
 * contiene, la crea y agrega.
 */
public class GeneraTs extends AccionSemantica{

    private MaquinaEstados maquina;
    private short token;
    private TablaSimbolos ts;


    public GeneraTs(MaquinaEstados maquina, TablaSimbolos ts , short token){
        this.maquina= maquina;
        this.ts=ts;
        this.token=token;

    }

    @Override
    public void ejecutar() {
        String lexema =getString();
        ts.agregarEntrada(token,lexema,"");
        ts.setUsoEntrada(lexema,"-");
        maquina.setVariablesSintactico(token,lexema);
    }
}
