package com.sime.backwebsime.DTO;

import lombok.Data;
import com.sime.backwebsime.model.Alumno;
import java.time.LocalDate;

@Data
public class AlumnoListarDTO {
    private Long idAlumno;
    private String dni;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String genero;
    private String direccion;
    private String departamento;
    private String provincia;
    private String distrito;
    private String telefono;
    private String telefonoEmergencia;
    private String diagnosticoMedico;
    private Boolean tieneDiscapacidad;
    private Boolean estado;
    
    // Constructor para mapear desde la entidad Alumno
    public AlumnoListarDTO(Alumno alumno) {
        this.idAlumno = alumno.getId();
        this.dni = alumno.getDniAlumno();
        this.nombre = alumno.getNombreAlumno();
        this.apellido = alumno.getApellidoAlumno();
        this.fechaNacimiento = alumno.getFechaNacimientoAlumno();
        this.genero = alumno.getGeneroAlumno() != null ? alumno.getGeneroAlumno().name() : null;
        this.direccion = alumno.getDireccionAlumno();
        this.departamento = alumno.getDepartamentoAlumno();
        this.provincia = alumno.getProvinciaAlumno();
        this.distrito = alumno.getDistritoAlumno();
        this.telefono = null; // No hay campo telefono en la entidad
        this.telefonoEmergencia = alumno.getTelefonoEmergencia();
        this.diagnosticoMedico = alumno.getDiagnosticoMedicoAlumno();
        this.tieneDiscapacidad = alumno.getTieneDiscapacidadAlumno();
        this.estado = alumno.getEstadoAlumno();
    }
    
    // Constructor vacío para Jackson
    public AlumnoListarDTO() {}
}
