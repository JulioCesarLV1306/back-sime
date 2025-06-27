package com.sime.backwebsime.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoCrearDTO {
    private String dni;
    private String nombres;
    private String apellidos;
    private String genero;
    private String direccion;
    private String telefonoEmergencia;
    private Integer gradoId; // ID del grado seleccionado
}
