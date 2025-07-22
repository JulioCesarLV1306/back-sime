package com.sime.backwebsime.controller;

import com.sime.backwebsime.DTO.VacanteDisponibleDTO;
import com.sime.backwebsime.service.VacanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacantes")
@CrossOrigin(origins = {
    "http://localhost:4200",
    "https://*.netlify.app",
    "https://netlify.app",
    "https://tu-app-sime.onrender.com"
}, 
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
             allowedHeaders = {"Content-Type", "Authorization", "X-Requested-With", "Accept", "Origin"},
             allowCredentials = "true")
public class VacanteController {

    @Autowired
    private VacanteService vacanteService;

    //Para un grado especifico
    @GetMapping("/{idGrado}")
    public ResponseEntity<VacanteDisponibleDTO> getVacantes(@PathVariable Long idGrado) {
        try {
            VacanteDisponibleDTO vacantes = vacanteService.getVacantesByGradoId(idGrado);
            return ResponseEntity.ok(vacantes);
        } catch (Exception e) {
            VacanteDisponibleDTO errorResponse = new VacanteDisponibleDTO(
                false, 0, 0, null, "Error al consultar vacantes: " + e.getMessage()
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    //lista de todos los grados
    @GetMapping
    public ResponseEntity<List<VacanteDisponibleDTO>> getAllVacantes() {
        List<VacanteDisponibleDTO> vacantes = vacanteService.getAllVacantes();
        return ResponseEntity.ok(vacantes);
    }
}