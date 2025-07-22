package com.sime.backwebsime.controller;

import com.sime.backwebsime.DTO.DniRequestDTO;
import com.sime.backwebsime.DTO.DniResponseDTO;
import com.sime.backwebsime.service.DniService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dni")
@CrossOrigin(origins = {
    "http://localhost:4200",
    "https://*.netlify.app",
    "https://netlify.app"
    
})
@Tag(name = "DNI", description = "Operaciones para consulta de información de DNI")
public class DniController {
    
    @Autowired
    private DniService dniService;
    
    @PostMapping("/consultar")
    @Operation(
        summary = "Consultar DNI por POST",
        description = "Consulta información de una persona mediante su DNI usando el método POST"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "DNI consultado exitosamente",
                content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "Error en la consulta del DNI",
                content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, Object>> consultarDni(@Valid @RequestBody DniRequestDTO request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            DniResponseDTO dniResponse = dniService.consultarDni(request.getDni());
            
            if (dniResponse.isSuccess()) {
                response.put("success", true);
                response.put("message", "DNI consultado exitosamente");
                response.put("data", dniResponse.getData());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", dniResponse.getMessage());
                response.put("data", null);
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno del servidor: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/consultar/{dni}")
    @Operation(
        summary = "Consultar DNI por GET",
        description = "Consulta información de una persona mediante su DNI usando el método GET"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "DNI consultado exitosamente",
                content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "Formato de DNI inválido o error en la consulta",
                content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, Object>> consultarDniGet(
            @Parameter(description = "DNI de la persona a consultar (8 dígitos)", example = "12345678")
            @PathVariable String dni) {
        Map<String, Object> response = new HashMap<>();
        
        // Validar formato del DNI
        if (dni == null || !dni.matches("\\d{8}")) {
            response.put("success", false);
            response.put("message", "El DNI debe tener exactamente 8 dígitos");
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            DniResponseDTO dniResponse = dniService.consultarDni(dni);
            
            if (dniResponse.isSuccess()) {
                response.put("success", true);
                response.put("message", "DNI consultado exitosamente");
                response.put("data", dniResponse.getData());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", dniResponse.getMessage());
                response.put("data", null);
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno del servidor: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
