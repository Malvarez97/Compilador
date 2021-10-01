package compilador.asemanticas;

public class InicStringVacio extends AccionSemantica {

	@Override
	public void ejecutar() {
		System.out.println(ANSI_BLUE+"inicializo Stirng vacio"+ANSI_RESET);
		this.inicString();
		
	}

}
