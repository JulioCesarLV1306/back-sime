package com.sime.backwebsime.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocenteDTO {
    private Long idDocente;
    private String nombre;
    private String apellido;
    private String dni;
}