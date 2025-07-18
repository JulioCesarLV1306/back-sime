package com.sime.backwebsime.service;

import com.sime.backwebsime.DTO.AlumnoCrearDTO;
import com.sime.backwebsime.DTO.AlumnoEstadoDTO;
import com.sime.backwebsime.DTO.AlumnoListarDTO;
import com.sime.backwebsime.model.Alumno;
import com.sime.backwebsime.repository.AlumnoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Transactional
    public Alumno crearAlumno(AlumnoCrearDTO dto) {
        System.out.println("🟢 ALUMNO SERVICE: Iniciando creación/búsqueda de alumno");
        
        // Validaciones básicas
        if (dto.getDni() == null || dto.getDni().trim().isEmpty()) {
            throw new RuntimeException("El DNI del alumno es obligatorio");
        }
        if (dto.getNombres() == null || dto.getNombres().trim().isEmpty()) {
            throw new RuntimeException("El nombre del alumno es obligatorio");
        }
        if (dto.getApellidos() == null || dto.getApellidos().trim().isEmpty()) {
            throw new RuntimeException("El apellido del alumno es obligatorio");
        }
        if (dto.getGenero() == null || dto.getGenero().trim().isEmpty()) {
            throw new RuntimeException("El género del alumno es obligatorio");
        }
        
        System.out.println("🔍 ALUMNO SERVICE: Buscando alumno con DNI: " + dto.getDni());
        
        // Verificar si ya existe un alumno con este DNI
        Optional<Alumno> alumnoExistente = alumnoRepository.findByDniAlumno(dto.getDni());
        if (alumnoExistente.isPresent()) {
            Alumno alumno = alumnoExistente.get();
            System.out.println("✅ ALUMNO SERVICE: Encontrado alumno existente con ID: " + alumno.getId());
            return alumno;
        }
        
        System.out.println("➕ ALUMNO SERVICE: Creando nuevo alumno con DNI: " + dto.getDni());
        
        try {
            // Crear nuevo alumno
            Alumno alumno = new Alumno();
            alumno.setDniAlumno(dto.getDni());
            alumno.setNombreAlumno(dto.getNombres());
            alumno.setApellidoAlumno(dto.getApellidos());
            alumno.setFechaNacimientoAlumno(dto.getFechaNacimiento());
            
            try {
                alumno.setGeneroAlumno(Alumno.Genero.valueOf(dto.getGenero()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Género inválido: '" + dto.getGenero() + "'. Valores válidos: Masculino, Femenino, Otro");
            }
            
            // Dirección completa
            alumno.setDireccionAlumno(dto.getDireccion());
            alumno.setDepartamentoAlumno(dto.getDepartamento());
            alumno.setProvinciaAlumno(dto.getProvincia());
            alumno.setDistritoAlumno(dto.getDistrito());
            
            // Información médica y contacto
            alumno.setTelefonoEmergencia(dto.getTelefonoEmergencia());
            alumno.setTieneDiscapacidadAlumno(dto.getTieneDiscapacidad());
            alumno.setDiagnosticoMedicoAlumno(dto.getDiagnosticoMedico());
            
            alumno.setEstadoAlumno(true); // Por defecto activo
            
            System.out.println("💾 ALUMNO SERVICE: Guardando nuevo alumno...");
            
            // Guardar sin flush inicialmente
            Alumno alumnoGuardado = alumnoRepository.save(alumno);
            
            System.out.println("✅ ALUMNO SERVICE: Alumno guardado exitosamente con ID: " + alumnoGuardado.getId());
            return alumnoGuardado;
            
        } catch (Exception e) {
            System.err.println("❌ ALUMNO SERVICE: Error al crear alumno: " + e.getMessage());
            throw new RuntimeException("Error al crear el alumno: " + e.getMessage(), e);
        }
    }
    
    /**
     * Lista todos los alumnos
     */
    public List<AlumnoListarDTO> listarTodosLosAlumnos() {
        System.out.println("🔍 ALUMNO SERVICE: Obteniendo lista de todos los alumnos");
        try {
            List<Alumno> alumnos = alumnoRepository.findAll();
            System.out.println("✅ ALUMNO SERVICE: Se encontraron " + alumnos.size() + " alumnos");
            
            return alumnos.stream()
                    .map(AlumnoListarDTO::new)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            System.err.println("❌ ALUMNO SERVICE: Error al listar alumnos: " + e.getMessage());
            throw new RuntimeException("Error al obtener la lista de alumnos: " + e.getMessage(), e);
        }
    }
    
    /**
     * Lista alumnos activos solamente
     */
    public List<AlumnoListarDTO> listarAlumnosActivos() {
        System.out.println("🔍 ALUMNO SERVICE: Obteniendo lista de alumnos activos");
        try {
            List<Alumno> alumnos = alumnoRepository.findByEstadoAlumno(true);
            System.out.println("✅ ALUMNO SERVICE: Se encontraron " + alumnos.size() + " alumnos activos");
            
            return alumnos.stream()
                    .map(AlumnoListarDTO::new)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            System.err.println("❌ ALUMNO SERVICE: Error al listar alumnos activos: " + e.getMessage());
            throw new RuntimeException("Error al obtener la lista de alumnos activos: " + e.getMessage(), e);
        }
    }
    
    /**
     * Busca un alumno por DNI y devuelve su información completa
     */
    public AlumnoListarDTO buscarAlumnoPorDni(String dni) {
        System.out.println("🔍 ALUMNO SERVICE: Buscando alumno por DNI: " + dni);
        try {
            Optional<Alumno> alumno = alumnoRepository.findByDniAlumno(dni);
            if (alumno.isPresent()) {
                System.out.println("✅ ALUMNO SERVICE: Alumno encontrado con ID: " + alumno.get().getId());
                return new AlumnoListarDTO(alumno.get());
            } else {
                System.out.println("❌ ALUMNO SERVICE: No se encontró alumno con DNI: " + dni);
                throw new RuntimeException("No se encontró un alumno con DNI: " + dni);
            }
        } catch (Exception e) {
            System.err.println("❌ ALUMNO SERVICE: Error al buscar alumno por DNI: " + e.getMessage());
            throw new RuntimeException("Error al buscar el alumno: " + e.getMessage(), e);
        }
    }
    
    /**
     * Actualiza el estado de un alumno (activo/inactivo)
     */
    @Transactional
    public AlumnoListarDTO actualizarEstadoAlumno(AlumnoEstadoDTO dto) {
        System.out.println("🔄 ALUMNO SERVICE: Actualizando estado del alumno ID: " + dto.getIdAlumno());
        
        try {
            // Buscar el alumno por ID
            Optional<Alumno> alumnoOpt = alumnoRepository.findById(dto.getIdAlumno());
            if (!alumnoOpt.isPresent()) {
                System.out.println("❌ ALUMNO SERVICE: No se encontró alumno con ID: " + dto.getIdAlumno());
                throw new RuntimeException("No se encontró un alumno con ID: " + dto.getIdAlumno());
            }
            
            Alumno alumno = alumnoOpt.get();
            Boolean estadoAnterior = alumno.getEstadoAlumno();
            
            // Actualizar el estado
            alumno.setEstadoAlumno(dto.getEstado());
            
            // Guardar los cambios
            Alumno alumnoActualizado = alumnoRepository.save(alumno);
            
            String estadoTexto = dto.getEstado() ? "activo" : "inactivo";
            String estadoAnteriorTexto = estadoAnterior ? "activo" : "inactivo";
            
            System.out.println("✅ ALUMNO SERVICE: Estado del alumno actualizado de '" + estadoAnteriorTexto + 
                             "' a '" + estadoTexto + "' para alumno ID: " + alumnoActualizado.getId());
            
            return new AlumnoListarDTO(alumnoActualizado);
            
        } catch (RuntimeException e) {
            // Re-lanzar errores de negocio
            throw e;
        } catch (Exception e) {
            System.err.println("❌ ALUMNO SERVICE: Error al actualizar estado del alumno: " + e.getMessage());
            throw new RuntimeException("Error al actualizar el estado del alumno: " + e.getMessage(), e);
        }
    }
    
    /**
     * Actualiza el estado de un alumno por DNI
     */
    @Transactional
    public AlumnoListarDTO actualizarEstadoAlumnoPorDni(String dni, Boolean estado) {
        System.out.println("🔄 ALUMNO SERVICE: Actualizando estado del alumno con DNI: " + dni);
        
        try {
            // Buscar el alumno por DNI
            Optional<Alumno> alumnoOpt = alumnoRepository.findByDniAlumno(dni);
            if (!alumnoOpt.isPresent()) {
                System.out.println("❌ ALUMNO SERVICE: No se encontró alumno con DNI: " + dni);
                throw new RuntimeException("No se encontró un alumno con DNI: " + dni);
            }
            
            Alumno alumno = alumnoOpt.get();
            Boolean estadoAnterior = alumno.getEstadoAlumno();
            
            // Actualizar el estado
            alumno.setEstadoAlumno(estado);
            
            // Guardar los cambios
            Alumno alumnoActualizado = alumnoRepository.save(alumno);
            
            String estadoTexto = estado ? "activo" : "inactivo";
            String estadoAnteriorTexto = estadoAnterior ? "activo" : "inactivo";
            
            System.out.println("✅ ALUMNO SERVICE: Estado del alumno actualizado de '" + estadoAnteriorTexto + 
                             "' a '" + estadoTexto + "' para alumno DNI: " + dni);
            
            return new AlumnoListarDTO(alumnoActualizado);
            
        } catch (RuntimeException e) {
            // Re-lanzar errores de negocio
            throw e;
        } catch (Exception e) {
            System.err.println("❌ ALUMNO SERVICE: Error al actualizar estado del alumno por DNI: " + e.getMessage());
            throw new RuntimeException("Error al actualizar el estado del alumno: " + e.getMessage(), e);
        }
    }
}