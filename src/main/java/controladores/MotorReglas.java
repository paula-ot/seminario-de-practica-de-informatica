package controladores;

import excepciones.AccesoDatosException;
import modelo.Alerta;
import modelo.Cerradura;
import modelo.Evento;
import modelo.RegistroEventoTemp;
import modelo.Tarjeta;
import persistencia.CerraduraDAO;
import persistencia.AlertaDAO;
import persistencia.EventoDAO;
import persistencia.PermisoDAO;
import persistencia.TarjetaDAO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import modelo.Persona;
import persistencia.PersonaDAO;

//procesa en bucle los registros contenidos en RegistroEventoTemp
//evalua los permisos en la base de adtos, inserta los eventos y registra alertas si detecta anomalias
public class MotorReglas {
    private EventoDAO eventoDAO;
    private AlertaDAO alertaDAO;
    private TarjetaDAO tarjetaDAO;
    private PermisoDAO permisoDAO;
    private CerraduraDAO cerraduraDAO;
    private PersonaDAO personaDAO;
    
    public MotorReglas(){
        this.eventoDAO = new EventoDAO();
        this.alertaDAO = new AlertaDAO();
        this.tarjetaDAO = new TarjetaDAO();
        this.permisoDAO = new PermisoDAO();
        this.cerraduraDAO = new CerraduraDAO();
        this.personaDAO = new PersonaDAO();
    }
    
    //recorre lineas del registro temporal, evalua cada acceso, guarda el evento y genera alertas.
    //devuelve arreglo con metricas [procesados, validos, alertas]
    public int[] procesarEventos(RegistroEventoTemp registroTemp) throws AccesoDatosException{
        int totalProcesados = registroTemp.cantRegistro();
        int totalValidos = 0;
        int totalAlertas = 0;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        for (int i = 0; i < totalProcesados; i++){
            String linea = registroTemp.obtenerEvento(i);
            String[] campos = linea.split("[,;]");
            try{
                LocalDateTime fechaHora = LocalDateTime.parse(campos[0].trim(), formatter);
                String uidLeido = campos[1].trim();
                String codigoLector = campos[2].trim();
                
                //obtiene la cerradura fisica por el codigo del lector
                Cerradura cerradura = cerraduraDAO.buscarPorCodigoLector(codigoLector);
                if (cerradura == null) {
                    throw new Exception("Lector desconocido: " + codigoLector);
                }
                int idCerradura = cerradura.getIdCerradura();
                //evalua reglas de acceso y determina anomalia si corresponde
                ResultadoEvaluacion eval = evaluarReglaAcceso(uidLeido, fechaHora, cerradura);
                
                //crea y guarda el objeto Evento en la base de datos
                Evento evento = new Evento();
                evento.setFechaHora(fechaHora);
                evento.setCodigoLeido(uidLeido);
                evento.setResultado(eval.esPermitido());
                evento.setCerradura(cerradura);
                evento.setNombreArchivoLog(registroTemp.getArchivoLog().getNombreArchivo());
                
                int idEventoGenerado = eventoDAO.guardarEvento(evento, idCerradura, registroTemp.getArchivoLog().getIdArchivo());
                evento.setIdEvento(idEventoGenerado);
                
                //registra alerta si hubo anomalia
                if (eval.esPermitido()){
                    totalValidos++;
                } else{
                    totalAlertas++;
                    Alerta alerta = new Alerta();
                    alerta.setEvento(evento);
                    alerta.setTipoAnomalia(eval.getTipoAnomalia());
                    alerta.setNivelGravedad(eval.getNivelGravedad());
                    
                    alertaDAO.guardarAlerta(alerta);
                }
            }//corta el procesamiento si falla la base
            //si falla una linea se informa y sigue con la siguiente
             catch(AccesoDatosException e){
                throw e;
            } catch (Exception e){
                System.err.println("Error al procesar la linea " + i + ": " + e.getMessage());
            }
        }
        return new int[]{totalProcesados, totalValidos, totalAlertas};
    }
    
    //evalua condiciones del acceso en orden:
    //tarjeta registrada -> tarjeta activa -> permiso vigente en el area
    //-> persona fuera de turno (empleado) o estadia (huesped)
    public ResultadoEvaluacion evaluarReglaAcceso(String uidTarjeta, LocalDateTime fechaHora, Cerradura cerradura) throws AccesoDatosException{
        //regla 1: tarjeta registrada
        Tarjeta tarjeta = tarjetaDAO.buscarPorUID(uidTarjeta);
        if (tarjeta == null){
            return new ResultadoEvaluacion(false, "Tarjeta No Registrada en Sistema", "CRITICA");
        }
        //regla 2: tarjeta activa
        if (!tarjeta.isEstado()){
            return new ResultadoEvaluacion(false, "Tarjeta Revocada o Inactiva", "CRITICA");
        }
        //la gravedad depende de la restriccion del area
        int idArea = (cerradura != null && cerradura.getArea() != null) ? cerradura.getArea().getIdArea() : 0;
        int nivelRestriccion = (cerradura != null && cerradura.getArea() != null) ? cerradura.getArea().getNivelRestriccion() : 1;
        String gravedad = (nivelRestriccion >= 3) ? "CRITICA" : "MEDIA";
        
        //regla 3: permiso vigente sobre el area de la cerradura
        if (!permisoDAO.validarPermisoVigente(tarjeta.getIdTarjeta(), idArea, fechaHora)){
                        return new ResultadoEvaluacion(false, "Acceso Fuera de Horario o Sin Permiso Vigente", gravedad);
        }
        //regla 4: turno del empleado o estadia del huesped
        Persona persona = personaDAO.buscarPorUID(uidTarjeta);
        if (persona != null && !persona.estaHabilitadoEn(fechaHora)){
            return new ResultadoEvaluacion(false, persona.getMotivoNoHabilitado(), gravedad);
        }
        //acceso autorizado
        return new ResultadoEvaluacion(true, "OK", "NORMAL");
    }
    
    //clase para mantener el resultado de la evaluacion
    public static class ResultadoEvaluacion{
        private final boolean permitido;
        private final String tipoAnomalia;
        private final String nivelGravedad;
        
        public ResultadoEvaluacion(boolean permitido, String tipoAnomalia, String nivelGravedad){
            this.permitido = permitido;
            this.tipoAnomalia = tipoAnomalia;
            this.nivelGravedad = nivelGravedad;
        }
        public boolean esPermitido(){return permitido;}
        public String getTipoAnomalia(){return tipoAnomalia;}
        public String getNivelGravedad(){return nivelGravedad;}
    }
}
