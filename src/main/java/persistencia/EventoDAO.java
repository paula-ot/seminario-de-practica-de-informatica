package persistencia;

import excepciones.AccesoDatosException;
import modelo.Evento;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {
    //guarda evento evaluado en historial de BD y retorna su ID
    public int guardarEvento(Evento evento, int idCerradura, int idArchivoLog) throws AccesoDatosException{
        String sql = "INSERT INTO Evento (fechaHora, codigoLeido, resultado, idCerradura, idArchivoLog) VALUES (?, ?, ?, ?, ?)";
                try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                    ps.setTimestamp(1, Timestamp.valueOf(evento.getFechaHora()));
                    ps.setString(2, evento.getCodigoLeido());
                    ps.setBoolean(3, evento.isResultado());
                    ps.setInt(4, idCerradura);
                    ps.setInt(5, idArchivoLog);
                    
                    if(ps.executeUpdate() > 0){
                        try (ResultSet rs = ps.getGeneratedKeys()){
                            if (rs.next()){
                                return rs.getInt(1);
                            }
                        }
                    }
                    return -1;
                } catch (SQLException e){
                    throw new AccesoDatosException("Error al guardar el evento", e);
            }
    }
    
    //fechas inicio y fin obligatorias
    //area, y UID opcionales
    //fetorna filas para la tabla UID, fechaHora, Area, Persona
    public List<Object[]> consultarHistorial(LocalDateTime inicio, LocalDateTime fin, String area, String uidTarjeta) throws AccesoDatosException{
        List<Object[]> resultados = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT e.codigoLeido, e.fechaHora, a.nombreArea, "
                + "COALESCE(CONCAT(p.nombre, ' ', p.apellido), 'No registrada') AS persona "
                + "FROM Evento e JOIN Cerradura c ON e.idCerradura = c.idCerradura "
                + "JOIN Area a ON c.idArea = a.idArea "
                + "LEFT JOIN Tarjeta t ON e.codigoLeido = t.codigoUID "
                + "LEFT JOIN Persona p ON t.idPersona = p.idPersona "
                + "WHERE e.fechaHora BETWEEN ? AND ? ");
        boolean filtraArea = area != null && !area.trim().isEmpty();
        boolean filtraUid = uidTarjeta != null && !uidTarjeta.trim().isEmpty();        
        if (filtraArea){
            sql.append("AND a.nombreArea = ? ");
        } 
        if (filtraUid){
            sql.append("AND e.codigoLeido = ? ");
        }
        sql.append("ORDER BY e.fechaHora DESC");
        
        try (Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(sql.toString())){
            ps.setTimestamp(1, Timestamp.valueOf(inicio));
            ps.setTimestamp(2, Timestamp.valueOf(fin));
            
            int paramIndex = 3;
            if (filtraArea){
                ps.setString(paramIndex++, area.trim());
            }
            if (filtraUid){
                ps.setString(paramIndex++, uidTarjeta.trim());
            }
            
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    resultados.add(new Object[]{
                        rs.getString("codigoLeido"),
                        rs.getTimestamp("fechaHora").toString(),
                        rs.getString("nombreArea"),
                        rs.getString("persona")});
                    }
                }
            } catch (SQLException e){
                throw new AccesoDatosException("Error al consultar el historial", e);
        }
        return resultados;
    }
    
    //eventos permitidos del periodo (base del reporte de asistencia) en orden cronologico
    public List<Evento> listarPermitidos(LocalDateTime inicio, LocalDateTime fin, String area, String uidTarjeta) throws AccesoDatosException{
        List<Evento> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT e.idEvento, e.fechaHora, e.codigoLeido FROM Evento e JOIN Cerradura c ON e.idCerradura = c.idCerradura "
            + "JOIN Area a ON c.idArea = a.idArea WHERE e.resultado = TRUE AND e.fechaHora BETWEEN ? AND ? ");
        boolean filtraArea = area != null && !area.trim().isEmpty();
        boolean filtraUid = uidTarjeta != null && !uidTarjeta.trim().isEmpty();
        if (filtraArea){
            sql.append("AND a.nombreArea = ? ");
        }
        if (filtraUid){
            sql.append("AND e.codigoLeido = ? ");
        }
        sql.append("ORDER BY e.fechaHora ASC");
        
        try (Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(sql.toString())){
            ps.setTimestamp(1, Timestamp.valueOf(inicio));
            ps.setTimestamp(2, Timestamp.valueOf(fin));
            int paramIndex = 3;
            if (filtraArea){
                ps.setString(paramIndex++, area.trim());
            }
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    Evento ev = new Evento();
                    ev.setIdEvento(rs.getInt("idEvento"));
                    ev.setFechaHora(rs.getTimestamp("fechaHora").toLocalDateTime());
                    ev.setCodigoLeido(rs.getString("codigoLeido"));
                    ev.setResultado(true);
                    lista.add(ev);
                }
            }
    } catch (SQLException e){
        throw new AccesoDatosException("Error al listar los accesos permitidos", e);
    }
    return lista;
}
}
