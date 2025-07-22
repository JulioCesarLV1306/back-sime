package com.sime.backwebsime.controller;

import com.sime.backwebsime.DTO.AlumnoEstadoDTO;
import com.sime.backwebsime.DTO.AlumnoListarDTO;
import com.sime.backwebsime.service.AlumnoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    /**
     * Lista todos los alumnos
     */
    @GetMapping("/listar")
    public ResponseEntity<Map<String, Object>> listarTodosLosAlumnos() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<AlumnoListarDTO> alumnos = alumnoService.listarTodosLosAlumnos();

            response.put("success", true);
            response.put("message", "Lista de alumnos obtenida exitosamente");
            response.put("status", "SUCCESS");
            response.put("code", "ALUMNOS_001");
            response.put("type", "SUCCESS");
            response.put("data", alumnos);
            response.put("totalAlumnos", alumnos.size());
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener la lista de alumnos");
            response.put("status", "ERROR");
            response.put("code", "ALUMNOS_ERROR_001");
            response.put("type", "INTERNAL_SERVER_ERROR");
            response.put("data", null);
            response.put("originalError", e.getMessage());
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Lista solo alumnos activos
     */
    @GetMapping("/listar/activos")
    public ResponseEntity<Map<String, Object>> listarAlumnosActivos() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<AlumnoListarDTO> alumnos = alumnoService.listarAlumnosActivos();

            response.put("success", true);
            response.put("message", "Lista de alumnos activos obtenida exitosamente");
            response.put("status", "SUCCESS");
            response.put("code", "ALUMNOS_002");
            response.put("type", "SUCCESS");
            response.put("data", alumnos);
            response.put("totalAlumnos", alumnos.size());
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener la lista de alumnos activos");
            response.put("status", "ERROR");
            response.put("code", "ALUMNOS_ERROR_002");
            response.put("type", "INTERNAL_SERVER_ERROR");
            response.put("data", null);
            response.put("originalError", e.getMessage());
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca un alumno por DNI
     */
    @GetMapping("/buscar/{dni}")
    public ResponseEntity<Map<String, Object>> buscarAlumnoPorDni(@PathVariable String dni) {
        Map<String, Object> response = new HashMap<>();

        // Validar formato del DNI
        if (dni == null || !dni.matches("\\d{8}")) {
            response.put("success", false);
            response.put("message", "El DNI debe tener exactamente 8 dígitos");
            response.put("status", "ERROR");
            response.put("code", "VALIDATION_001");
            response.put("type", "VALIDATION_ERROR");
            response.put("data", null);
            response.put("originalError", "Formato inválido: " + dni);
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.badRequest().body(response);
        }

        try {
            AlumnoListarDTO alumno = alumnoService.buscarAlumnoPorDni(dni);

            response.put("success", true);
            response.put("message", "Alumno encontrado exitosamente");
            response.put("status", "SUCCESS");
            response.put("code", "ALUMNOS_003");
            response.put("type", "SUCCESS");
            response.put("data", alumno);
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Error de negocio (alumno no encontrado)
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("status", "ERROR");
            response.put("code", "ALUMNOS_ERROR_003");
            response.put("type", "NOT_FOUND");
            response.put("data", null);
            response.put("originalError", e.getMessage());
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            // Error interno del servidor
            response.put("success", false);
            response.put("message", "Error interno al buscar el alumno");
            response.put("status", "ERROR");
            response.put("code", "ALUMNOS_ERROR_004");
            response.put("type", "INTERNAL_SERVER_ERROR");
            response.put("data", null);
            response.put("originalError", e.getMessage());
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Actualiza el estado de un alumno (activo/inactivo) por ID
     */
    @PutMapping("/estado")
    public ResponseEntity<Map<String, Object>> actualizarEstadoAlumno(@Valid @RequestBody AlumnoEstadoDTO request) {
        Map<String, Object> response = new HashMap<>();

        try {
            AlumnoListarDTO alumnoActualizado = alumnoService.actualizarEstadoAlumno(request);

            String estadoTexto = request.getEstado() ? "activo" : "inactivo";

            response.put("success", true);
            response.put("message", "Estado del alumno actualizado a " + estadoTexto + " exitosamente");
            response.put("status", "SUCCESS");
            response.put("code", "ALUMNOS_004");
            response.put("type", "SUCCESS");
            response.put("data", alumnoActualizado);
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Error de negocio (alumno no encontrado)
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("status", "ERROR");
            response.put("code", "ALUMNOS_ERROR_005");
            response.put("type", "NOT_FOUND");
            response.put("data", null);
            response.put("originalError", e.getMessage());
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            // Error interno del servidor
            response.put("success", false);
            response.put("message", "Error interno al actualizar el estado del alumno");
            response.put("status", "ERROR");
            response.put("code", "ALUMNOS_ERROR_006");
            response.put("type", "INTERNAL_SERVER_ERROR");
            response.put("data", null);
            response.put("originalError", e.getMessage());
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Actualiza el estado de un alumno (activo/inactivo) por DNI
     */
    @PutMapping("/estado/{dni}")
    public ResponseEntity<Map<String, Object>> actualizarEstadoAlumnoPorDni(
            @PathVariable String dni,
            @RequestParam Boolean estado) {

        Map<String, Object> response = new HashMap<>();

        // Validar formato del DNI
        if (dni == null || !dni.matches("\\d{8}")) {
            response.put("success", false);
            response.put("message", "El DNI debe tener exactamente 8 dígitos");
            response.put("status", "ERROR");
            response.put("code", "VALIDATION_002");
            response.put("type", "VALIDATION_ERROR");
            response.put("data", null);
            response.put("originalError", "Formato inválido: " + dni);
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.badRequest().body(response);
        }

        // Validar parámetro estado
        if (estado == null) {
            response.put("success", false);
            response.put("message", "El parámetro 'estado' es obligatorio");
            response.put("status", "ERROR");
            response.put("code", "VALIDATION_003");
            response.put("type", "VALIDATION_ERROR");
            response.put("data", null);
            response.put("originalError", "Parámetro estado no proporcionado");
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.badRequest().body(response);
        }

        try {
            AlumnoListarDTO alumnoActualizado = alumnoService.actualizarEstadoAlumnoPorDni(dni, estado);

            String estadoTexto = estado ? "activo" : "inactivo";

            response.put("success", true);
            response.put("message",
                    "Estado del alumno con DNI " + dni + " actualizado a " + estadoTexto + " exitosamente");
            response.put("status", "SUCCESS");
            response.put("code", "ALUMNOS_005");
            response.put("type", "SUCCESS");
            response.put("data", alumnoActualizado);
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Error de negocio (alumno no encontrado)
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("status", "ERROR");
            response.put("code", "ALUMNOS_ERROR_007");
            response.put("type", "NOT_FOUND");
            response.put("data", null);
            response.put("originalError", e.getMessage());
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            // Error interno del servidor
            response.put("success", false);
            response.put("message", "Error interno al actualizar el estado del alumno");
            response.put("status", "ERROR");
            response.put("code", "ALUMNOS_ERROR_008");
            response.put("type", "INTERNAL_SERVER_ERROR");
            response.put("data", null);
            response.put("originalError", e.getMessage());
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
