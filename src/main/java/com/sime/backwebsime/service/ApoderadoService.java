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
        System.out.println("🔵 APODERADO SERVICE: Iniciando creación/búsqueda de apoderado");
        
        // Validaciones básicas
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
        System.out.println("🔍 APODERADO SERVICE: Buscando apoderado con DNI: " + dto.getDni());
        
        Optional<Apoderado> apoderadoExistente = apoderadoRepository.findByDni(dto.getDni());
        if (apoderadoExistente.isPresent()) {
            Apoderado apoderado = apoderadoExistente.get();
            System.out.println("✅ APODERADO SERVICE: Encontrado apoderado existente con ID: " + apoderado.getId());
            return apoderado;
        }
        
        System.out.println("➕ APODERADO SERVICE: Creando nuevo apoderado con DNI: " + dto.getDni());
        
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
            
            System.out.println("💾 APODERADO SERVICE: Guardando nuevo apoderado...");
            
            // Guardar sin flush inicialmente
            Apoderado apoderadoGuardado = apoderadoRepository.save(apoderado);
            
            System.out.println("✅ APODERADO SERVICE: Apoderado guardado exitosamente con ID: " + apoderadoGuardado.getId());
            return apoderadoGuardado;
            
        } catch (Exception e) {
            System.err.println("❌ APODERADO SERVICE: Error al crear apoderado: " + e.getMessage());
            throw new RuntimeException("Error al crear el apoderado: " + e.getMessage(), e);
        }
    }
}
