package compilador.simbolo;

import java.util.ArrayList;
import java.util.List;

public class Casilla {
    public static final String USO_PROC = "Proc";
    public static final String USO_VAR = "Var";
    public static final String USO_PARAM_CVR = "ParamCVR";
    public static final String USO_PARAM_CV = "ParamCV";
    public static final String TIPO_UINT = "UINT";
    public static final String TIPO_DOUBLE = "DOUBLE";
    public static final String USO_CTE = "CTE";

    /**
     * Atributos comunes.
     */
    private final int token;
    private final String lexema;
    private String tipo;
    private String uso = "-";
    private boolean declarada;
    private int referencias;

    /**
     * Atributos de procedimientos.
     */
    private int maxInvoc;
    private List<String> paramsDecl = new ArrayList<>();
    private List<String> tipoParamsDecl = new ArrayList<>();
    private final List<List<String>> paramsReales = new ArrayList<>();

    public Casilla(int token, String lexema, String tipo) {
        this.token = token;
        this.lexema = lexema;
        this.tipo = tipo;
        this.declarada = true;
        this.referencias = 0;
    }


    @Override
    public String toString() {
        String baseCelda =
                "{" +
                        "lex='" + lexema + '\'' +
                        ", tipo='" + tipo + '\'' +
                        ", uso='" + uso + '\'' +
                        ", decl='" + declarada + '\'' +
                        ", nRefs=" + referencias;
        if (uso.equals(USO_PROC) && paramsDecl != null && paramsReales != null)
            return baseCelda + ", maxNI=" + maxInvoc + "," +
                    "\n paramsFormales=" + paramsDecl.toString() + "\n paramsReales=" + paramsReales.toString() + '}';
        return baseCelda + '}';
    }

    public int getToken() {
        return token;
    }


    public void setUso(String uso) {
        this.uso = uso;
        if (uso.equals(Casilla.USO_PROC)) this.paramsDecl = new ArrayList<>();
    }


    public void setDeclarada(boolean declarada) {
        this.declarada = declarada;
    }

    public void actualizarReferencias(int i) {
        referencias += i;
    }
}




