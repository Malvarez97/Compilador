package compilador.asemanticas;

import compilador.maquina_estado.MaquinaEstados;
import compilador.simbolo.TablaSimbolos;

/**
 * Si la TS contiene una entrada con el lexema actual, incrementa su numero de referencias en uno. Si no la
 * contiene, la crea y agrega.
 */
public class GeneraTokenTSimbolos extends AccionSemantica{

    private MaquinaEstados maquina;
    private short token;
    private TablaSimbolos ts;


    public GeneraTokenTSimbolos(MaquinaEstados maquina, TablaSimbolos ts , short token){
        this.maquina= maquina;
        this.ts=ts;
        this.token=token;

    }

    @Override
    public void ejecutar() {
        System.out.println(ANSI_BLUE+"genero token simbolos"+ANSI_RESET);
        String lexema =getString();
        ts.agregarEntrada(token,lexema,"");
        ts.setUsoEntrada(lexema,"-");
        maquina.setVariablesSintactico(token,lexema);
    }
}
