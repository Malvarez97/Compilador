package compilador.asemanticas;

import compilador.AnalizadorLex;
import compilador.util.Notificacion;

public class NotWarning extends AccionSemantica{
    private  String mensaje;
    private AnalizadorLex aLexico;
    // Accion semantica que genera los warnings
    public NotWarning(String mensaje, AnalizadorLex aLexico) {
        this.mensaje = mensaje;
        this.aLexico = aLexico;
    }

    @Override
    public void ejecutar() {
        Notificacion.addWarnings(aLexico.getLineaActual(),mensaje);
    }
        }


