-- Script de migración PostgreSQL para SIME
-- Ejecutar en PostgreSQL solamente

-- Eliminar tablas si existen (en orden inverso por dependencias)
DROP TABLE IF EXISTS alumno_apoderado;
DROP TABLE IF EXISTS matriculas;
DROP TABLE IF EXISTS grado_cursos;
DROP TABLE IF EXISTS aulas;
DROP TABLE IF EXISTS alumnos;
DROP TABLE IF EXISTS apoderados;
DROP TABLE IF EXISTS cursos;
DROP TABLE IF EXISTS docentes;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS grados;

-- Crear tabla grados
CREATE TABLE grados (
    id_grado BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    nivel VARCHAR(20) CHECK (nivel IN ('BASICO', 'MEDIO', 'SUPERIOR'))
);

-- Crear tabla docentes
CREATE TABLE docentes (
    id_docente BIGSERIAL PRIMARY KEY,
    dni VARCHAR(8),
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    direccion VARCHAR(255),
    departamento VARCHAR(100),
    provincia VARCHAR(100),
    distrito VARCHAR(100),
    estado BOOLEAN DEFAULT true
);

-- Crear tabla aulas
CREATE TABLE aulas (
    id_aula BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255),
    capacidad INTEGER,
    id_grado BIGINT,
    id_docente BIGINT,
    horario_inicio TIME,
    horario_fin TIME
);

-- Crear tabla alumnos
CREATE TABLE alumnos (
    id_alumno BIGSERIAL PRIMARY KEY,
    dni VARCHAR(8) NOT NULL UNIQUE,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    fecha_nacimiento DATE,
    genero VARCHAR(20) NOT NULL CHECK (genero IN ('Femenino', 'Masculino', 'Otro')),
    direccion VARCHAR(255),
    departamento VARCHAR(100),
    provincia VARCHAR(100),
    distrito VARCHAR(100),
    telefono_emergencia VARCHAR(500),
    tiene_discapacidad BOOLEAN,
    diagnostico_medico VARCHAR(500),
    estado BOOLEAN DEFAULT true
);

-- Crear tabla apoderados
CREATE TABLE apoderados (
    id_apoderado BIGSERIAL PRIMARY KEY,
    dni VARCHAR(8) NOT NULL UNIQUE,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    telefono VARCHAR(15),
    email VARCHAR(100),
    direccion VARCHAR(255),
    departamento VARCHAR(100),
    provincia VARCHAR(100),
    distrito VARCHAR(100),
    parentesco VARCHAR(20) NOT NULL CHECK (parentesco IN ('ABUELO_A', 'HERMANO_A', 'MADRE', 'OTRO', 'PADRE', 'TIO_A', 'TUTOR_LEGAL')),
    lugar_trabajo VARCHAR(100),
    cargo VARCHAR(100)
);

-- Crear tabla cursos
CREATE TABLE cursos (
    id_curso BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

-- Crear tabla grado_cursos
CREATE TABLE grado_cursos (
    id_grado_curso BIGSERIAL PRIMARY KEY,
    id_grado BIGINT NOT NULL,
    id_curso BIGINT NOT NULL,
    horas_semanales INTEGER
);

-- Crear tabla matriculas
CREATE TABLE matriculas (
    id_matricula BIGSERIAL PRIMARY KEY,
    id_alumno BIGINT NOT NULL,
    id_aula BIGINT NOT NULL,
    fecha_matricula DATE NOT NULL,
    fecha_creacion DATE NOT NULL,
    fecha_actualizacion DATE,
    anio_escolar VARCHAR(10) NOT NULL,
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('activo', 'inactivo', 'pendiente', 'suspendido')),
    tipo_matricula VARCHAR(20) NOT NULL CHECK (tipo_matricula IN ('NuevoIngreso', 'Regular', 'Transferido'))
);

-- Crear tabla alumno_apoderado
CREATE TABLE alumno_apoderado (
    id_alumno BIGINT NOT NULL,
    id_apoderado BIGINT NOT NULL,
    es_principal BOOLEAN DEFAULT false,
    PRIMARY KEY (id_alumno, id_apoderado)
);

-- Crear tabla usuarios
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    role VARCHAR(50),
    country VARCHAR(255)
);

-- Agregar foreign keys
ALTER TABLE aulas ADD CONSTRAINT fk_aulas_grado FOREIGN KEY (id_grado) REFERENCES grados(id_grado);
ALTER TABLE aulas ADD CONSTRAINT fk_aulas_docente FOREIGN KEY (id_docente) REFERENCES docentes(id_docente);
ALTER TABLE grado_cursos ADD CONSTRAINT fk_gradocursos_grado FOREIGN KEY (id_grado) REFERENCES grados(id_grado);
ALTER TABLE grado_cursos ADD CONSTRAINT fk_gradocursos_curso FOREIGN KEY (id_curso) REFERENCES cursos(id_curso);
ALTER TABLE matriculas ADD CONSTRAINT fk_matriculas_alumno FOREIGN KEY (id_alumno) REFERENCES alumnos(id_alumno);
ALTER TABLE matriculas ADD CONSTRAINT fk_matriculas_aula FOREIGN KEY (id_aula) REFERENCES aulas(id_aula);
ALTER TABLE alumno_apoderado ADD CONSTRAINT fk_alumnoapoderado_alumno FOREIGN KEY (id_alumno) REFERENCES alumnos(id_alumno);
ALTER TABLE alumno_apoderado ADD CONSTRAINT fk_alumnoapoderado_apoderado FOREIGN KEY (id_apoderado) REFERENCES apoderados(id_apoderado);

-- Insertar datos de grados
INSERT INTO grados (nombre, nivel) VALUES 
('1er Grado', 'BASICO'),
('2do Grado', 'BASICO'),
('3er Grado', 'BASICO'),
('4to Grado', 'MEDIO'),
('5to Grado', 'MEDIO'),
('6to Grado', 'SUPERIOR');

-- Insertar datos de aulas
INSERT INTO aulas (nombre, capacidad, id_grado, id_docente, horario_inicio, horario_fin) VALUES 
('Aula 1er GradoA', 30, 1, NULL, NULL, NULL),
('Aula 1er GradoB', 25, 1, NULL, NULL, NULL),
('Aula 2do GradoA', 30, 2, NULL, NULL, NULL),
('Aula 2do GradoB', 25, 2, NULL, NULL, NULL),
('Aula 3er GradoA', 30, 3, NULL, NULL, NULL),
('Aula 3er GradoB', 25, 3, NULL, NULL, NULL),
('Aula 4to GradoA', 30, 4, NULL, NULL, NULL),
('Aula 4to GradoB', 25, 4, NULL, NULL, NULL),
('Aula 5to GradoA', 30, 5, NULL, NULL, NULL),
('Aula 5to GradoB', 25, 5, NULL, NULL, NULL),
('Aula 6to GradoA', 30, 6, NULL, NULL, NULL),
('Aula 6to GradoB', 25, 6, NULL, NULL, NULL);

-- Insertar usuarios de prueba
INSERT INTO usuarios (username, password, firstname, lastname, role, country) VALUES 
('jdoe', 'pass123', 'John', 'Doe', 'admin', 'USA'),
('mperez', 'secret456', 'María', 'Pérez', 'alumno', 'Peru');

-- Crear índices
CREATE INDEX idx_matricula_alumno ON matriculas(id_alumno);
CREATE INDEX idx_matricula_aula ON matriculas(id_aula);
CREATE INDEX idx_matricula_anio ON matriculas(anio_escolar);
CREATE INDEX idx_matricula_estado ON matriculas(estado);
CREATE INDEX idx_alumnos_dni ON alumnos(dni);
CREATE INDEX idx_apoderados_dni ON apoderados(dni);
