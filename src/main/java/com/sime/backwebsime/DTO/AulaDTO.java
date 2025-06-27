package com.sime.backwebsime.DTO;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AulaDTO {
    private Long idAula;
    private String nombre;
    private Integer capacidad;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private String docenteNombre;
}