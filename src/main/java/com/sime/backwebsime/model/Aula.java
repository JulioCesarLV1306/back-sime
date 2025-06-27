package com.sime.backwebsime.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Data
@Table(name = "AULAS")
@NoArgsConstructor
@AllArgsConstructor
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AULA")
    private Long idAula;

    @Column(name = "NOMBRE")
    private String nombre;


    @ManyToOne
    @JoinColumn(name = "ID_GRADO", referencedColumnName = "ID_GRADO")
    private Grado grado;

    @ManyToOne
    @JoinColumn(name = "ID_DOCENTE", referencedColumnName = "ID_DOCENTE")
    private Docente docente;


    @Column(name = "CAPACIDAD")
    private Integer capacidad;

    @Column(name = "HORARIO_INICIO")
    private LocalTime horaInicio;

    @Column(name = "HORARIO_FIN")
    private LocalTime horaFin;
}