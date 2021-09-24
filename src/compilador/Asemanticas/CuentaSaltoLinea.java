package compilador.Asemanticas;


import compilador.Asemanticas.AccionSemantica;

public class CuentaSaltoLinea extends AccionSemantica {
    private int cantLineas = 1;

    @Override
    public void ejecutar() {
        cantLineas++;
    }

    public int getCantLineas() {
        return cantLineas;
    }   
}