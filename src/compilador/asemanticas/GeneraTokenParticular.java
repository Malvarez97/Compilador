package compilador.asemanticas;

import compilador.util.AlmacenToken;
import compilador.maquina_estado.MaquinaEstados;
// Genera token particular
public class GeneraTokenParticular extends AccionSemantica{
    private MaquinaEstados maquina;
    private short token ;

   public GeneraTokenParticular(MaquinaEstados maquina, short token){
       this.maquina=maquina;
       this.token=token;
   }

    @Override
    public void ejecutar() {
        System.out.println(ANSI_BLUE+"genero token Particular"+ANSI_RESET);
       maquina.setVariablesSintactico(token, AlmacenToken.getRepresentacion(token));

    }
}
