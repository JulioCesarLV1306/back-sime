package com.sime.backwebsime.service;

import com.sime.backwebsime.DTO.MatriculaCrearDTO;
import com.sime.backwebsime.DTO.MatriculaResponseDTO;
import com.sime.backwebsime.model.Alumno;
import com.sime.backwebsime.model.Alumno_Apoderado;
import com.sime.backwebsime.model.Apoderado;
import com.sime.backwebsime.model.Aula;
import com.sime.backwebsime.model.Matricula;
import com.sime.backwebsime.repository.AlumnoApoderadoRepository;
import com.sime.backwebsime.repository.AlumnoRepository;
import com.sime.backwebsime.repository.ApoderadoRepository;
import com.sime.backwebsime.repository.AulaRepository;
import com.sime.backwebsime.repository.MatriculaRepository;

// Cambiamos de jakarta.transaction a org.springframework para tener más opciones
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

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
    
    @Autowired
    private AlumnoRepository alumnoRepository;
    
    @Autowired
    private ApoderadoRepository apoderadoRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarAlumnoYApoderado(MatriculaCrearDTO dto) {
        System.out.println("🚀 Iniciando registro de matrícula en una nueva transacción...");
        
        try {
            // 1. Verificar vacantes PRIMERO
            if (!vacanteService.tieneVacantesDisponibles(dto.getGradoId())) {
                throw new RuntimeException("No hay vacantes disponibles para el grado seleccionado.");
            }

            String anioActual = String.valueOf(java.time.LocalDate.now().getYear());
            
            // 2. VERIFICACIÓN ÚNICA: Comprobar si el DNI ya tiene matrícula activa ANTES de crear nada
            Optional<Alumno> alumnoExistentePorDni = alumnoRepository.findByDniAlumno(dto.getAlumno().getDni());
            if (alumnoExistentePorDni.isPresent()) {
                System.out.println("🔍 Alumno con DNI " + dto.getAlumno().getDni() + " ya existe, verificando matrícula...");
                
                Optional<Matricula> matriculaExistente = matriculaRepository.findMatriculaActivaByAlumnoAndAnio(
                    alumnoExistentePorDni.get().getId(), anioActual);
                
                if (matriculaExistente.isPresent()) {
                    System.out.println("❌ Alumno ya tiene matrícula activa en " + anioActual);
                    throw new RuntimeException("El alumno ya está matriculado en el año " + anioActual + " con estado: " + matriculaExistente.get().getEstado());
                }
                
                System.out.println("✅ Alumno existe pero no tiene matrícula activa, continuando...");
            }

            // 3. Procesar entidades de forma secuencial y segura
            // 3.1. Crear o reutilizar alumno
            Alumno alumno = alumnoService.crearAlumno(dto.getAlumno());
            
            // VALIDACIÓN CRÍTICA: Verificar que el alumno tiene ID válido
            if (alumno == null || alumno.getId() == null) {
                throw new RuntimeException("Error crítico: El alumno no tiene un ID válido");
            }
            System.out.println("✅ Alumno procesado con ID: " + alumno.getId());

            // 3.2. Crear o reutilizar apoderado
            Apoderado apoderado = apoderadoService.crearApoderado(dto.getApoderado());
            
            // VALIDACIÓN CRÍTICA: Verificar que el apoderado tiene ID válido
            if (apoderado == null || apoderado.getId() == null) {
                throw new RuntimeException("Error crítico: El apoderado no tiene un ID válido");
            }
            System.out.println("✅ Apoderado procesado con ID: " + apoderado.getId());

            // 3.3. Obtener las entidades frescas de la base de datos para asegurar que están en la sesión actual
            final Long alumnoId = alumno.getId();
            final Long apoderadoId = apoderado.getId();
            
            // Recargar entidades para asegurar que están en la sesión actual
            alumno = alumnoRepository.findById(alumnoId)
                    .orElseThrow(() -> new RuntimeException("Error: No se pudo obtener el alumno con ID " + alumnoId));
            apoderado = apoderadoRepository.findById(apoderadoId)
                    .orElseThrow(() -> new RuntimeException("Error: No se pudo obtener el apoderado con ID " + apoderadoId));
            
            // VALIDACIONES FINALES antes de continuar
            if (alumno.getId() == null) {
                throw new RuntimeException("Error crítico: Alumno recargado sin ID válido");
            }
            if (apoderado.getId() == null) {
                throw new RuntimeException("Error crítico: Apoderado recargado sin ID válido");
            }

            // 3.4. Crear relación alumno-apoderado (si no existe)
            boolean relacionExiste = alumnoApoderadoRepository.existsByAlumno_IdAndApoderado_Id(alumnoId, apoderadoId);
            if (!relacionExiste) {
                Alumno_Apoderado alumnoApoderado = new Alumno_Apoderado();
                alumnoApoderado.setAlumno(alumno);
                alumnoApoderado.setApoderado(apoderado);
                alumnoApoderado.setEsPrincipal(true);
                alumnoApoderadoRepository.save(alumnoApoderado);
                System.out.println("✅ Relación alumno-apoderado creada");
            } else {
                System.out.println("✅ Relación alumno-apoderado ya existe");
            }

            // 3.5. Asociar al aula con vacantes disponibles
            Aula aula = aulaRepository.findByGradoId(dto.getGradoId()).stream()
                    .filter(a -> {
                        int capacidad = a.getCapacidad() != null ? a.getCapacidad() : 0;
                        int ocupados = matriculaRepository.countMatriculasActivasByAula(a.getIdAula());
                        return capacidad > ocupados;
                    })
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No hay aulas con vacantes disponibles para el grado " + dto.getGradoId()));

            // 3.6. Registrar matrícula
            Matricula matricula = new Matricula();
            matricula.setAlumno(alumno);
            matricula.setAula(aula);
            matricula.setAnioEscolar(anioActual);
            matricula.setFechaMatricula(LocalDate.now());
            matricula.setTipoMatricula(Matricula.TipoMatricula.Regular);
            matricula.setEstado(Matricula.EstadoMatricula.activo);

            // IMPORTANTE: Guardar y hacer flush inmediatamente
            matriculaRepository.saveAndFlush(matricula);
            
            // Verificar que el proceso se completó correctamente
            System.out.println("✅ Matrícula registrada exitosamente");
            
            System.out.println("🎯 Proceso de matrícula completado exitosamente");
            
            // NO acceder a ninguna propiedad de las entidades después de este punto
            // para evitar lazy loading y problemas de sesión;
            
        } catch (Exception e) {
            System.err.println("❌ Error al procesar matrícula: " + e.getMessage());
            e.printStackTrace();
            
            // Relanzar la excepción para que sea manejada por el controlador
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException("Error al registrar la matrícula: " + e.getMessage(), e);
            }
        }
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
        
        try {
            // Datos de la matrícula
            dto.setIdMatricula(matricula.getId());
            dto.setAnioEscolar(matricula.getAnioEscolar());
            dto.setFechaMatricula(matricula.getFechaMatricula());
            dto.setFechaCreacion(matricula.getFechaCreacion());
            dto.setFechaActualizacion(matricula.getFechaActualizacion());
            dto.setTipoMatricula(matricula.getTipoMatricula().name());
            dto.setEstado(matricula.getEstado().name());
            
            // Datos del alumno (con protección contra lazy loading)
            if (matricula.getAlumno() != null && matricula.getAlumno().getId() != null) {
                dto.setAlumno(convertAlumnoToDTO(matricula.getAlumno()));
            }
            
            // Datos del aula y grado (con protección contra lazy loading)
            if (matricula.getAula() != null && matricula.getAula().getIdAula() != null) {
                dto.setAula(convertAulaToDTO(matricula.getAula()));
            }
            
            // Datos del apoderado principal (solo si el alumno tiene ID válido)
            if (matricula.getAlumno() != null && matricula.getAlumno().getId() != null) {
                Optional<Alumno_Apoderado> apoderadoPrincipal = alumnoApoderadoRepository
                        .findApoderadoPrincipalByAlumnoId(matricula.getAlumno().getId());
                if (apoderadoPrincipal.isPresent()) {
                    dto.setApoderadoPrincipal(convertApoderadoToDTO(apoderadoPrincipal.get().getApoderado()));
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al convertir matrícula a DTO: " + e.getMessage());
            // Continuar con los datos que se pudieron obtener
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