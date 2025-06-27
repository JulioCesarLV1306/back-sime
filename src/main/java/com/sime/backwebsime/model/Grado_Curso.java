package com.sime.backwebsime.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "GRADO_CURSOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grado_Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GRADO_CURSO", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_GRADO", referencedColumnName = "ID_GRADO", nullable = false)
    private Grado grado;

    @ManyToOne
    @JoinColumn(name = "ID_CURSO", referencedColumnName = "ID_CURSO", nullable = false)
    private Curso curso;

    @Column(name = "HORAS_SEMANALES")
    private Integer horasSemanales;
}