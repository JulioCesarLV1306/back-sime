package com.sime.backwebsime.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "DOCENTES")
@NoArgsConstructor
@AllArgsConstructor
public class Docente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DOCENTE")
    private Long id;

    @Column(name = "DNI", length = 8)
    private String dni_Docente;

    @Column(name = "NOMBRE", length = 50)
    private String nombre_ocente;

    @Column(name = "APELLIDO", length = 50)
    private String apellidosDocente;

    @Column(name = "DIRECCION", length = 255)
    private String direccionDocente;

    @Column(name = "DEPARTAMENTO", length = 100)
    private String departamentoDocente;

    @Column(name = "PROVINCIA", length = 100)
    private String provinciaDocente;

    @Column(name = "DISTRITO", length = 100)
    private String distritoDocente;

    @Column(name = "ESTADO")
    private boolean estadoDocente;
}
