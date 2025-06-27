package com.sime.backwebsime.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "ALUMNOS")
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALUMNO", nullable = false)
    @Id
    private Integer id;

    @Column(name = "DNI", length = 8, nullable = false, unique = true)
    private String dniAlumno;

    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String nombreAlumno;

    @Column(name = "APELLIDO", length = 50, nullable = false)
    private String apellidoAlumno;

    @Column(name = "FECHA_NACIMIENTO")
    private LocalDate fechaNacimientoAlumno;

    @Column(name = "GENERO", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private Genero generoAlumno;

    @Column(name = "DIRECCION", length = 255)
    private String direccionAlumno;

    @Column(name = "DEPARTAMENTO", length = 100)
    private String departamentoAlumno;

    @Column(name = "PROVINCIA", length = 100)
    private String provinciaAlumno;

    @Column(name = "DISTRITO", length = 100)
    private String distritoAlumno;

    @Column(name = "TIENE_DISCAPACIDAD")
    private Boolean tieneDiscapacidadAlumno;

    @Column(name = "DIAGNOSTICO_MEDICO", length = 500)
    private String diagnosticoMedicoAlumno;

    @Column(name = "Telefono_EMERGENCIA", length = 500)
    private String telefonoEmergencia;

    @Column(name = "ESTADO")
    private Boolean estadoAlumno;

    public enum Genero {
        Masculino, Femenino, Otro
    }
}
