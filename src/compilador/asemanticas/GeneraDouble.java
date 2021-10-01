package compilador.asemanticas;

import compilador.maquina_estado.MaquinaEstados;
import compilador.simbolo.TablaSimbolos;
import compilador.util.Notificacion;
// esta clase verifica los exponenetes y ademas si se encuentra en el rango correcto

public class GeneraDouble extends AccionSemantica{

    private final  double INFERIOR_DOUBLE = 2.2250738585072014;
    private final  int MAX_EXP = 308;
    private final  double SUPERIOR_DOUBLE = 1.7976931348623157;


    private MaquinaEstados maquina;
    private TablaSimbolos ts;
    private short token ;

    public GeneraDouble(MaquinaEstados maquina,TablaSimbolos ts, short token){
        this.maquina=maquina;
        this.ts=ts;
        this.token=token;
    }
    @Override
    public void ejecutar() {
        System.out.println(ANSI_BLUE+"genero double"+ANSI_RESET);
        boolean valido = true;
        int exponente = 0;
        if (baseInicializada()) {// si hay una base inicializada

            if (getString().isEmpty() || getString().equals("-") || getString().equals("+")) {
                Notificacion.addWarnings(maquina.getLineaActual(), "no hay exponente,fue tomado el exponente 0 por defecto");
            } else try {
                exponente = Integer.parseInt(getString());
            } catch (NumberFormatException numerFormatEx) {
                valido = false;
                maquina.reiniciar();// reinicio el estado de la maquina de estados
                Notificacion.addError(maquina.getLineaActual(), "el exponente  " + getString() + "del numero double se fue de Rango ");
            }
            if (valido && doubleValido(getBaseDouble(),exponente)){ // refactorizo el numero e inserto el numero final
                double numfinal= getBaseDouble()*Math.pow(10,exponente);
                maquina.setVariablesSintactico(token,String.valueOf(numfinal));
                ts.agregarEntrada(token,String.valueOf(numfinal),"DOUBLE");
                ts.setUsoEntrada(String.valueOf(numfinal),"CTE");
                ts.setDeclaracionEntrada(String.valueOf(numfinal),true);
            }
        }
    }
    private boolean doubleValido(double baseNumDouble, double expNumDouble) {
        boolean valido = true;
                if (expFueraRango(expNumDouble)) {
                     maquina.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token.
                     valido = false;
                     Notificacion.addError(maquina.getLineaActual(),"El exponente '" + expNumDouble + "' esta fuera de rango.");
                 }
                  else if (doubleFueraRango(baseNumDouble, expNumDouble)) {
                     maquina.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token.
                     valido = false;
                     Notificacion.addError(maquina.getLineaActual(),"El numero DOUBLE '" + baseNumDouble * Math.pow(10, expNumDouble) + "' esta fuera de rango.");
                 }

                 return valido;
             }

   private boolean expFueraRango(double numero) { return numero < -MAX_EXP || numero > MAX_EXP;}

   private boolean doubleFueraRango(double baseNumDouble, double expNumDouble) {
                 double min = INFERIOR_DOUBLE * Math.pow(10, -MAX_EXP);
                 double max = SUPERIOR_DOUBLE * Math.pow(10, MAX_EXP);
                 double doubleNormalizado = baseNumDouble * Math.pow(10, expNumDouble);
                 return (doubleNormalizado < min || doubleNormalizado > max)&& (doubleNormalizado != 0.0 );
             }

    }


