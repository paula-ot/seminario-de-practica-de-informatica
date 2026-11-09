package modelo;

import java.time.LocalDateTime;

public class ArchivoLog {
    private int idArchivo;
    private String nombreArchivo;
    private LocalDateTime fechaImportacion;
    private int cantidadRegistros;
    
    public ArchivoLog(){}
    
    public ArchivoLog(int idArchivo, String nombreArchivo, LocalDateTime fechaImportacion, int cantidadRegistros){
    this.idArchivo = idArchivo;
    this.nombreArchivo = nombreArchivo;
    this.fechaImportacion = fechaImportacion;
    this.cantidadRegistros = cantidadRegistros;
}
    
    public int getIdArchivo(){return idArchivo;}
    public void setIdArchivo(int idArchivo){this.idArchivo = idArchivo;}
    public String getNombreArchivo() {return nombreArchivo;}
    public void setNombreArchivo(String nombreArchivo){this.nombreArchivo = nombreArchivo;}
    public LocalDateTime getFechaImportacion(){return fechaImportacion;}
    public void setFechaImportacion(LocalDateTime fechaImportacion){this.fechaImportacion = fechaImportacion;}
    public int getCantidadRegistros(){return cantidadRegistros;}
    public void setCantidadRegistros(int cantidadRegistros){this.cantidadRegistros = cantidadRegistros;}
}
