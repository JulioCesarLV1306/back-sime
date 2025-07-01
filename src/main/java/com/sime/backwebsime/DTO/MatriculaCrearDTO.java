package com.sime.backwebsime.DTO;

import lombok.*;




@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaCrearDTO {
    private AlumnoCrearDTO alumno;
    private ApoderadoCrearDTO apoderado;
    private Long gradoId;
}