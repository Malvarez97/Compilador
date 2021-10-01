package compilador.asemanticas;

import compilador.maquina_estado.MaquinaEstados;
import compilador.util.Notificacion;
import compilador.util.TablaPalabrasReserv;

// corrobora si la palabra reservada es valida y agrega el token a la lista de tokens

public class GeneraTPr extends AccionSemantica{
    private MaquinaEstados maquina ;

    public GeneraTPr(MaquinaEstados maquina){
        this.maquina=maquina;
    }

    @Override
    public void ejecutar() {
        System.out.println(ANSI_BLUE+"genero Token palabra reservada"+ANSI_RESET);
        String palabra =getString();
        if(TablaPalabrasReserv.esReservada(palabra)){
            maquina.setVariablesSintactico(TablaPalabrasReserv.getToken(palabra),""); //
        }else
        {
            Notificacion.addError(maquina.getLineaActual(),"la palabra resevada siguiente no fue encontrada como palabra reservada"+palabra);
            maquina.reiniciar();
        }

    }
}
