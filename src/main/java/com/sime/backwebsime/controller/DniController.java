package com.sime.backwebsime.controller;

import com.sime.backwebsime.DTO.DniRequestDTO;
import com.sime.backwebsime.DTO.DniResponseDTO;
import com.sime.backwebsime.service.DniService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dni")
@CrossOrigin(origins = "http://localhost:4200")
public class DniController {
    
    @Autowired
    private DniService dniService;
    
    @PostMapping("/consultar")
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
    public ResponseEntity<Map<String, Object>> consultarDniGet(@PathVariable String dni) {
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
