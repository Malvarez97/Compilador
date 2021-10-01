package compilador.asemanticas;

public class CuentaSaltoLinea extends AccionSemantica {

    private int cantLineas = 1;

    public CuentaSaltoLinea(){};

    @Override
    public void ejecutar() {
        cantLineas++;
    }

    public int getCantLineas() {
        return cantLineas;
    }   
}