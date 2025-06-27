package com.sime.backwebsime.service;

import com.sime.backwebsime.DTO.MatriculaCrearDTO;
import com.sime.backwebsime.model.Alumno;
import com.sime.backwebsime.model.Alumno_Apoderado;
import com.sime.backwebsime.model.Apoderado;
import com.sime.backwebsime.model.Aula;
import com.sime.backwebsime.model.Matricula;
import com.sime.backwebsime.repository.AlumnoApoderadoRepository;
import com.sime.backwebsime.repository.AulaRepository;
import com.sime.backwebsime.repository.MatriculaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class MatriculaService {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private ApoderadoService apoderadoService;

    @Autowired
    private VacanteService vacanteService;

    @Autowired
    private AulaRepository aulaRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlumnoApoderadoRepository alumnoApoderadoRepository;

    @Transactional
    public void registrarAlumnoYApoderado(MatriculaCrearDTO dto) {
        // 1. Verificar vacantes
        if (!vacanteService.tieneVacantesDisponibles(dto.getGradoId())) {
            throw new RuntimeException("No hay vacantes disponibles para el grado seleccionado.");
        }

        // 2. Crear alumno
        Alumno alumno = alumnoService.crearAlumno(dto.getAlumno());

        // 3. Crear apoderado
        Apoderado apoderado = apoderadoService.crearApoderado(dto.getApoderado());

        // 3.1. Crear relación alumno-apoderado (si no existe)
        boolean relacionExiste = alumnoApoderadoRepository.existsByAlumno_IdAndApoderado_Id(alumno.getId(), apoderado.getId());
        if (!relacionExiste) {
            Alumno_Apoderado alumnoApoderado = new Alumno_Apoderado();
            alumnoApoderado.setAlumno(alumno);
            alumnoApoderado.setApoderado(apoderado);
            alumnoApoderado.setEsPrincipal(true); // Asumimos que es el apoderado principal
            alumnoApoderadoRepository.save(alumnoApoderado);
        }

        // 4. Verificar si el alumno ya está matriculado en el año actual
        String anioActual = String.valueOf(java.time.LocalDate.now().getYear());
        Optional<Matricula> matriculaExistente = matriculaRepository.findMatriculaActivaByAlumnoAndAnio(alumno.getId(), anioActual);
        if (matriculaExistente.isPresent()) {
            throw new RuntimeException("El alumno ya está matriculado en el año " + anioActual + " con estado: " + matriculaExistente.get().getEstado());
        }

        // 5. Asociar al aula con vacantes disponibles
        Aula aula = aulaRepository.findByGradoId(dto.getGradoId()).stream()
                .filter(a -> {
                    int capacidad = a.getCapacidad() != null ? a.getCapacidad() : 0;
                    int ocupados = matriculaRepository.countMatriculasActivasByAula(a.getIdAula());
                    return capacidad > ocupados;
                })
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay aulas con vacantes disponibles para el grado " + dto.getGradoId()));

        // 6. Registrar matrícula
        Matricula matricula = new Matricula();
        matricula.setAlumno(alumno);
        matricula.setAula(aula);
        matricula.setAnioEscolar(anioActual);
        matricula.setFechaMatricula(LocalDate.now());
        matricula.setTipoMatricula(Matricula.TipoMatricula.Regular);
        matricula.setEstado(Matricula.EstadoMatricula.activo);

        matriculaRepository.save(matricula);
    }
}