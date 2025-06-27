package com.sime.backwebsime.DTO;

import com.sime.backwebsime.model.Grado;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradoDTO {
    private Integer idGrado;
    private String nombre;
    private String nivel; // basico, medio, superior
}