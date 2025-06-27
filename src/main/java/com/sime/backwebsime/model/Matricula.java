package com.sime.backwebsime.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "MATRICULAS", 
       indexes = {
           @Index(name = "idx_matricula_alumno", columnList = "ID_ALUMNO"),
           @Index(name = "idx_matricula_aula", columnList = "ID_AULA"),
           @Index(name = "idx_matricula_anio", columnList = "ANIO_ESCOLAR"),
           @Index(name = "idx_matricula_estado", columnList = "ESTADO")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MATRICULA", nullable = false)
    private Long id;

    @Column(name = "ANIO_ESCOLAR", length = 10, nullable = false)
    private String anioEscolar;

    @Column(name = "FECHA_MATRICULA", nullable = false)
    private LocalDate fechaMatricula;

    @Column(name = "TIPO_MATRICULA", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoMatricula tipoMatricula; // ENUM: regular / extraordinaria

    @Column(name = "ESTADO", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoMatricula estado; // ENUM: pendiente / aprobado / rechazado

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ALUMNO", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AULA", nullable = false)
    private Aula aula;

    @Column(name = "FECHA_CREACION", nullable = false, updatable = false)
    private LocalDate fechaCreacion;

    @Column(name = "FECHA_ACTUALIZACION")
    private LocalDate fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDate.now();
        fechaActualizacion = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDate.now();
    }

    public enum TipoMatricula {
        Regular,
        NuevoIngreso,
        Transferido
    }

    public enum EstadoMatricula {
        pendiente,
        activo,
        inactivo,
        suspendido
    }


}
