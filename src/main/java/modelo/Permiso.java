package modelo;

import java.time.LocalDateTime;

public class Permiso {
    private int idPermiso;
    private Persona persona;
    private Area area;
    private Tarjeta tarjeta;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean estado;
    
    public Permiso(){}
    
    public Permiso(int idPermiso, Persona persona, Area area, Tarjeta tarjeta, LocalDateTime fechaInicio, LocalDateTime fechaFin, boolean estado){
        this.idPermiso = idPermiso;
        this.persona = persona;
        this.area = area;
        this.tarjeta = tarjeta;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }
    
    public int getIdPermiso(){return idPermiso;}
    public void setIdPermiso(int idPermiso){this.idPermiso = idPermiso;}
    public Persona getPersona(){return persona;}
    public void setPersona(Persona persona){this.persona = persona;}
    public Area getArea(){return area;}
    public void setArea(Area area){this.area = area;};
    public Tarjeta getTarjeta(){return tarjeta;}
    public void setTarjeta(Tarjeta tarjeta){this.tarjeta = tarjeta;}
    public LocalDateTime getFechaInicio(){return fechaInicio;}
    public void setFechaInicio(LocalDateTime fechaInicio){this.fechaInicio = fechaInicio;}
    public LocalDateTime getFechaFin(){return fechaFin;}
    public void setFechaFin(LocalDateTime fechaFin){this.fechaFin = fechaFin;}
    public boolean isEstado(){return estado;}
    public void setEstado(boolean estado){this.estado = estado;}
    
}
