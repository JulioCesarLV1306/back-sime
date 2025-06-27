package com.sime.backwebsime.service;

import com.sime.backwebsime.DTO.GradoDTO;
import com.sime.backwebsime.DTO.VacanteDisponibleDTO;
import com.sime.backwebsime.model.Aula;
import com.sime.backwebsime.model.Grado;
import com.sime.backwebsime.repository.AulaRepository;
import com.sime.backwebsime.repository.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VacanteService {

    @Autowired
    private AulaRepository aulaRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    public List<VacanteDisponibleDTO> getAllVacantes() {
        List<Aula> aulas = aulaRepository.findAllWithGrado();

        Map<Grado, List<Aula>> aulasPorGrado = aulas.stream()
                .collect(Collectors.groupingBy(Aula::getGrado));

        return aulasPorGrado.entrySet().stream()
                .map(entry -> calcularVacantesPorGrado(entry.getKey(), entry.getValue()))
                .toList();
    }

    public VacanteDisponibleDTO getVacantesByGradoId(Integer idGrado) {
        List<Aula> aulas = aulaRepository.findByGradoId(idGrado);

        if (aulas.isEmpty()) {
            return new VacanteDisponibleDTO(
                    false,
                    0,
                    0,
                    null,
                    "No se encontraron aulas para este grado."
            );
        }

        // Reutilizamos el método común
        Grado grado = aulas.getFirst().getGrado();
        return calcularVacantesPorGrado(grado, aulas);
    }

    private VacanteDisponibleDTO calcularVacantesPorGrado(Grado grado, List<Aula> aulas) {
        int cuposTotales = aulas.stream()
                .map(Aula::getCapacidad)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();

        int cuposOcupados = aulas.stream()
                .flatMap(aula -> matriculaRepository.findByAula_IdAula(aula.getIdAula()).stream())
                .mapToInt(m -> 1)
                .sum();

        boolean hayVacante = cuposTotales > cuposOcupados;

        String mensaje = aulas.isEmpty()
                ? "No se encontraron aulas para este grado."
                : (hayVacante ? "Vacantes disponibles" : "No hay vacantes disponibles para este grado.");

        return new VacanteDisponibleDTO(
                hayVacante,
                cuposTotales,
                cuposTotales - cuposOcupados,
                new GradoDTO(grado.getId(), grado.getNombre(), grado.getNivel().toString()),
                mensaje
        );
    }

    public boolean tieneVacantesDisponibles(Integer idGrado) {
        List<Aula> aulas = aulaRepository.findByGradoId(idGrado);

        if (aulas.isEmpty()) {
            return false;
        }

        int cuposTotales = aulas.stream()
                .map(Aula::getCapacidad)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();

        int cuposOcupados = aulas.stream()
                .flatMap(aula -> matriculaRepository.findByAula_IdAula(aula.getIdAula()).stream())
                .mapToInt(m -> 1)
                .sum();

        return cuposTotales > cuposOcupados;
    }
}