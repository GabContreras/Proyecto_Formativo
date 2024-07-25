CREATE TABLE Enfermera (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario varchar2(50) ,
    contrasena VARCHAR2(250)
);

CREATE TABLE Habitacion (
    numero_habitacion INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
);  

insert into Habitacion(numero_habitacion) values (default);
insert into Habitacion(numero_habitacion) values (default);
insert into Habitacion(numero_habitacion) values (default);
insert into Habitacion(numero_habitacion) values (default);
insert into Habitacion(numero_habitacion) values (default);
insert into Habitacion(numero_habitacion) values (default);
insert into Habitacion(numero_habitacion) values (default);
insert into Habitacion(numero_habitacion) values (default);
insert into Habitacion(numero_habitacion) values (default);
insert into Habitacion(numero_habitacion) values (default);

CREATE TABLE Paciente (
    id_paciente INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR2(50),
    apellido VARCHAR2(50),
    edad VARCHAR2(50),
    enfermedad VARCHAR2(100),
    fecha_de_ingreso VARCHAR2(50)
);

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
-- Inserts para tabla medicamento
insert into Medicamento(nombre, descripcion) values ('Paracetamol', 'Analgésico y antipirético, utilizado para reducir la fiebre y aliviar el dolor.');
insert into Medicamento(nombre, descripcion) values ('Ibuprofeno', 'Antiinflamatorio no esteroideo (AINE) utilizado para reducir la inflamación y aliviar el dolor.');
insert into Medicamento(nombre, descripcion) values ('Amoxicilina', 'Antibiótico utilizado para tratar una amplia variedad de infecciones bacterianas.');
insert into Medicamento(nombre, descripcion) values ('Omeprazol', 'Inhibidor de la bomba de protones, utilizado para tratar el reflujo ácido y las úlceras gástricas.');
insert into Medicamento(nombre, descripcion) values ('Loratadina', 'Antihistamínico utilizado para aliviar los síntomas de las alergias.');
insert into Medicamento(nombre, descripcion) values ('Metformina', 'Medicamento utilizado para el tratamiento de la diabetes tipo 2.');
insert into Medicamento(nombre, descripcion) values ('Atenolol', 'Beta bloqueador utilizado para tratar la hipertensión y la angina.');
insert into Medicamento(nombre, descripcion) values ('Simvastatina', 'Estatina utilizada para reducir los niveles de colesterol y triglicéridos en la sangre.');
insert into Medicamento(nombre, descripcion) values ('Clonazepam', 'Benzodiazepina utilizada para tratar los trastornos de ansiedad y convulsiones.');
insert into Medicamento(nombre, descripcion) values ('Ciprofloxacino', 'Antibiótico de amplio espectro utilizado para tratar diversas infecciones bacterianas.');


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
