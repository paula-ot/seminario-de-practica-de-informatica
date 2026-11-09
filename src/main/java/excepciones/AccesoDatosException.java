package excepciones;
//error al interactuar con MySQL (conexion o consulta). envuelve excepcion original y la envia a pantalla
public class AccesoDatosException extends Exception{
    public AccesoDatosException(String mensaje, Throwable causa){
        super(mensaje + (causa != null ? ": " + causa.getMessage() : ""), causa);
    }
}
