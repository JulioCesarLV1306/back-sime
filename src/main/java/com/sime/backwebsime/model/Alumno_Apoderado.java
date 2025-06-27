package com.sime.backwebsime.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ALUMNO_APODERADO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AlumnoApoderadoId.class)
public class Alumno_Apoderado {

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_ALUMNO", referencedColumnName = "ID_ALUMNO")
    private Alumno alumno;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_APODERADO", referencedColumnName = "ID_APODERADO")
    private Apoderado apoderado;

    @Column(name = "ES_PRINCIPAL")
    private Boolean esPrincipal;
}
