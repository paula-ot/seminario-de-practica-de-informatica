package modelo;

import java.time.LocalDateTime;

public abstract class Persona {
    private int idPersona;
    private String dni;
    private String nombre;
    private String apellido;
    private boolean estado; //true activo, false inactivo/baja
    
    public Persona() {}
    
    public Persona(int idPersona, String dni, String nombre, String apellido, boolean estado){
        this.idPersona = idPersona;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.estado = estado;
    }
    
    public int getIdPersona(){return idPersona;}
    public void setIdPersona(int idPersona){this.idPersona = idPersona;}
    public String getDni(){return dni;}
    public void setDni(String dni){this.dni = dni;}
    public String getNombre(){return nombre;}
    public void setNombre(String nombre){this.nombre = nombre;}
    public String getApellido(){return apellido;}
    public void setApellido(String apellido){this.apellido = apellido;}
    public boolean isEstado(){return estado;}
    public void setEstado(boolean estado){this.estado = estado;}
    public String getNombreCompleto(){
        return nombre +" "+ apellido;
    }
    
    @Override
    public String toString(){
        return getNombreCompleto() + "(DNI: "+ dni + ")";
    }
    
    public abstract boolean estaHabilitadoEn(LocalDateTime fechaHora);
    public abstract String getMotivoNoHabilitado();
    public abstract String getDetalleReporte();
    public abstract boolean controlaAsistencia();
}
