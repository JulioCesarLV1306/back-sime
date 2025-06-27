package com.sime.backwebsime.controller;

import com.sime.backwebsime.DTO.MatriculaCrearDTO;
import com.sime.backwebsime.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String registrar(@RequestBody MatriculaCrearDTO dto) {
        try {
            matriculaService.registrarAlumnoYApoderado(dto);
            return "✅ Alumno y apoderado registrados correctamente.";
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }
}
