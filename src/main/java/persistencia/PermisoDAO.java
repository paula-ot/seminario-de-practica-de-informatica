package persistencia;

import excepciones.AccesoDatosException;
import modelo.Permiso;
import modelo.Persona;
import modelo.Area;
import modelo.Tarjeta;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PermisoDAO {
    //registra nuevo permiso en BD con estado true (activo)
    public boolean guardarPermiso(int idPersona, int idArea, int idTarjeta, LocalDateTime inicio, LocalDateTime fin) throws AccesoDatosException{
        String sql = "INSERT INTO Permiso (idPersona, idArea, idTarjeta, fechaInicio, fechaFin, estado) VALUES (?, ?, ?, ?, ?, true)";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, idPersona);
            ps.setInt(2, idArea);
            ps.setInt(3, idTarjeta);
            ps.setTimestamp(4, Timestamp.valueOf(inicio));
            ps.setTimestamp(5, Timestamp.valueOf(fin));
            return ps.executeUpdate() > 0;
        } catch (SQLException e){
            throw new AccesoDatosException("Error al guardar el permiso", e);
        }
    }
    
    //revocacion de permiso
    public boolean revocarPermiso(int idPermiso) throws AccesoDatosException{
        String sql = "UPDATE Permiso SET estado = false WHERE idPermiso = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, idPermiso);
            return ps.executeUpdate() > 0;
        } catch (SQLException e){
            throw new AccesoDatosException("Error al revocar el permiso", e);
        }
    }
    
    //devuelve permisos activos asociados a DNI, con persona (empleado o huesped), area y tarjeta
    public List<Permiso> buscarPermisosPorDni(String dni) throws AccesoDatosException{
        List<Permiso> lista = new ArrayList<>();
        String sql = "SELECT p.idPermiso, p.fechaInicio, p.fechaFin, p.estado, "
                + "per.idPersona, per.dni, per.nombre, per.apellido, per.estado AS estadoPersona, "
                + "e.legajo, e.turno, e.cargo, h.numeroReserva, h.fechaCheckIn, h.fechaCheckOut, "
                + "a.idArea, a.nombreArea, a.nivelRestriccion, "
                + "t.idTarjeta, t.codigoUID, t.estado AS estadoTarjeta, t.fechaEmision "
                + "FROM Permiso p JOIN Persona per ON p.idPersona = per.idPersona "
                + "LEFT JOIN Empleado e ON per.idPersona = e.idPersona "
                + "LEFT JOIN Huesped h ON per.idPersona = h.idPersona "
                + "JOIN Area a ON p.idArea = a.idArea "
                + "LEFT JOIN Tarjeta t ON p.idTarjeta = t.idTarjeta "
                + "WHERE per.dni = ? AND p.estado = true ORDER BY p.fechaInicio DESC";
        try (Connection con = ConexionBD.getConexion(); 
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Persona persona = PersonaDAO.construirPersona(rs);
                    Area area = new Area(rs.getInt("idArea"), rs.getString("nombreArea"), rs.getInt("nivelRestriccion"));
                    Tarjeta tarjeta = null;
                    if (rs.getInt("idTarjeta") != 0) {
                        Timestamp ts = rs.getTimestamp("fechaEmision");
                        tarjeta = new Tarjeta(rs.getInt("idTarjeta"), rs.getString("codigoUID"),
                                rs.getBoolean("estadoTarjeta"), ts != null ? ts.toLocalDateTime() : LocalDateTime.now());
                    }
                    lista.add(new Permiso(rs.getInt("idPermiso"), persona, area, tarjeta,
                            rs.getTimestamp("fechaInicio").toLocalDateTime(),
                            rs.getTimestamp("fechaFin").toLocalDateTime(),
                            rs.getBoolean("estado")));
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al consultar los permisos", e);
        }
        return lista;
    }
    
    //true si existe permiso activo para tarjeta sobre el area en la fechaHora
    public boolean validarPermisoVigente(int idTarjeta, int idArea, LocalDateTime fechaHora) throws AccesoDatosException{
        String sql = "SELECT COUNT(*) FROM Permiso WHERE idTarjeta = ? AND idArea = ? AND estado = true " +
                "AND ? BETWEEN fechaInicio AND fechaFin";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, idTarjeta);
            ps.setInt(2, idArea);
            ps.setTimestamp(3, Timestamp.valueOf(fechaHora));
            try (ResultSet rs = ps.executeQuery()){
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e){
            throw new AccesoDatosException("Error al validar el permiso", e);
        }
    }
}
