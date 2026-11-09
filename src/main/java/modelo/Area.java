package modelo;

public class Area {
    private int idArea;
    private String nombreArea;
    private int nivelRestriccion;
    
    public Area(){}
    
    public Area(int idArea, String nombreArea, int nivelRestriccion){
        this.idArea = idArea;
        this.nombreArea = nombreArea;
        this.nivelRestriccion = nivelRestriccion;
    }
    
    public int getIdArea(){return idArea;}
    public void setIdArea(int idArea){this.idArea = idArea;}
    public String getNombreArea(){return nombreArea;}
    public void setNombreArea (String nombreArea){this.nombreArea = nombreArea;}
    public int getNivelRestriccion(){return nivelRestriccion;}
    public void setNivelRestriccion(int nivelRestriccion){this.nivelRestriccion = nivelRestriccion;}
    
    @Override
    public String toString(){
        return nombreArea;
    }
}
