package controladores;

import modelo.ArchivoLog;
import modelo.RegistroEventoTemp;
import persistencia.ArchivoLogDAO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

//selecciona, abre, valida el formato de 4 campos del archivo, lo vuelca en memoria y guarda la trazabilidad en ArchivoLog
public class ControladorImportacion {
    private ArchivoLogDAO archivoLogDAO;
    
    public ControladorImportacion(){
        this.archivoLogDAO = new ArchivoLogDAO();
    }
    
    //valida el archivo, descarta lineas invaildas, registra el archivo en ArchivoLog y retorna lineas validas en un objeto RegistroEventoTemp
    public RegistroEventoTemp importarArchivo(File archivo) throws Exception{
        if (archivo == null || !archivo.exists() || !archivo.getName().toLowerCase().endsWith(".log"))
        {
            throw new Exception("El archivo seleccionado no existe o tiene un formato no valido");
        }
        RegistroEventoTemp eventoTemp = new RegistroEventoTemp();
        eventoTemp.setFechaHora(LocalDateTime.now());
        
        //registrar archivo en el modelo
        ArchivoLog logMeta = new ArchivoLog();
        logMeta.setNombreArchivo(archivo.getName());
        logMeta.setFechaImportacion(LocalDateTime.now());
        
        int lineasValidas = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))){
            String linea;
            while ((linea = br.readLine()) != null){
                linea = linea.trim();
                if (!linea.isEmpty() && validarFormatoLinea(linea)){
                    eventoTemp.agregar(linea);
                    lineasValidas++;
                }
            }
        } catch (IOException e){
            throw new Exception("Error al leer el archivo: " + e.getMessage());
        }
        
        if (lineasValidas == 0){
            throw new Exception("El archivo no contiene registros con formato valido.");
        }
        logMeta.setCantidadRegistros(lineasValidas);
        
        //guarda metadatos en la base de datos
        int idLogGenerado = archivoLogDAO.registrarArchivoLog(logMeta);
        logMeta.setIdArchivo(idLogGenerado);
        
        eventoTemp.setArchivoLog(logMeta);
        return eventoTemp;
    }
    
    //valida que linea tenga 4 campos separados por un ";" o ","
    //formato esperado: [FechaHora, UIDTarjeta, CodigoLector, Resultado]
    private boolean validarFormatoLinea(String linea){
        String[] partes = linea.split("[,;]");
        return partes.length == 4;
    }
}
