package compilador.Asemanticas;

import compilador.MaquinaEstados.MaquinaEstados;
import compilador.TablaPalabrasReserv;

public class GeneraPr extends AccionSemantica{
    private MaquinaEstados maquina ;

    public GeneraPr(MaquinaEstados maquina){
        this.maquina=maquina;
    }

    @Override
    public void ejecutar() {
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
