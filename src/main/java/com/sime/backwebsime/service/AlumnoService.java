package com.sime.backwebsime.service;

import com.sime.backwebsime.DTO.AlumnoCrearDTO;
import com.sime.backwebsime.model.Alumno;
import com.sime.backwebsime.repository.AlumnoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}