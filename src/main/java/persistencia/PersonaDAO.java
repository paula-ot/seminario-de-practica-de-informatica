package persistencia;

import excepciones.AccesoDatosException;
import modelo.Empleado;
import modelo.Huesped;
import modelo.Persona;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {
    
    //Consulta base con sus datos de empleado o de huesped
    static final String SELECT_PERSONA = "SELECT p.idPersona, p.dni, p.nombre, p.apellido, p.estado AS estadoPersona,"
            + "e.legajo, e.turno, e.cargo, h.numeroReserva, h.fechaCheckIn, h.fechaCheckOut "
            + "FROM Persona p LEFT JOIN Empleado e ON p.idPersona = e.idPersona LEFT JOIN Huesped h ON p.idPersona = h.idPersona";
    
    //construye empleado o huesped segun las columnas de datos. null si no tiene tipo
    static Persona construirPersona(ResultSet rs) throws SQLException{
        int id = rs.getInt("idPersona");
        String dni = rs.getString("dni");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        boolean estado = rs.getBoolean("estadoPersona");
        if (rs.getString("legajo")!= null){
            return new Empleado(id, dni, nombre, apellido, estado, rs.getString("legajo"), rs.getString("turno"), rs.getString("cargo"));
        }
        if (rs.getString("numeroReserva") != null){
            Timestamp in = rs.getTimestamp("fechaCheckIn");
            Timestamp out = rs.getTimestamp("fechaCheckOut");
            return new Huesped(id, dni, nombre, apellido, estado, rs.getString("numeroReserva"),
                in != null ? in.toLocalDateTime() : null,
                out != null ? out.toLocalDateTime() : null);
        }
        return null; // persona sin tipo definido
    }
    
    public Persona buscarPorDni(String dni) throws AccesoDatosException{
        String sql = SELECT_PERSONA + " WHERE p.dni = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    return construirPersona(rs);
                }
            }
        } catch (SQLException e){
            throw new AccesoDatosException("Error al buscar la persona por dni", e);
        }
        return null;
    }
    
    //busca personas por coincidencia parcial de DNI, nombre y/o apellido
    public List<Persona> buscarPersonas(String dni, String nombre, String apellido) throws AccesoDatosException{
        List<Persona> lista = new ArrayList<>();
        String sql = SELECT_PERSONA + " WHERE p.dni LIKE ? AND p.nombre LIKE ? AND p.apellido LIKE ? ORDER BY p.apellido, p.nombre";
        try (Connection con = ConexionBD.getConexion(); 
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + dni + "%");
            ps.setString(2, "%" + nombre + "%");
            ps.setString(3, "%" + apellido + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Persona p = construirPersona(rs);
                    if (p != null){
                        lista.add(p);
                    }
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar personas", e);
        }
        return lista;
    }
    
    //busca persona dueña de tarjeta
    public Persona buscarPorUID(String codigoUID) throws AccesoDatosException{
        String sql = SELECT_PERSONA + " JOIN Tarjeta t ON t.idPersona = p.idPersona WHERE t.codigoUID = ?";
        try (Connection con = ConexionBD.getConexion(); 
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigoUID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()){
                    return construirPersona(rs);
                }
            }
        } catch (SQLException e){
            throw new AccesoDatosException("Error al buscar la persona de la tarjeta", e);
        }
        return null;
    }
}
