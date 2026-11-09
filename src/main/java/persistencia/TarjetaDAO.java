package persistencia;

import excepciones.AccesoDatosException;
import modelo.Tarjeta;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TarjetaDAO {
    public Tarjeta buscarPorUID(String codigoUID) throws AccesoDatosException{
        String sql = "SELECT idTarjeta, codigoUID, estado, fechaEmision FROM Tarjeta WHERE codigoUID = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, codigoUID);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    Timestamp ts = rs.getTimestamp("fechaEmision");
                    LocalDateTime fechaEmision = (ts != null) ? ts.toLocalDateTime() : LocalDateTime.now();
                    return new Tarjeta(rs.getInt("idTarjeta"), rs.getString("codigoUID"), rs.getBoolean("estado"), fechaEmision);
                }
            }
        } catch (SQLException e){
            throw new AccesoDatosException("Error al buscar la tarjeta", e);
        }
        return null;
    }
    
    //inserta nueva tarjeta con estado true (activa) y la fecha de emision actual
    public Tarjeta registrarTarjeta(String codigoUID, int idPersona) throws AccesoDatosException{
        String sql = "INSERT INTO Tarjeta (codigoUID, estado, fechaEmision, idPersona) VALUES (?, true, NOW(), ?)";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, codigoUID);
            ps.setInt(2, idPersona);
            if (ps.executeUpdate() > 0){
                try (ResultSet rs = ps.getGeneratedKeys()){
                    if (rs.next()){
                        int idGen = rs.getInt(1);
                        return new Tarjeta(idGen, codigoUID, true, LocalDateTime.now());
                    }
                }
            }
            return null;
        } catch (SQLException e){
            throw new AccesoDatosException("Error al registrar la tarjeta", e);
        }
    }
    
    //revocacion de tarjeta
    public boolean desactivarTarjeta(String codigoUID) throws AccesoDatosException{
        String sql = "UPDATE Tarjeta SET estado = false WHERE codigoUID = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, codigoUID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e){
            throw new AccesoDatosException("Error al desactivar la tarjeta", e);
        }
    }
    
    //listar tarjetas por persona
    public List<Tarjeta> listarPorPersona(int idPersona) throws AccesoDatosException{
        List<Tarjeta> lista = new ArrayList<>();
        String sql = "SELECT idTarjeta, codigoUID, estado, fechaEmision FROM Tarjeta WHERE idPersona = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPersona);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("fechaEmision");
                    lista.add(new Tarjeta(rs.getInt("idTarjeta"), rs.getString("codigoUID"),
                            rs.getBoolean("estado"), ts != null ? ts.toLocalDateTime() : LocalDateTime.now()));
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar las tarjetas", e);
        }
        return lista;
    }
}
