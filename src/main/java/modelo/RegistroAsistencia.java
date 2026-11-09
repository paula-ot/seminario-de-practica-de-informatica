package modelo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//asistencia de una persona en un dia
//ingreso = primer acceso permitido
//egreso = ultimo acceso
public class RegistroAsistencia {
    private Persona persona;
    private LocalDate fecha;
    private LocalDateTime ingreso;
    private LocalDateTime egreso;
    
    public RegistroAsistencia(Persona persona, LocalDate fecha, LocalDateTime ingreso){
        this.persona = persona;
        this.fecha = fecha;
        this.ingreso = ingreso;
    }
    
    //los accesos llegan en orden cronologico, el ultimo pasa a ser egreso
    public void registrarAcceso(LocalDateTime fechaHora){
        if (fechaHora.isAfter(ingreso)){
            egreso = fechaHora;
        }
    }
    
    public String getHorasTexto(){
        if (egreso == null){
            return "-";
        }
        Duration d = Duration.between(ingreso, egreso);
        return String.format("%02d:%02d", d.toHours(), d.toMinutes() % 60);
    }
    
    //fila para la tabla y para el archivo. persona, fecha, ingreso, egreso, horas
    public String[] aFila(){
        DateTimeFormatter hora = DateTimeFormatter.ofPattern("HH:mm:ss");
        return new String[]{
            persona.getDetalleReporte(), fecha.toString(), ingreso.format(hora), 
            egreso != null ? egreso.format(hora) : "Sin egreso", getHorasTexto()
        };
    }
    
    public Persona getPersona(){return persona;}
    public LocalDate getFecha(){return fecha;}
    public LocalDateTime getIngreso(){return ingreso;}
    public LocalDateTime getEgreso(){return egreso;}

}
