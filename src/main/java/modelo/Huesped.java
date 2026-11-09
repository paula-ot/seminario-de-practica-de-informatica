package modelo;

import java.time.LocalDateTime;

public class Huesped extends Persona{
    private String numeroReserva;
    private LocalDateTime fechaCheckIn;
    private LocalDateTime fechaCheckOut;
    
    public Huesped(){super();}
    
    public Huesped(int idPersona, String dni, String nombre, String apellido, boolean estado, String numeroReserva, LocalDateTime fechaCheckIn, LocalDateTime fechaCheckOut){
        super(idPersona, dni, nombre, apellido, estado);
        this.numeroReserva = numeroReserva;
        this.fechaCheckIn = fechaCheckIn;
        this.fechaCheckOut = fechaCheckOut;
        }
    
    public String getNumeroReserva(){return numeroReserva;}
    public void setNumeroReserva(String numeroReserva){this.numeroReserva = numeroReserva;}
    public LocalDateTime getFechaCheckIn(){return fechaCheckIn;}
    public void setFechaCheckIn(LocalDateTime fechaCheckIn){this.fechaCheckIn = fechaCheckIn;}
    public LocalDateTime getFechaCheckOut(){return fechaCheckOut;}
    public void setFechaCheckOut(LocalDateTime fechaCheckOut){this.fechaCheckOut = fechaCheckOut;}

    //huesped solo puede acceder durante su entre su check-in y su check-out
    @Override
    public boolean estaHabilitadoEn(LocalDateTime fechaHora){
        if (fechaCheckIn == null || fechaCheckOut == null){
            return false;
        }
        return !fechaHora.isBefore(fechaCheckIn) && !fechaHora.isAfter(fechaCheckOut);
    }
    
    @Override
    public String getMotivoNoHabilitado(){
        return "Acceso fuera del periodo de estadía";
    }
    
    @Override
    public String getDetalleReporte(){
        return getNombreCompleto() + "(Reserva: " + numeroReserva + ")";
    }

    @Override
    public boolean controlaAsistencia(){
        return false;
    }
}
