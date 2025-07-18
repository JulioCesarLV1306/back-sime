package com.sime.backwebsime.DTO;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class AlumnoEstadoDTO {
    @NotNull(message = "El ID del alumno es obligatorio")
    private Long idAlumno;
    
    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}
