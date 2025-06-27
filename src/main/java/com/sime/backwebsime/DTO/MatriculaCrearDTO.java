package com.sime.backwebsime.DTO;

import lombok.*;

import java.time.LocalDate;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaCrearDTO {
    private AlumnoCrearDTO alumno;
    private ApoderadoCrearDTO apoderado;
    private Integer gradoId;
}