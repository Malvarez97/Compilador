package compilador.asemanticas;

public class InicStringVacio extends AccionSemantica {

	@Override
	public void ejecutar() {
		System.out.println("inicializo Stirng vacio");
		this.inicString();
		
	}

}
