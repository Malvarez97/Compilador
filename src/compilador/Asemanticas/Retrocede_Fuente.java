package compilador.Asemanticas;

import compilador.CodigoFuente;

public class Retrocede_Fuente extends AccionSemantica{
    private CodigoFuente codigo;

    public Retrocede_Fuente(CodigoFuente codigo){this.codigo=codigo;}
    @Override
    public void ejecutar() {codigo.retrocede();}
}
