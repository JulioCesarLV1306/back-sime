package com.sime.backwebsime.controller;

import com.sime.backwebsime.DTO.MatriculaCrearDTO;
import com.sime.backwebsime.DTO.MatriculaResponseDTO;
import com.sime.backwebsime.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matricula")
@CrossOrigin(origins = "http://localhost:4200", 
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
             allowedHeaders = {"Content-Type", "Authorization", "X-Requested-With", "Accept", "Origin"},
             allowCredentials = "true")
public class MatriculaController {
    @Autowired
    private MatriculaService matriculaService;

    @PostMapping("/registrar")
    public ResponseEntity<Map<String, Object>> registrar(@RequestBody MatriculaCrearDTO dto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            matriculaService.registrarAlumnoYApoderado(dto);
            
            // Respuesta de éxito
            response.put("success", true);
            response.put("message", "Alumno y apoderado registrados correctamente");
            response.put("status", "success");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            // Respuesta de error
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("status", "error");
            response.put("errorType", determineErrorType(e.getMessage()));
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    private String determineErrorType(String errorMessage) {
        if (errorMessage.contains("vacantes disponibles")) {
            return "NO_VACANCIES";
        } else if (errorMessage.contains("ya está matriculado")) {
            return "ALREADY_ENROLLED";
        } else if (errorMessage.contains("DNI")) {
            return "INVALID_DNI";
        } else if (errorMessage.contains("obligatorio")) {
            return "REQUIRED_FIELD";
        } else if (errorMessage.contains("inválido")) {
            return "INVALID_DATA";
        } else {
            return "GENERAL_ERROR";
        }
    }
    
    // Endpoints para listar matrículas
    @GetMapping("/listar")
    public ResponseEntity<Map<String, Object>> listarTodasMatriculas() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<MatriculaResponseDTO> matriculas = matriculaService.getAllMatriculas();
            
            response.put("success", true);
            response.put("data", matriculas);
            response.put("total", matriculas.size());
            response.put("message", "Matrículas obtenidas correctamente");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener las matrículas: " + e.getMessage());
            response.put("errorType", "FETCH_ERROR");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/listar/anio/{anioEscolar}")
    public ResponseEntity<Map<String, Object>> listarMatriculasPorAnio(@PathVariable String anioEscolar) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<MatriculaResponseDTO> matriculas = matriculaService.getMatriculasByAnio(anioEscolar);
            
            response.put("success", true);
            response.put("data", matriculas);
            response.put("total", matriculas.size());
            response.put("message", "Matrículas del año " + anioEscolar + " obtenidas correctamente");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener las matrículas del año " + anioEscolar + ": " + e.getMessage());
            response.put("errorType", "FETCH_ERROR");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/listar/estado/{estado}")
    public ResponseEntity<Map<String, Object>> listarMatriculasPorEstado(@PathVariable String estado) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<MatriculaResponseDTO> matriculas = matriculaService.getMatriculasByEstado(estado);
            
            response.put("success", true);
            response.put("data", matriculas);
            response.put("total", matriculas.size());
            response.put("message", "Matrículas con estado " + estado + " obtenidas correctamente");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener las matrículas con estado " + estado + ": " + e.getMessage());
            response.put("errorType", "FETCH_ERROR");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/listar/grado/{gradoId}")
    public ResponseEntity<Map<String, Object>> listarMatriculasPorGrado(@PathVariable Long gradoId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<MatriculaResponseDTO> matriculas = matriculaService.getMatriculasByGrado(gradoId);
            
            response.put("success", true);
            response.put("data", matriculas);
            response.put("total", matriculas.size());
            response.put("message", "Matrículas del grado obtenidas correctamente");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener las matrículas del grado: " + e.getMessage());
            response.put("errorType", "FETCH_ERROR");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
