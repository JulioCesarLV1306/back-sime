package com.sime.backwebsime.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "MATRICULAS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MATRICULA", nullable = false)
    private Integer id;

    @Column(name = "ANIO_ESCOLAR", length = 10, nullable = true)
    private String anioEscolar;

    @Column(name = "FECHA_MATRICULA", nullable = false)
    private LocalDate fechaMatricula;

    @Column(name = "TIPO_MATRICULA", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoMatricula tipoMatricula; // ENUM: regular / extraordinaria

    @Column(name = "ESTADO", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoMatricula estado; // ENUM: pendiente / aprobado / rechazado

    @OneToOne
    @JoinColumn(name = "ID_ALUMNO", referencedColumnName = "ID_ALUMNO")
    private Alumno alumno;

    @OneToOne
    @JoinColumn(name = "ID_AULA", referencedColumnName = "ID_AULA")
    private Aula aula;

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
