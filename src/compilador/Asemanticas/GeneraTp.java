package compilador.Asemanticas;

import compilador.AlmacenToken;
import compilador.MaquinaEstados;

public class GeneraTp extends AccionSemantica{
    private MaquinaEstados maquina;
    private short token ;

   public GeneraTp(MaquinaEstados maquina,short token){
       this.maquina=maquina;
       this.token=token;
   }

    @Override
    public void ejecutar() {
       maquina.setVariablesSintactico(token, AlmacenToken.getRepresentacion(token));

    }
}
