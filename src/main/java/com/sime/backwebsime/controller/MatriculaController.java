package com.sime.backwebsime.controller;

import com.sime.backwebsime.DTO.MatriculaCrearDTO;
import com.sime.backwebsime.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matricula")
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
