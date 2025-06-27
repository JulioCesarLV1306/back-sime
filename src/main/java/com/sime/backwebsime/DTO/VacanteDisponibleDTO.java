package com.sime.backwebsime.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VacanteDisponibleDTO {
    private boolean hayVacante;
    private Integer cuposTotales;
    private Integer cuposDisponibles;
    private GradoDTO grado;
    private String mensaje;

}