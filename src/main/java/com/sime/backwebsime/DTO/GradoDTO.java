package com.sime.backwebsime.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradoDTO {
    private Long idGrado;
    private String nombre;
    private String nivel; // basico, medio, superior
}