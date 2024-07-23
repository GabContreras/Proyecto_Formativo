CREATE TABLE Enfermera (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario varchar2(50) ,
    contrasena VARCHAR2(250)
);

insert into Enfermera (usuario,contrasena) values ('elpepe', 'etesech')

select * from enfermera 

CREATE TABLE Habitacion (
    numero_habitacion INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
);

CREATE TABLE Paciente (
    id_paciente INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR2(50),
    apellido VARCHAR2(50),
    edad VARCHAR2(50),
    enfermedad VARCHAR2(100),
    fecha_de_ingreso VARCHAR2(50)
);
insert into Paciente (nombre, apellido, edad, enfermedad, fecha_de_ingreso ) values ('Pepe', 'sech', '12 años','Rinofaringitisqueseyo','12/06/2024' )

select * from Paciente;
CREATE TABLE Habitación_paciente (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    numero_cama INT,
    numero_habitacion INT,
    id_paciente INT,
    FOREIGN KEY (numero_habitacion) REFERENCES Habitacion(numero_habitacion),
    FOREIGN KEY (id_paciente) REFERENCES Paciente(id_paciente)
);


CREATE TABLE Medicamento (
    id_medicamento INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR2(50),
    descripcion VARCHAR2(150)
);

CREATE TABLE Aplicacion_Medicamento (
    id_aplicacion INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_medicamento INT,
    id_paciente INT,
    hora_de_aplicacion VARCHAR2(50),
    FOREIGN KEY (id_medicamento) REFERENCES Medicamento(id_medicamento),
    FOREIGN KEY (id_paciente) REFERENCES Paciente(id_paciente)
);

SELECT 
    p.nombre AS Nombre,
    p.apellido AS Apellido,
    m.nombre AS Medicamento,
    am.hora_de_aplicacion AS "Hora de aplicación"
FROM 
    Aplicacion_Medicamento am
INNER JOIN 
    Paciente p ON am.id_paciente = p.id_paciente
INNER JOIN 
    Medicamento m ON am.id_medicamento = m.id_medicamento;