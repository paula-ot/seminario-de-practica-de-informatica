package persistencia;

import excepciones.AccesoDatosException;
import modelo.Area;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AreaDAO {
    public List<Area> listarTodas() throws AccesoDatosException{
        List<Area> lista = new ArrayList<>();
        String sql = "SELECT idArea, nombreArea, nivelRestriccion FROM Area ORDER BY nombreArea ASC";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        Area area = new Area(rs.getInt("idArea"), rs.getString("nombreArea"), rs.getInt("nivelRestriccion"));
                        lista.add(area);
                    }
        } catch (SQLException e){
            throw new AccesoDatosException("Error al consultar las areas", e);
        }
        return lista;
    }
    
    public Area buscarPorNombre(String nombreArea) throws AccesoDatosException{
        String sql = "SELECT idArea, nombreArea, nivelRestriccion FROM Area WHERE nombreArea = ?";
        try(Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, nombreArea);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    return new Area(rs.getInt("idArea"), rs.getString("nombreArea"), rs.getInt("nivelRestriccion"));
                }
            }
        } catch (SQLException e){
            throw new AccesoDatosException("Error al buscar el area", e);
        }
        return null;
    }
}
