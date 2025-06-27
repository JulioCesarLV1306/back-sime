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

        // 3.1. Crear relación alumno-apoderado
        Alumno_Apoderado alumnoApoderado = new Alumno_Apoderado();
        alumnoApoderado.setAlumno(alumno);
        alumnoApoderado.setApoderado(apoderado);
        alumnoApoderado.setEsPrincipal(true); // Asumimos que es el apoderado principal
        alumnoApoderadoRepository.save(alumnoApoderado);

        // 4. Asociar al aula (por ejemplo, el primero disponible)
        Aula aula = aulaRepository.findByGradoId(dto.getGradoId()).stream()
                .filter(a -> {
                    int cupo = a.getCapacidad() != null ? a.getCapacidad() : 0;
                    int ocupados = matriculaRepository.findByAula_IdAula(a.getIdAula()).size();
                    return cupo > ocupados;
                })
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay aulas con vacantes disponibles"));

        // 5. Registrar matrícula
        Matricula matricula = new Matricula();
        matricula.setAlumno(alumno);
        matricula.setAula(aula);
        matricula.setFechaMatricula(LocalDate.now());
        matricula.setTipoMatricula(Matricula.TipoMatricula.Regular);
        matricula.setEstado(Matricula.EstadoMatricula.activo);

        matriculaRepository.save(matricula);
    }
}