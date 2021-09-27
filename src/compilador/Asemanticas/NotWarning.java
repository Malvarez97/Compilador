package compilador.Asemanticas;

import compilador.AnalizadorLex;
import compilador.CodigoFuente;

public class NotWarning extends AccionSemantica{
    private String mensaje;
    private CodigoFuente codigoFuente;
    private boolean irreconocible;
    private AnalizadorLex lexico;
    // esta clase sirve para notificar los warnings

    public NotWarning(String mensaje, CodigoFuente codigoFuente, boolean irreconocible, AnalizadorLex lexico) {
        this.mensaje = mensaje;
        this.codigoFuente = codigoFuente;
        irreconocible = irreconocible;
        this.lexico = lexico;
    }

    @Override
    public void ejecutar() {
        String warning ="";
        if (irreconocible){
            warning += " El Simbolo  : "+codigoFuente.simActual()+ "no fue reconocido";
        }
        else{
            warning += mensaje;
            Notificacion.addError(lexico.getLineaActual(),warning );
        }


    }
}
