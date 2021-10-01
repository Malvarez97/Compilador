package compilador.asemanticas;

public class CuentaSaltoLinea extends AccionSemantica {

    private int cantLineas = 1;

    public CuentaSaltoLinea(){};

    @Override
    public void ejecutar() {
        cantLineas++;
        System.out.println("ejecuto cuenta linea");
    }

    public int getCantLineas() {
        return cantLineas;
    }   
}