package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

//Gestiona la conexión JDBC con OceanviewBD
public class ConexionBD {
    private static final Logger LOGGER = Logger.getLogger(ConexionBD.class.getName());
    //Configura conexion a MySQL
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/OceanviewBD?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    //cambiar usuario
    private static final String USER = "root";
    
    //cambiar contraseña
    private static final String PASSWORD = "";
    
    private static Connection conexion = null;
    
    //obtiene conexion a base de datos, si no existe o esta cerrada, la abre.
    //return Connection JDBC activo
    public static Connection getConexion() throws SQLException{
        try{
            if (conexion == null || conexion.isClosed()){
                //carga driver JDBC de MySQL
                Class.forName(DRIVER);
                //Establece conexion con URL y credenciales
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                LOGGER.info("Conexion con OceanviewBD establecida exitosamente");
            }
        } catch (ClassNotFoundException ex){
            LOGGER.log(Level.SEVERE, "Error: No se encontro el driver JDBC de MySQL.",ex);
            throw new SQLException("No se encontro el driver JDBC de MySQL.", ex);
        } catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Error al conectar con la base de datos MySQL.", ex);
            throw ex;
        }
        return conexion;
    }
    //cierra conexion con la base de datos
    public static void cerrarConexion(){
        if (conexion != null){
            try{
                if (!conexion.isClosed()){
                    conexion.close();
                    LOGGER.info("Conexion con OceanviewBD cerrada correctamente.");
                }
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "Error al intentar cerrar la conexion con la base de datos", ex);
            }
        }
    }
}
