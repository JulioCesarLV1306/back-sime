package com.sime.backwebsime.service;


import com.sime.backwebsime.DTO.AlumnoCrearDTO;
import com.sime.backwebsime.model.Alumno;
import com.sime.backwebsime.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    public Alumno crearAlumno(AlumnoCrearDTO dto) {
        // Validaciones
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
        
        // Verificar si ya existe un alumno con este DNI
        Optional<Alumno> alumnoExistente = alumnoRepository.findByDniAlumno(dto.getDni());
        if (alumnoExistente.isPresent()) {
            // Retornar el alumno existente en lugar de crear uno nuevo
            return alumnoExistente.get();
        }
        
        // Si no existe, crear uno nuevo
        Alumno alumno = new Alumno();
        alumno.setDniAlumno(dto.getDni());
        alumno.setNombreAlumno(dto.getNombres());
        alumno.setApellidoAlumno(dto.getApellidos());
        try {
            alumno.setGeneroAlumno(Alumno.Genero.valueOf(dto.getGenero()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Género inválido: '" + dto.getGenero() + "'. Valores válidos: Masculino, Femenino, Otro");
        }
        alumno.setDireccionAlumno(dto.getDireccion());
        alumno.setTelefonoEmergencia(dto.getTelefonoEmergencia());
        alumno.setEstadoAlumno(true); // Por defecto activo
        

        return alumnoRepository.save(alumno);
    }
}