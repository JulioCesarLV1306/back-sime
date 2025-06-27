package com.sime.backwebsime.service;

import com.sime.backwebsime.DTO.MatriculaCrearDTO;
import com.sime.backwebsime.DTO.MatriculaResponseDTO;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    
    // Métodos para listar matrículas
    public List<MatriculaResponseDTO> getAllMatriculas() {
        List<Matricula> matriculas = matriculaRepository.findAllWithDetails();
        return matriculas.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<MatriculaResponseDTO> getMatriculasByAnio(String anioEscolar) {
        List<Matricula> matriculas = matriculaRepository.findByAnioEscolarWithDetails(anioEscolar);
        return matriculas.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<MatriculaResponseDTO> getMatriculasByEstado(String estado) {
        Matricula.EstadoMatricula estadoEnum = Matricula.EstadoMatricula.valueOf(estado);
        List<Matricula> matriculas = matriculaRepository.findByEstadoWithDetails(estadoEnum);
        return matriculas.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<MatriculaResponseDTO> getMatriculasByGrado(Long gradoId) {
        List<Matricula> matriculas = matriculaRepository.findByGradoIdWithDetails(gradoId);
        return matriculas.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    private MatriculaResponseDTO convertToResponseDTO(Matricula matricula) {
        MatriculaResponseDTO dto = new MatriculaResponseDTO();
        
        // Datos de la matrícula
        dto.setIdMatricula(matricula.getId());
        dto.setAnioEscolar(matricula.getAnioEscolar());
        dto.setFechaMatricula(matricula.getFechaMatricula());
        dto.setFechaCreacion(matricula.getFechaCreacion());
        dto.setFechaActualizacion(matricula.getFechaActualizacion());
        dto.setTipoMatricula(matricula.getTipoMatricula().name());
        dto.setEstado(matricula.getEstado().name());
        
        // Datos del alumno
        if (matricula.getAlumno() != null) {
            dto.setAlumno(convertAlumnoToDTO(matricula.getAlumno()));
        }
        
        // Datos del aula y grado
        if (matricula.getAula() != null) {
            dto.setAula(convertAulaToDTO(matricula.getAula()));
        }
        
        // Datos del apoderado principal
        Optional<Alumno_Apoderado> apoderadoPrincipal = alumnoApoderadoRepository
                .findApoderadoPrincipalByAlumnoId(matricula.getAlumno().getId());
        if (apoderadoPrincipal.isPresent()) {
            dto.setApoderadoPrincipal(convertApoderadoToDTO(apoderadoPrincipal.get().getApoderado()));
        }
        
        return dto;
    }
    
    private MatriculaResponseDTO.AlumnoResponseDTO convertAlumnoToDTO(Alumno alumno) {
        return new MatriculaResponseDTO.AlumnoResponseDTO(
                alumno.getId(),
                alumno.getDniAlumno(),
                alumno.getNombreAlumno(),
                alumno.getApellidoAlumno(),
                alumno.getFechaNacimientoAlumno(),
                alumno.getGeneroAlumno() != null ? alumno.getGeneroAlumno().name() : null,
                alumno.getDireccionAlumno(),
                alumno.getDepartamentoAlumno(),
                alumno.getProvinciaAlumno(),
                alumno.getDistritoAlumno(),
                alumno.getTelefonoEmergencia(),
                alumno.getTieneDiscapacidadAlumno(),
                alumno.getDiagnosticoMedicoAlumno(),
                alumno.getEstadoAlumno()
        );
    }
    
    private MatriculaResponseDTO.AulaResponseDTO convertAulaToDTO(Aula aula) {
        MatriculaResponseDTO.AulaResponseDTO aulaDTO = new MatriculaResponseDTO.AulaResponseDTO();
        aulaDTO.setIdAula(aula.getIdAula());
        aulaDTO.setNombre(aula.getNombre());
        aulaDTO.setCapacidad(aula.getCapacidad());
        
        // Datos del grado
        if (aula.getGrado() != null) {
            aulaDTO.setGrado(new MatriculaResponseDTO.GradoResponseDTO(
                    aula.getGrado().getId(),
                    aula.getGrado().getNombre(),
                    aula.getGrado().getNivel() != null ? aula.getGrado().getNivel().name() : null
            ));
        }
        
        // Datos del docente
        if (aula.getDocente() != null) {
            aulaDTO.setDocente(new MatriculaResponseDTO.DocenteResponseDTO(
                    aula.getDocente().getId(),
                    aula.getDocente().getDni_Docente(),
                    aula.getDocente().getNombre_ocente(),
                    aula.getDocente().getApellidosDocente(),
                    aula.getDocente().getDepartamentoDocente(),
                    aula.getDocente().getProvinciaDocente(),
                    aula.getDocente().getDistritoDocente(),
                    aula.getDocente().getDireccionDocente(),
                    aula.getDocente().isEstadoDocente()
            ));
        }
        
        return aulaDTO;
    }
    
    private MatriculaResponseDTO.ApoderadoResponseDTO convertApoderadoToDTO(Apoderado apoderado) {
        return new MatriculaResponseDTO.ApoderadoResponseDTO(
                apoderado.getId(),
                apoderado.getDni(),
                apoderado.getNombre(),
                apoderado.getApellido(),
                apoderado.getParentesco() != null ? apoderado.getParentesco().getDisplayName() : null,
                apoderado.getTelefono(),
                apoderado.getEmail(),
                apoderado.getDireccion(),
                apoderado.getDepartamento(),
                apoderado.getProvincia(),
                apoderado.getDistrito(),
                apoderado.getLugarTrabajo(),
                apoderado.getCargo()
        );
    }
}