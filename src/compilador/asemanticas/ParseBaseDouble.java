package compilador.asemanticas;

public class ParseBaseDouble  extends AccionSemantica{


	public void ejecutar() {
        String doub = getString();
        if (doub.equals(".")) setBaseDouble(0);
        else setBaseDouble(Double.parseDouble(doub));
        inicString(); // luego de terminar la ejecucion reinicia el string temporal
    }
		
}
