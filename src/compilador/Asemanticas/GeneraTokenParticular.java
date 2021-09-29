package compilador.Asemanticas;

import compilador.util.AlmacenToken;
import compilador.MaquinaEstados.MaquinaEstados;
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
       maquina.setVariablesSintactico(token, AlmacenToken.getRepresentacion(token));

    }
}
