package compilador;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import compilador.Casilla;

public class TablaSimbolos {
	private final Map<String, Casilla> tablaSimb;
	
	public TablaSimbolos() {
    	tablaSimb = new Hashtable<>();
    	}
	
	public String toString() {
		if (tablaSimb.isEmpty()) return "Tabla de s�mbolos vac�a.";
		StringBuilder builder = new StringBuilder();
		for (Casilla c : tablaSimb.values())
			builder.append(c.toString()).append('\n');
		return builder.toString();
	}
	
	public String toAsm(){
        StringBuilder asmBuilder = new StringBuilder();

        for (String lexema : tablaSimb.keySet()) {
            Casilla celda = tablaSimb.get(lexema);

            if (celda.isProc()){
                asmBuilder.append("_NI_MAX_").append(lexema).append(" DW ").append(getMaxInvoc(lexema)).append('\n');
                asmBuilder.append("_NI_ACT_").append(lexema).append(" DW ").append(0).append('\n');
                asmBuilder.append("_INVOCADO_").append(lexema).append(" DW ").append(0).append('\n');
            }

            //DOUBLE
            if (celda.getTipo().equals(Casilla.TIPO_DOUBLE)){
                if (celda.esCte())
                    asmBuilder.append('_').append(formatDouble(lexema)).append(" DQ ").append(lexema).append('\n'); //Cte.
                else{ //Variable
                    if (lexema.startsWith("PROGRAM")) asmBuilder.append('_'); //Variable no auxiliar.
                    asmBuilder.append(lexema).append(" DQ ").append(0).append('\n');
                }
            }

            //UINT
            if (celda.getTipo().equals(Casilla.TIPO_UINT)
                    && (celda.getUso().equals(Casilla.USO_VAR) || celda.isParamCVR() || celda.isParamCV())){
                if (lexema.startsWith("PROGRAM")) asmBuilder.append('_'); //Variable no auxiliar.
                asmBuilder.append(lexema).append(" DW ").append(0).append('\n');
            }

            //Cadena caracteres
            if (lexema.startsWith("\"") && lexema.endsWith("\"")){
                asmBuilder.append("_CAD_")
                        .append(lexema.replace(' ', '_'), 1, lexema.length() - 1)
                        .append(" DB ")
                        .append("'").append(lexema, 1, lexema.length() - 1).append("'")
                        .append(", 0").append('\n');
            }
        }

        return asmBuilder.toString();
    }

    public static String formatDouble(String doubleS){
        String nDouble = doubleS.replace('-','n');
        return nDouble.replace('.','p');
    }

    public void clear() {
        tablaSimb.clear();
    }

    //---INSERCION, ELIMINACION Y OBTENCION DE ENTRADAS---

    public boolean contieneLexema(String lexema) {
        return tablaSimb.containsKey(lexema);
    }

    /**
     * Agrega una nueva entrada solo si no existe en la tabla.
     */
    public void agregarEntrada(Casilla entrada){
        if (tablaSimb.containsKey(entrada.getLexema()))
            throw new IllegalStateException("Ya hay una entrada con el lexema '"+entrada.getLexema()+"'.");
        tablaSimb.put(entrada.getLexema(), entrada);
        entrada.actualizarReferencias(1);
    }

    /**
     * Agrega una celda (token,lexema,tipo). En caso de existir previamente, incrementa en uno las referencias a la
     * celda.
     */
    public void agregarEntrada(int token, String lexema, String tipo) {
    	Casilla celda;

        if (tablaSimb.containsKey(lexema))  //Si el lexema existe extraigo la celda para actualizar las referencias
            celda = getEntrada(lexema);
        else {//Si no existe creo una nueva y la inserto.
            celda = new Casilla(token, lexema, tipo);
            tablaSimb.put(lexema, celda);
        }
        celda.actualizarReferencias(1);
    }

    public Casilla getEntrada(String lexema) {
    	Casilla celda = tablaSimb.get(lexema);

        if (celda == null) //Agrege la excepcion por si llega a fallar el get, que no ande el null dando vueltas.
            throw new IllegalStateException("El lexema '" + lexema + "' no se encontro en la tabla de simbolos.");
        return celda;
    }

    //---CONSULTAS DATOS ENTRADAS---

    public void agregarReferencia(String lexema){
    	Casilla celda = tablaSimb.get(lexema);
        celda.actualizarReferencias(1); //refs++
    }

    public void quitarReferencia(String lexema) {
    	Casilla celda = tablaSimb.get(lexema);
        celda.actualizarReferencias(-1); //refs--
        if (celda.sinReferencias()) tablaSimb.remove(celda.getLexema());
    }

    public boolean esEntradaCte(String lexema) {
    	Casilla entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema '"+lexema+"' no encontrado en la TS");
        return entrada.esCte();
    }

    public String getTipoEntrada(String lexema){
    	Casilla entrada = tablaSimb.get(lexema);
        return entrada.getTipo();
    }

    public void setUsoEntrada(String lexema, String uso){
    	Casilla entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema '" + lexema + "' no encontrado en la TS");
        entrada.setUso(uso);
    }

    public boolean isEntradaDeclarada(String lexema){
        return tablaSimb.get(lexema) != null
                && tablaSimb.get(lexema).isDeclarada();
    }

    public void setDeclaracionEntrada(String lexema, boolean declarada){
    	Casilla entrada = tablaSimb.get(lexema);
        entrada.setDeclarada(declarada);
    }

    //---METODOS PROCEDIMIENTOS---

    public boolean isEntradaProc(String lexema){
    	Casilla entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema '" + lexema + "' no encontrado en la TS");
        return entrada.isProc();
    }

    public boolean isEntradaParamCVR(String lexema){
    	Casilla entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema '" + lexema + "' no encontrado en la TS");
        return entrada.isParamCVR();
    }

    public void setMaxInvoc(String lexema, int nMax){
    	Casilla entrada = tablaSimb.get(lexema);

        if (entrada == null) throw new IllegalStateException("Lexema '"+lexema+"' no encontrado en la TS");
        entrada.setMaxInvoc(nMax);
    }

    public int getMaxInvoc(String lexema){
    	Casilla entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema '"+lexema+"' no encontrado en la TS");
        return entrada.getMaxInvoc();
    }

    public boolean maxInvocAlcanzadas(String lexema){
    	Casilla entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema '" + lexema + "' no encontrado en la TS");
        return entrada.maxInvocAlcanzadas();
    }

    public void addParamProc(String lexema, String param){
    	Casilla entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema '" + lexema + "' no encontrado en la TS");
        entrada.addParamDecl(param);
    }

    public void addTipoParamProc(String lexema, String tipoParam){
    	Casilla entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema '" + lexema + "' no encontrado en la TS");
        entrada.addTipoParamDecl(tipoParam);
    }

    public String getTipoParamProc(String lexema, int nParam){
    	Casilla entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema '" + lexema + "' no encontrado en la TS");
        return entrada.getTipoParam(nParam);
    }

    public void setParamsReales(String lexema, List<String> paramsReales){
    	Casilla entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema '" + lexema + "' no encontrado en la TS");
        entrada.setParamsReales(paramsReales);
    }

    public int getNParams(String lexema){
    	Casilla entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema '" + lexema + "' no encontrado en la TS");
        return entrada.getNParams();
    }

    public String getParam(String lexema, int i){
    	Casilla entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema '" + lexema + "' no encontrado en la TS");

        Casilla entradaParam = tablaSimb.get(entrada.getParam(i));
        if (entradaParam == null)
            throw new IllegalStateException("Lexema del parametro '"+entrada.getParam(i)+"'no encontrado en la TS");

        return entradaParam.getLexema();
    }

    public List<String> getParamReales(String lexema){
    	Casilla entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema '" + lexema + "' no encontrado en la TS");

        return entrada.getParamsReales(); //Params reales de la primera invocacion.
    }

    public boolean containsParamFormal(String proc, String paramFormal){
    	Casilla entrada = tablaSimb.get(proc);
        if (entrada == null) throw new IllegalStateException("Lexema '" + proc + "' no encontrado en la TS");

        return entrada.containsParamFormal(paramFormal);
    }
}

