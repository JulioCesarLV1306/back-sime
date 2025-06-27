package com.sime.backwebsime.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "GRADOS")
@NoArgsConstructor
@AllArgsConstructor
public class Grado {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GRADO", nullable = false)
    @Id
    private Long id;

    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String nombre;

    @Column(name = "nivel")
    @Enumerated(EnumType.STRING)
    private Nivel nivel;

    public enum Nivel {
        BASICO, MEDIO, SUPERIOR
    }
}
