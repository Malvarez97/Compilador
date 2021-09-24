package compilador.Asemanticas;

public class ParseBaseDouble  extends AccionSemantica{
	
	public void ejecutar() {
        String doubleString = getString();
        if (getString().equals(".")) setBaseNumDouble(0);
        else setBaseNumDouble(Double.parseDouble(doubleString));

        inicString(); //Reinicia el string temporal.
    }
		
}
