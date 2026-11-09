package persistencia;

import excepciones.AccesoDatosException;
import modelo.Cerradura;
import modelo.Area;
import java.sql.*;

public class CerraduraDAO {
    //busca cerradura y area asociada mediante codigo del lector
    public Cerradura buscarPorCodigoLector(String codigoLector) throws AccesoDatosException{
        String sql = "SELECT c.idCerradura, c.ubicacion, c.codigoLector, a.idArea, a.nombreArea, a.nivelRestriccion " +
                "FROM Cerradura c JOIN Area a ON c.idArea = a.idArea WHERE c.codigoLector = ? ";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, codigoLector);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    Area area = new Area(rs.getInt("idArea"), rs.getString("nombreArea"), rs.getInt("nivelRestriccion"));
                    return new Cerradura(rs.getInt("idCerradura"), rs.getString("ubicacion"), rs.getString("codigoLector"), area);
                }
            }
        } catch (SQLException e){
            throw new AccesoDatosException("Error al buscar la cerradura", e);
        }
        return null;
    }
}
