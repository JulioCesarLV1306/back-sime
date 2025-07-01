package com.sime.backwebsime.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApoderadoCrearDTO {
    private String dni;
    private String nombres;
    private String apellidos;
    private String parentesco;
    private String telefono;
    private String email;
    
    // Dirección completa
    private String direccion;
    private String departamento;
    private String provincia;
    private String distrito;
    
    // Información laboral
    private String lugarTrabajo;
    private String cargo;
}