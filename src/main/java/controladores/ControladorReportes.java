package controladores;

import excepciones.AccesoDatosException;
import modelo.Evento;
import modelo.Persona;
import modelo.RegistroAsistencia;
import persistencia.EventoDAO;
import persistencia.PersonaDAO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//genera el reporte de asistencia del personal y lo guarda en un archivo de texto
public class ControladorReportes {
    private EventoDAO eventoDAO;
    private PersonaDAO personaDAO;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public ControladorReportes(){
        this.eventoDAO = new EventoDAO();
        this.personaDAO = new PersonaDAO();
    }
    
    //por empleado y por dia. huéspedes se excluyen
    //ingreso = primer acceso permitido. egreso = ultimo
    public List<RegistroAsistencia> generarAsistencia(LocalDateTime inicio, LocalDateTime fin, String area, String uidTarjeta) throws AccesoDatosException{
        List<Evento> eventos = eventoDAO.listarPermitidos(inicio, fin, area, uidTarjeta);
        //se guarda la persona de cada UID para no consultarlas varias veces
        Map<String, Persona> personasPorUid = new HashMap<>();
        Map<String, RegistroAsistencia> consolidado = new LinkedHashMap<>();
        for (Evento ev : eventos){
            String uid = ev.getCodigoLeido();
            if (!personasPorUid.containsKey(uid)){
                personasPorUid.put(uid, personaDAO.buscarPorUID(uid)); //puede ser null
            }
            Persona persona = personasPorUid.get(uid);
            //cada tipo de persona indica si controla asistencia
            if (persona == null || !persona.controlaAsistencia()){
                continue;
            }
            LocalDate dia = ev.getFechaHora().toLocalDate();
            //un registro por empleado y día
            String clave = persona.getIdPersona() + "|" + dia;
            RegistroAsistencia registro = consolidado.get(clave);
            if (registro == null){
                consolidado.put(clave, new RegistroAsistencia(persona, dia, ev.getFechaHora()));
            } else{
                registro.registrarAcceso(ev.getFechaHora());
            }
        }
        return new ArrayList<>(consolidado.values());
    }
    
    //guarda el reporte en un archivo de texto
    public void exportarAArchivo(List<RegistroAsistencia> registros, LocalDateTime inicio, LocalDateTime fin, File destino) throws IOException{
        try (BufferedWriter bw = Files.newBufferedWriter(destino.toPath(), StandardCharsets.UTF_8)){
            bw.write("REPORTE DE ASISTENCIA - Oceanview");
            bw.newLine();
            bw.write("Periodo: " + inicio.format(FMT) + " a " + fin.format(FMT));
            bw.newLine();
            bw.write("Persona;Fecha;Ingreso;Egreso;Horas");
            bw.newLine();
            for (RegistroAsistencia r : registros){
                bw.write(String.join(";", r.aFila()));
                bw.newLine();
            }
        }
    }
}
