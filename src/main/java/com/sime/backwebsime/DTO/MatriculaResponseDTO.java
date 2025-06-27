package com.sime.backwebsime.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaResponseDTO {
    // Datos de la matrícula
    private Long idMatricula;
    private String anioEscolar;
    private LocalDate fechaMatricula;
    private LocalDate fechaCreacion;
    private LocalDate fechaActualizacion;
    private String tipoMatricula;
    private String estado;
    
    // Datos del alumno
    private AlumnoResponseDTO alumno;
    
    // Datos del aula
    private AulaResponseDTO aula;
    
    // Datos del apoderado principal
    private ApoderadoResponseDTO apoderadoPrincipal;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlumnoResponseDTO {
        private Long idAlumno;
        private String dni;
        private String nombres;
        private String apellidos;
        private LocalDate fechaNacimiento;
        private String genero;
        private String direccion;
        private String departamento;
        private String provincia;
        private String distrito;
        private String telefonoEmergencia;
        private Boolean tieneDiscapacidad;
        private String diagnosticoMedico;
        private Boolean estado;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AulaResponseDTO {
        private Long idAula;
        private String nombre;
        private Integer capacidad;
        private GradoResponseDTO grado;
        private DocenteResponseDTO docente;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GradoResponseDTO {
        private Long idGrado;
        private String nombre;
        private String nivel;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocenteResponseDTO {
        private Long idDocente;
        private String dni;
        private String nombres;
        private String apellidos;
        private String departamento;
        private String provincia;
        private String distrito;
        private String direccion;
        private Boolean estado;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApoderadoResponseDTO {
        private Long idApoderado;
        private String dni;
        private String nombres;
        private String apellidos;
        private String parentesco;
        private String telefono;
        private String email;
        private String direccion;
        private String departamento;
        private String provincia;
        private String distrito;
        private String lugarTrabajo;
        private String cargo;
    }
}
