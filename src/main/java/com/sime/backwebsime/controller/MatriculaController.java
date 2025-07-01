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
            
            // ✅ RESPUESTA DE ÉXITO MEJORADA
            response.put("success", true);
            response.put("message", "Matrícula registrada exitosamente");
            response.put("status", "SUCCESS");
            response.put("code", "REGISTRATION_SUCCESS");
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            // ❌ RESPUESTA DE ERROR MEJORADA
            ErrorInfo errorInfo = determineErrorInfo(e.getMessage());
            
            response.put("success", false);
            response.put("message", errorInfo.message);
            response.put("status", "ERROR");
            response.put("code", errorInfo.code);
            response.put("type", errorInfo.type);
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return ResponseEntity.status(errorInfo.httpStatus).body(response);
            
        } catch (Exception e) {
            // ❌ ERROR INTERNO DEL SERVIDOR
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            response.put("status", "ERROR");
            response.put("code", "INTERNAL_SERVER_ERROR");
            response.put("type", "SYSTEM_ERROR");
            response.put("details", e.getMessage());
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // Clase interna para manejar información de errores
    private static class ErrorInfo {
        String code;
        String type;
        String message;
        HttpStatus httpStatus;
        
        ErrorInfo(String code, String type, String message, HttpStatus httpStatus) {
            this.code = code;
            this.type = type;
            this.message = message;
            this.httpStatus = httpStatus;
        }
    }
    
    private ErrorInfo determineErrorInfo(String errorMessage) {
        String lowerMessage = errorMessage.toLowerCase();
        
        // Manejo específico para errores de duplicado
        if (lowerMessage.contains("duplicate entry") || lowerMessage.contains("constraint")) {
            if (lowerMessage.contains("dni") || lowerMessage.contains("alumnos.uk")) {
                return new ErrorInfo(
                    "STUDENT_DNI_ALREADY_EXISTS",
                    "DUPLICATE_ENTRY_ERROR",
                    "Ya existe un alumno registrado con ese número de DNI",
                    HttpStatus.CONFLICT
                );
            }
            if (lowerMessage.contains("apoderados")) {
                return new ErrorInfo(
                    "GUARDIAN_DNI_ALREADY_EXISTS",
                    "DUPLICATE_ENTRY_ERROR",
                    "Ya existe un apoderado registrado con ese número de DNI",
                    HttpStatus.CONFLICT
                );
            }
            return new ErrorInfo(
                "DUPLICATE_ENTRY_ERROR",
                "DATABASE_CONSTRAINT_VIOLATION",
                "Ya existe un registro con esos datos. Verifique la información ingresada",
                HttpStatus.CONFLICT
            );
        }
        
        // Manejo para mensajes de existencia en el sistema
        if (lowerMessage.contains("ya existe en el sistema")) {
            if (lowerMessage.contains("alumno")) {
                return new ErrorInfo(
                    "STUDENT_ALREADY_EXISTS",
                    "DUPLICATE_ENTRY_ERROR",
                    "El alumno con este DNI ya está registrado en el sistema",
                    HttpStatus.CONFLICT
                );
            }
            if (lowerMessage.contains("apoderado")) {
                return new ErrorInfo(
                    "GUARDIAN_ALREADY_EXISTS",
                    "DUPLICATE_ENTRY_ERROR",
                    "El apoderado con este DNI ya está registrado en el sistema",
                    HttpStatus.CONFLICT
                );
            }
        }
        
        if (lowerMessage.contains("vacantes disponibles") || lowerMessage.contains("no hay vacantes")) {
            return new ErrorInfo(
                "NO_VACANCIES_AVAILABLE",
                "BUSINESS_RULE_VIOLATION",
                "No hay vacantes disponibles para el grado seleccionado",
                HttpStatus.CONFLICT
            );
        }
        
        if (lowerMessage.contains("ya está matriculado")) {
            return new ErrorInfo(
                "STUDENT_ALREADY_ENROLLED",
                "BUSINESS_RULE_VIOLATION", 
                "El estudiante ya se encuentra matriculado en el año escolar actual",
                HttpStatus.CONFLICT
            );
        }
        
        if (lowerMessage.contains("dni") && lowerMessage.contains("inválido")) {
            return new ErrorInfo(
                "INVALID_DNI_FORMAT",
                "VALIDATION_ERROR",
                "El formato del DNI proporcionado no es válido",
                HttpStatus.BAD_REQUEST
            );
        }
        
        if (lowerMessage.contains("obligatorio") || lowerMessage.contains("requerido")) {
            return new ErrorInfo(
                "REQUIRED_FIELD_MISSING",
                "VALIDATION_ERROR",
                "Faltan campos obligatorios en la solicitud",
                HttpStatus.BAD_REQUEST
            );
        }
        
        if (lowerMessage.contains("género") && lowerMessage.contains("inválido")) {
            return new ErrorInfo(
                "INVALID_GENDER_VALUE",
                "VALIDATION_ERROR",
                "El valor del género no es válido. Valores permitidos: Masculino, Femenino, Otro",
                HttpStatus.BAD_REQUEST
            );
        }
        
        if (lowerMessage.contains("parentesco") && lowerMessage.contains("inválido")) {
            return new ErrorInfo(
                "INVALID_RELATIONSHIP_VALUE",
                "VALIDATION_ERROR",
                "El valor del parentesco no es válido",
                HttpStatus.BAD_REQUEST
            );
        }
        
        if (lowerMessage.contains("grado") && lowerMessage.contains("no existe")) {
            return new ErrorInfo(
                "GRADE_NOT_FOUND",
                "RESOURCE_NOT_FOUND",
                "El grado especificado no existe",
                HttpStatus.NOT_FOUND
            );
        }
        
        // Errores de base de datos
        if (lowerMessage.contains("could not execute statement") || lowerMessage.contains("sql")) {
            return new ErrorInfo(
                "DATABASE_ERROR",
                "DATABASE_OPERATION_FAILED",
                "Error en la operación de base de datos. Verifique los datos ingresados",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        
        // Error genérico
        return new ErrorInfo(
            "UNKNOWN_ERROR",
            "GENERAL_ERROR",
            "Ha ocurrido un error inesperado: " + errorMessage,
            HttpStatus.BAD_REQUEST
        );
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
