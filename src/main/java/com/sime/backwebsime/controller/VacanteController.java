package com.sime.backwebsime.controller;

import com.sime.backwebsime.DTO.VacanteDisponibleDTO;
import com.sime.backwebsime.service.VacanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacantes")
@CrossOrigin(origins = "http://localhost:4200", 
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
             allowedHeaders = {"Content-Type", "Authorization", "X-Requested-With", "Accept", "Origin"},
             allowCredentials = "true")
public class VacanteController {

    @Autowired
    private VacanteService vacanteService;

    //Para un grado especifico
    @GetMapping("/{idGrado}")
    public VacanteDisponibleDTO getVacantes(@PathVariable Integer idGrado) {
        return vacanteService.getVacantesByGradoId(idGrado.longValue());
    }

    //lista de todos los grados
    @GetMapping
    public ResponseEntity<List<VacanteDisponibleDTO>> getAllVacantes() {
        List<VacanteDisponibleDTO> vacantes = vacanteService.getAllVacantes();
        return ResponseEntity.ok(vacantes);
    }
}