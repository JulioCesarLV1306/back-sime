package com.sime.backwebsime.service;

import com.sime.backwebsime.DTO.ApoderadoCrearDTO;
import com.sime.backwebsime.model.Apoderado;
import com.sime.backwebsime.repository.ApoderadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApoderadoService {
    @Autowired
    private ApoderadoRepository apoderadoRepository;

    @Transactional
    public Apoderado crearApoderado(ApoderadoCrearDTO dto) {
        // Validaciones
        if (dto.getDni() == null || dto.getDni().trim().isEmpty()) {
            throw new RuntimeException("El DNI del apoderado es obligatorio");
        }
        if (dto.getNombres() == null || dto.getNombres().trim().isEmpty()) {
            throw new RuntimeException("El nombre del apoderado es obligatorio");
        }
        if (dto.getApellidos() == null || dto.getApellidos().trim().isEmpty()) {
            throw new RuntimeException("El apellido del apoderado es obligatorio");
        }
        if (dto.getParentesco() == null || dto.getParentesco().trim().isEmpty()) {
            throw new RuntimeException("El parentesco es obligatorio");
        }
        
        // Verificar si ya existe un apoderado con este DNI
        System.out.println("🔍 Verificando si existe apoderado con DNI: " + dto.getDni());
        
        Optional<Apoderado> apoderadoExistente = apoderadoRepository.findByDni(dto.getDni());
        if (apoderadoExistente.isPresent()) {
            System.out.println("✅ Reutilizando apoderado existente con DNI: " + dto.getDni());
            return apoderadoExistente.get();
        }
        
        System.out.println("➕ Creando nuevo apoderado con DNI: " + dto.getDni());
        
        try {
            Apoderado apoderado = new Apoderado();
            apoderado.setDni(dto.getDni());
            apoderado.setNombre(dto.getNombres());
            apoderado.setApellido(dto.getApellidos());
            apoderado.setEmail(dto.getEmail());
            apoderado.setTelefono(dto.getTelefono());
            
            try {
                apoderado.setParentesco(Apoderado.Parentesco.fromDisplayName(dto.getParentesco()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Parentesco inválido: '" + dto.getParentesco() + "'. Valores válidos: Padre, Madre, Abuelo/a, Tío/a, Hermano/a, Tutor Legal, Otro");
            }
            
            // Dirección completa
            apoderado.setDireccion(dto.getDireccion());
            apoderado.setDepartamento(dto.getDepartamento());
            apoderado.setProvincia(dto.getProvincia());
            apoderado.setDistrito(dto.getDistrito());
            
            // Información laboral
            apoderado.setLugarTrabajo(dto.getLugarTrabajo());
            apoderado.setCargo(dto.getCargo());
            
            // Intentar guardar
            Apoderado apoderadoGuardado = apoderadoRepository.save(apoderado);
            System.out.println("✅ Apoderado creado exitosamente con ID: " + apoderadoGuardado.getId());
            return apoderadoGuardado;
            
        } catch (Exception e) {
            System.err.println("❌ Error al crear apoderado: " + e.getMessage());
            
            // En caso de error, intentar buscar si el apoderado ya existe
            Optional<Apoderado> apoderadoConflicto = apoderadoRepository.findByDni(dto.getDni());
            if (apoderadoConflicto.isPresent()) {
                System.out.println("✅ Apoderado encontrado después del conflicto, reutilizando");
                return apoderadoConflicto.get();
            }
            
            throw new RuntimeException("Error al crear el apoderado: " + e.getMessage());
        }
    }
}
