package modelo;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Empleado extends Persona {
    private String legajo;
    private String turno;
    private String cargo;
    
    public Empleado(){super();}
    
    public Empleado(int idPersona, String  dni, String nombre, String apellido, boolean estado, String legajo, String turno, String cargo){
        super(idPersona, dni, nombre, apellido, estado);
        this.legajo = legajo;
        this.turno = turno;
        this.cargo = cargo;
    }
    
    public String getLegajo(){return legajo;}
    public void setLegajo(String legajo){this.legajo = legajo;}
    public String getTurno(){return turno;}
    public void setTurno(String turno){this.turno = turno;}
    public String getCargo(){return cargo;}
    public void setCargo(String cargo){this.cargo = cargo;}

    //si el empleado puede acceder en la hora según su turno. 
    //horarios por turno: mañana 06-14, tarde 14-22, noche 22-06, rotativo sin restriccion
    @Override
    public boolean estaHabilitadoEn(LocalDateTime fechaHora){
        //normalizer quita tildes y mayusculas para comparar nombre de turno
        String t = Normalizer.normalize(turno == null ? "" : turno, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase();
        LocalTime hora = fechaHora.toLocalTime();
        if (t.startsWith("ma")){
            return !hora.isBefore(LocalTime.of(6, 0)) && hora.isBefore(LocalTime.of(14, 0));
        } else if (t.startsWith("ta")){
            return !hora.isBefore(LocalTime.of(14, 0)) && hora.isBefore(LocalTime.of(22, 0));
        } else if (t.startsWith("no")){
            return !hora.isBefore(LocalTime.of(22, 0))|| hora.isBefore(LocalTime.of(6, 0));
        }
        return true; //turno rotativo sin restriccion horaria
    }
    
    @Override
    public String getMotivoNoHabilitado(){
        return "Ingreso fuera de turno";
    }
    
    @Override
    public String getDetalleReporte(){
        return getNombreCompleto() + "(Legajo: " + legajo + ", " + cargo + ")";
    }
    
    @Override
    public boolean controlaAsistencia(){
        return true;
    }
}
