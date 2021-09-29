package compilador.Asemanticas;

import compilador.AnalizadorLex;

public class NotWarning extends AccionSemantica{
    private  String mensaje;
    private AnalizadorLex aLexico;

    public NotWarning(String mensaje, AnalizadorLex aLexico) {
        this.mensaje = mensaje;
        this.aLexico = aLexico;
    }

    /**
     * Accion semantica auxiliar para notificar warnings.
     */
    @Override
    public void ejecutar() {
        AnalizadorLex.Notificacion.addWarnings(aLexico.getLineaActual(),mensaje);
    }
        }


