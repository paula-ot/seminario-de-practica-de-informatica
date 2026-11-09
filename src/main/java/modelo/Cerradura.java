package modelo;

public class Cerradura {
    private int idCerradura;
    private String ubicacion;
    private String codigoLector;
    private Area area;
    
    public Cerradura(){}
    
    public Cerradura(int idCerradura, String ubicacion, String codigoLector, Area area){
        this.idCerradura = idCerradura;
        this.ubicacion = ubicacion;
        this.codigoLector = codigoLector;
        this.area = area;
    }
    
    public int getIdCerradura(){return idCerradura;}
    public void setIdCerradura(int idCerradura){this.idCerradura = idCerradura;}
    public String getUbicacion(){return ubicacion;}
    public void setUbicacion(String ubicacion){this.ubicacion = ubicacion;}
    public String getCodigoLector(){return codigoLector;}
    public void setCodigoLector(String codigoLector){this.codigoLector = codigoLector;}
    public Area getArea(){return area;}
    public void setArea(Area area){this.area = area;}
    
}
