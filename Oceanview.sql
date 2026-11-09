DROP DATABASE IF EXISTS OceanviewBD;
CREATE DATABASE OceanviewBD;
USE OceanviewBD;

CREATE TABLE Persona (
    idPersona INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(15) NOT NULL UNIQUE,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE Empleado (
    idPersona INT PRIMARY KEY,
    legajo VARCHAR(20) NOT NULL UNIQUE,
    turno VARCHAR(30) NOT NULL,
    cargo VARCHAR(50) NOT NULL,
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);

CREATE TABLE Huesped (
    idPersona INT PRIMARY KEY,
    numeroReserva VARCHAR(30) NOT NULL,
    fechaCheckIn DATETIME NOT NULL,
    fechaCheckOut DATETIME NOT NULL,
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);

CREATE TABLE Usuario (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    idPersona INT NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    pass VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);

CREATE TABLE Tarjeta (
    idTarjeta INT AUTO_INCREMENT PRIMARY KEY,
    codigoUID VARCHAR(30) NOT NULL UNIQUE,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    fechaEmision DATE NOT NULL,
    idPersona INT NULL,
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE SET NULL
);

CREATE TABLE Area (
    idArea INT AUTO_INCREMENT PRIMARY KEY,
    nombreArea VARCHAR(50) NOT NULL,
    nivelRestriccion INT NOT NULL
);

CREATE TABLE Cerradura (
    idCerradura INT AUTO_INCREMENT PRIMARY KEY,
    codigoLector VARCHAR(30) NOT NULL UNIQUE,
    ubicacion VARCHAR(100) NOT NULL,
    idArea INT NOT NULL,
    FOREIGN KEY (idArea) REFERENCES Area(idArea) ON DELETE CASCADE
);

CREATE TABLE Permiso (
    idPermiso INT AUTO_INCREMENT PRIMARY KEY,
    fechaInicio DATETIME NOT NULL,
    fechaFin DATETIME NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    idPersona INT NOT NULL,
    idArea INT NOT NULL,
    idTarjeta INT NULL,
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE,
    FOREIGN KEY (idArea) REFERENCES Area(idArea) ON DELETE CASCADE,
    FOREIGN KEY (idTarjeta) REFERENCES Tarjeta(idTarjeta) ON DELETE SET NULL
);

CREATE TABLE ArchivoLog (
    idArchivoLog INT AUTO_INCREMENT PRIMARY KEY,
    nombreArchivo VARCHAR(100) NOT NULL,
    fechaImportacion DATETIME NOT NULL,
    cantidadRegistros INT NOT NULL
);

CREATE TABLE Evento (
    idEvento INT AUTO_INCREMENT PRIMARY KEY,
    fechaHora DATETIME NOT NULL,
    codigoLeido VARCHAR(30) NOT NULL,
    resultado BOOLEAN NOT NULL,
    idArchivoLog INT NOT NULL,
    idCerradura INT NOT NULL,
    FOREIGN KEY (idArchivoLog) REFERENCES ArchivoLog(idArchivoLog) ON DELETE CASCADE,
    FOREIGN KEY (idCerradura) REFERENCES Cerradura(idCerradura) ON DELETE CASCADE
);

CREATE TABLE Alerta (
    idAlerta INT AUTO_INCREMENT PRIMARY KEY,
    tipoAnomalia VARCHAR(100) NOT NULL,
    fechaHoraGeneracion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    nivelGravedad VARCHAR(20) NOT NULL,
    idEvento INT NOT NULL UNIQUE,
    FOREIGN KEY (idEvento) REFERENCES Evento(idEvento) ON DELETE CASCADE
);

-- INSERTS
INSERT INTO Area (nombreArea, nivelRestriccion) VALUES
('Habitación 302', 1), ('Gimnasio', 2), ('Spa', 2),
('Terraza VIP', 3), ('Habitación 105', 1), ('Oficina Administración', 4);

INSERT INTO Cerradura (codigoLector, ubicacion, idArea) VALUES
('LEC_HAB302', 'Puerta Habitación 302', 1),
('LEC_GIM', 'Acceso Principal Gimnasio', 2),
('LEC_SPA', 'Entrada Spa', 3),
('LEC_TVIP', 'Ascensor Terraza VIP', 4),
('LEC_HAB105', 'Puerta Habitación 105', 5),
('LEC_ADM', 'Puerta Administración', 6);

INSERT INTO Persona (dni, nombre, apellido, estado) VALUES
('32456789', 'Carlos', 'Gómez', TRUE),
('28123456', 'Ana', 'Gómez', TRUE),
('35987654', 'Juan', 'Gómez', TRUE),
('27111222', 'María', 'Pérez', TRUE),
('30333444', 'Juan', 'Díaz', TRUE);

INSERT INTO Huesped (idPersona, numeroReserva, fechaCheckIn, fechaCheckOut) VALUES
(1, 'RES-302-20260920-1', '2026-09-20 10:00:00', '2026-10-05 12:00:00'),
(3, 'RES-SPA-20260927-1', '2026-09-27 14:00:00', '2026-09-30 12:00:00'),
(5, 'RES-105-20260926-1', '2026-09-26 12:00:00', '2026-10-02 10:00:00');

INSERT INTO Empleado (idPersona, legajo, turno, cargo) VALUES
(2, 'EMP-1042', 'Mañana', 'Instructora Gimnasio'),
(4, 'EMP-1001', 'Rotativo', 'Administradora de Seguridad');

INSERT INTO Usuario (idPersona, username, pass, rol) VALUES
(4, 'mperez', 'admin2026', 'ADMINISTRADOR'),
(2, 'agomez', 'opera2026', 'OPERADOR');

INSERT INTO Tarjeta (codigoUID, estado, fechaEmision, idPersona) VALUES
('A4:B2:C9:10', TRUE, '2026-09-20', 1),
('4B:8F:12:DE', TRUE, '2026-09-20', 2),
('04:A1:B2:C3', TRUE, '2026-09-20', 3),
('D1:E2:F3:44', TRUE, '2026-09-20', 4),
('B7:C8:D9:E0', FALSE, '2026-09-20', 5);

INSERT INTO Permiso (fechaInicio, fechaFin, estado, idPersona, idArea, idTarjeta) VALUES
('2026-09-20 00:00:00', '2026-10-05 23:59:59', TRUE, 1, 1, 1),
('2026-01-01 00:00:00', '2026-12-31 23:59:59', TRUE, 4, 6, 4),
('2026-01-01 00:00:00', '2026-12-31 23:59:59', TRUE, 2, 2, 2);

INSERT INTO ArchivoLog (nombreArchivo, fechaImportacion, cantidadRegistros) VALUES
('Eventos_20260920.log', '2026-09-20 20:00:00', 2);

INSERT INTO Evento (fechaHora, codigoLeido, resultado, idArchivoLog, idCerradura) VALUES
('2026-09-20 08:30:00', 'D1:E2:F3:44', TRUE, 1, 6),
('2026-09-20 15:10:00', '99:99:99:99', FALSE, 1, 1);

INSERT INTO Alerta (tipoAnomalia, fechaHoraGeneracion, nivelGravedad, idEvento) VALUES
('Tarjeta No Registrada en Sistema', '2026-09-20 20:05:00', 'CRITICA', 2);


-- CONSULTAS DE PRUEBA
/*
-- Consulta 1

SELECT
p.idPersona AS ID,
CONCAT(p.nombre,' ', p.apellido) AS Nombre_Apellido,
p.dni AS DNI,
p.estado AS Estado,
e.legajo AS Legajo_Empleado,
h.numeroReserva AS Reserva_Huesped,
u.username AS Usuario,
u.rol AS Rol
FROM Persona p
LEFT JOIN Empleado e ON p.idPersona = e.idPersona
LEFT JOIN Huesped h ON p.idPersona = h.idPersona
LEFT JOIN Usuario u ON p.idPersona = u.idPersona
;


-- Consulta 2

SELECT
a.nombreArea AS Area,
a.nivelRestriccion AS Restriccion,
c.codigoLector AS Lector_Instalado,
t.codigoUID AS UID_Tarjeta,
t.estado AS Estado_Tarjeta,
pm.fechaInicio AS Validez_Desde,
pm.fechaFin AS Validez_Hasta,
pm.estado AS Estado_Permiso
FROM Area a
INNER JOIN Cerradura c ON a.idArea = c.idArea
INNER JOIN Permiso pm ON a.idArea = pm.idArea
INNER JOIN Tarjeta t ON pm.idPersona = t.idPersona
;


-- Consulta 3

SELECT
arl.nombreArchivo AS Archivo_Origen,
e.fechaHora AS Hora_Evento,
e.codigoLeido AS UID_Detectado,
e.resultado AS Resultado_Acceso,
al.nivelGravedad AS Gravedad
FROM ArchivoLog arl
INNER JOIN Evento e ON arl.idArchivoLog = e.idArchivoLog
LEFT JOIN Alerta al ON e.idEvento = al.idEvento
;

*/
