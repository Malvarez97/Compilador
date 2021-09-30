package compilador.asemanticas;

import compilador.util.CodigoFuente;

public class Retrocede_Fuente extends AccionSemantica{
    private CodigoFuente codigo;

    public Retrocede_Fuente(CodigoFuente codigo){this.codigo=codigo;}
    @Override
    public void ejecutar() {codigo.retrocede();}
}
