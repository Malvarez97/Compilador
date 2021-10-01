package compilador.asemanticas;

import compilador.maquina_estado.MaquinaEstados;
import compilador.simbolo.TablaSimbolos;
import compilador.util.Notificacion;

public class GeneraULONG extends AccionSemantica{
    private MaquinaEstados maquina;
    private TablaSimbolos ts ;
    private short token;

    public GeneraULONG(MaquinaEstados maquina, TablaSimbolos ts, short token) {
        this.maquina = maquina;
        this.ts = ts;
        this.token = token;
    }

    @Override
    public void ejecutar() {
        System.out.println(ANSI_BLUE+"genero ulong "+ANSI_RESET);
        int numero = Integer.parseInt(this.getString());
        if ((numero >= 0) && (numero <= (int) (Math.pow(2, 32) - 1))) { // Rango Ulong
            ts.agregarEntrada(token, getString(), "ULONG");
            ts.setDeclaracionEntrada(getString(), true);// getString es el numero en string
            ts.setUsoEntrada(getString(), "CTE");
            maquina.setVariablesSintactico(token, getString());
        } else {
            Notificacion.addError(maquina.getLineaActual(), "Numero " + numero + " esta fuera del rango permitdo ");
            maquina.reiniciar();
        }
    }// puede que se rompa por algo, chusemar si pasa ..
}
