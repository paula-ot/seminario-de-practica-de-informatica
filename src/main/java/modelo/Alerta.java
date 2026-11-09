package modelo;

public class Alerta {
    private int idAlerta;
    private Evento evento;
    private String tipoAnomalia;
    private String nivelGravedad;
    
    public Alerta(){}
    
    public Alerta(int idAlerta, Evento evento, String tipoAnomalia, String nivelGravedad){
        this.idAlerta = idAlerta;
        this.evento = evento;
        this.tipoAnomalia = tipoAnomalia;
        this.nivelGravedad = nivelGravedad;
    }
    
    public int getIdAlerta(){return idAlerta;}
    public void setIdAlerta(int idAlerta){this.idAlerta = idAlerta;}
    public Evento getEvento(){return evento;}
    public void setEvento(Evento evento){this.evento = evento;}
    public String getTipoAnomalia(){return tipoAnomalia;}
    public void setTipoAnomalia(String tipoAnomalia){this.tipoAnomalia = tipoAnomalia;}
    public String getNivelGravedad(){return nivelGravedad;}
    public void setNivelGravedad(String nivelGravedad){this.nivelGravedad = nivelGravedad;}
    
}
