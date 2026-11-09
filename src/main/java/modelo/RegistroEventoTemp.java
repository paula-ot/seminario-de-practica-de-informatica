package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//temporal en memoria para almacenar el buffer de lineas no parseadas del .log
public class RegistroEventoTemp {
    private String codigoLector;
    private LocalDateTime fechaHora;
    private ArchivoLog archivoLog;
    private List<String> listaEventos;
    
    public RegistroEventoTemp(){
        this.listaEventos = new ArrayList<>();
    }
    
    public RegistroEventoTemp(String codigoLector, LocalDateTime fechaHora, ArchivoLog archivoLog){
        this.codigoLector = codigoLector;
        this.fechaHora = fechaHora;
        this.archivoLog = archivoLog;
        this.listaEventos = new ArrayList<>();
    }
    
    //metodo para agregar una linea leida del archivo a la lista
    public void agregar(String linea){
        if (linea != null && !linea.trim().isEmpty()){
            this.listaEventos.add(linea);
        }
    }
    
    //obtener una linea para parsear
    public String obtenerEvento(int indice){
        if (indice >= 0 && indice < listaEventos.size()){
            return listaEventos.get(indice);
        }
        return null;
    }
    
    //cantidad total de registros/lineas en el contenedor
    public int cantRegistro(){
        return listaEventos.size();
    }
    
    public String getCodigoLector(){return codigoLector;}
    public void setCodigoLector(String codigoLector){this.codigoLector = codigoLector;}
    public LocalDateTime getFechaHora(){return fechaHora;}
    public void setFechaHora(LocalDateTime fechaHora){this.fechaHora = fechaHora;}
    public ArchivoLog getArchivoLog(){return archivoLog;}
    public void setArchivoLog(ArchivoLog archivoLog){this.archivoLog = archivoLog;}
    public List<String> getListaEventos(){return listaEventos;}
    public void setListaEventos(List<String> listaEventos){this.listaEventos = listaEventos;}
    }
    

