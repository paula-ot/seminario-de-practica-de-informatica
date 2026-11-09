package modelo;

import java.time.LocalDateTime;

public class Evento {
    private int idEvento;
    private LocalDateTime fechaHora;
    private String codigoLeido;
    private boolean resultado;
    private Cerradura cerradura;
    private String nombreArchivoLog;
    
    public Evento(){}
    
    public Evento(int idEvento, LocalDateTime fechaHora, String codigoLeido, boolean resultado, Cerradura cerradura, String nombreArchivoLog){
        this.idEvento = idEvento;
        this.fechaHora = fechaHora;
        this.codigoLeido = codigoLeido;
        this.resultado = resultado;
        this.cerradura = cerradura;
        this.nombreArchivoLog = nombreArchivoLog;
    }
    
    public int getIdEvento(){return idEvento;}
    public void  setIdEvento(int idEvento){this.idEvento = idEvento;}
    public LocalDateTime getFechaHora(){return fechaHora;}
    public void setFechaHora(LocalDateTime fechaHora){this.fechaHora = fechaHora;}
    public String getCodigoLeido(){return codigoLeido;}
    public void setCodigoLeido(String codigoLeido){this.codigoLeido = codigoLeido;}
    public boolean isResultado(){return resultado;}
    public void setResultado(boolean resultado){this.resultado = resultado;}
    public Cerradura getCerradura(){return cerradura;}
    public void setCerradura(Cerradura cerradura){this.cerradura = cerradura;}
    public String getNombreArchivoLog(){return nombreArchivoLog;}
    public void setNombreArchivoLog(String nombreArchivoLog){this.nombreArchivoLog = nombreArchivoLog;}
}
