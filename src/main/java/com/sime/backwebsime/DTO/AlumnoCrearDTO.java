package com.sime.backwebsime.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoCrearDTO {
    private String dni;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String genero;
    
    // Dirección completa
    private String direccion;
    private String departamento;
    private String provincia;
    private String distrito;
    
    // Información médica y contacto
    private String telefonoEmergencia;
    private Boolean tieneDiscapacidad;
    private String diagnosticoMedico;
    
    private Long gradoId; // ID del grado seleccionado
}
