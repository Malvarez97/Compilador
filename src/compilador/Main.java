package compilador;

import java.io.File;

public class Main {

	public static void main(String[] args) {
			Compilador compilador=new Compilador();
			File file= new File("C:\\Users\\mauri\\eclipse-workspace\\Compilador\\src\\compilador\\ejemplo1.txt");
			file.getPath();
			String ubicacion =file.getPath();
			compilador.ejecutar(ubicacion);

	}

}
