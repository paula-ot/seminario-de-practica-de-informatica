package modelo;

import java.time.LocalDateTime;

public class Tarjeta {
    private int idTarjeta;
    private String codigoUID;
    private boolean estado;
    private LocalDateTime fechaEmision;
    
    public Tarjeta(){}
    
    public Tarjeta(int idTarjeta, String codigoUID, boolean estado, LocalDateTime fechaEmision){
        this.idTarjeta = idTarjeta;
        this.codigoUID = codigoUID;
        this.estado = estado;
        this.fechaEmision = fechaEmision;
    }
    
    public Tarjeta(String codigoUID, boolean estado, LocalDateTime fechaEmision){
        this.codigoUID = codigoUID;
        this.estado = estado;
        this.fechaEmision = fechaEmision;
    }
    
    public int getIdTarjeta(){return idTarjeta;}
    public void setIdTarjeta(int idTarjeta){this.idTarjeta = idTarjeta;}
    public String getCodigoUID(){return codigoUID;}
    public void setCodigoUID(String codigoUID){this.codigoUID = codigoUID;}
    public boolean isEstado(){return estado;}
    public void setEstado(boolean estado){this.estado = estado;}
    public LocalDateTime getFechaEmision(){return fechaEmision;}
    public void setFechaEmision(LocalDateTime fechaEmision){this.fechaEmision = fechaEmision;}
    
    @Override
    public String toString(){
        return codigoUID + "("+ (estado ? "ACTIVA":"REVOCADA") +")";
    }
}
