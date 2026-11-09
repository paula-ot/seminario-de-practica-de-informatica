package persistencia;

import modelo.ArchivoLog;
import excepciones.AccesoDatosException;
import java.sql.*;

public class ArchivoLogDAO {
    public int registrarArchivoLog(ArchivoLog log) throws AccesoDatosException{
        String sql = "INSERT INTO ArchivoLog (nombreArchivo, fechaImportacion, cantidadRegistros) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, log.getNombreArchivo());
            ps.setTimestamp(2, Timestamp.valueOf(log.getFechaImportacion()));
            ps.setInt(3, log.getCantidadRegistros());
            if (ps.executeUpdate() > 0){
                try (ResultSet rs = ps.getGeneratedKeys()){
                    if (rs.next()){
                        return rs.getInt(1); //retorna pk generada
                    }
                }
            }
            return -1;
        } catch (SQLException e){
            throw new AccesoDatosException("Error al registrar el archivo de log", e);
        }
    }
}