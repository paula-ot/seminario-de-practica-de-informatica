package persistencia;

import modelo.Alerta;
import excepciones.AccesoDatosException;
import java.sql.*;

public class AlertaDAO {
    public boolean guardarAlerta(Alerta alerta) throws AccesoDatosException{
        String sql = "INSERT INTO Alerta (idEvento, tipoAnomalia, nivelGravedad) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, alerta.getEvento().getIdEvento());
            ps.setString(2, alerta.getTipoAnomalia());
            ps.setString(3, alerta.getNivelGravedad());
            return ps.executeUpdate() > 0;
        } catch (SQLException e){
            throw new AccesoDatosException("Error al guardar la alerta", e);
        }
    }
}
