package compilador.asemanticas;

public class CuentaSaltoLinea extends AccionSemantica {

    private int cantLineas = 1;

    public CuentaSaltoLinea(){};

    @Override
    public void ejecutar() {
        cantLineas++;
        System.out.println(ANSI_BLUE+"ejecuto cuenta linea"+ANSI_RESET);
    }

    public int getCantLineas() {
        return cantLineas;
    }   
}