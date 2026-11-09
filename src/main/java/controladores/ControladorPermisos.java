package controladores;

import excepciones.AccesoDatosException;
import modelo.Persona;
import modelo.Tarjeta;
import persistencia.PermisoDAO;
import persistencia.PersonaDAO;
import persistencia.TarjetaDAO;
import java.time.LocalDateTime;

public class ControladorPermisos {
    private PersonaDAO personaDAO;
    private TarjetaDAO tarjetaDAO;
    private PermisoDAO permisoDAO;
    
    public ControladorPermisos(){
        this.personaDAO = new PersonaDAO();
        this.tarjetaDAO = new TarjetaDAO();
        this.permisoDAO = new PermisoDAO();
    }
    
    public Persona buscarPersonaPorDni(String dni) throws AccesoDatosException{
        return personaDAO.buscarPorDni(dni);
    }
    
    //guarda un permiso activo
    //valida que inicio sea anterior a fin
    //si tarjeta con uid no existe, la crea vinculandola a persona
    public boolean crearPermiso(int idPersona, int idArea, String uidTarjeta, LocalDateTime inicio, LocalDateTime fin) throws Exception{
        //valida fechas
        if (inicio.isAfter(fin) || inicio.isEqual(fin)){
            throw new Exception("La fecha de inicio debe ser anterior a la fecha de fin.");
        }
        //verifica existencia de tarjeta o la crea
        Tarjeta tarjeta = tarjetaDAO.buscarPorUID(uidTarjeta);
        if (tarjeta == null){
            tarjeta = tarjetaDAO.registrarTarjeta(uidTarjeta, idPersona);
            if (tarjeta == null){
                throw new Exception("Error al registrar la nueva tarjeta con UID: " + uidTarjeta);
            }
        }
        //crea el permiso activo
        return permisoDAO.guardarPermiso(idPersona, idArea, tarjeta.getIdTarjeta(), inicio, fin);
    }
    //revocacion de permiso
    public boolean revocarPermiso(int idPermiso) throws AccesoDatosException{
        return permisoDAO.revocarPermiso(idPermiso);
    }
}
