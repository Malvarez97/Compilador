package compilador.asemanticas;

public class IgnoraC extends AccionSemantica{


    public IgnoraC() {
    }

    @Override
    public void ejecutar() {
        System.out.println("ignoroChar");
        return ;
    }
}

