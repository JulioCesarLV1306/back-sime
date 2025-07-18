package com.sime.backwebsime.DTO;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class DniResponseDTO {
    private boolean success;
    private String message;
    private Data data;
    
    @lombok.Data
    public static class Data {
        @JsonProperty("numero")
        private String numero;
        
        @JsonProperty("nombre_completo")
        private String nombreCompleto;
        
        @JsonProperty("nombres")
        private String nombres;
        
        @JsonProperty("apellido_paterno")
        private String apellidoPaterno;
        
        @JsonProperty("apellido_materno")
        private String apellidoMaterno;
        
        @JsonProperty("codigo_verificacion")
        private String codigoVerificacion;
    }
}
