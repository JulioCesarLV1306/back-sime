package com.sime.backwebsime.controller;

import com.sime.backwebsime.DTO.VacanteDisponibleDTO;
import com.sime.backwebsime.service.VacanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vacantes")
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