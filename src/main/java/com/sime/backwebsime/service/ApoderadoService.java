package com.sime.backwebsime.service;

import com.sime.backwebsime.DTO.ApoderadoCrearDTO;
import com.sime.backwebsime.model.Apoderado;
import com.sime.backwebsime.repository.ApoderadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApoderadoService {
    @Autowired
    private ApoderadoRepository apoderadoRepository;

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
        Optional<Apoderado> apoderadoExistente = apoderadoRepository.findByDni(dto.getDni());
        if (apoderadoExistente.isPresent()) {
            // Retornar el apoderado existente en lugar de crear uno nuevo
            return apoderadoExistente.get();
        }
        
        // Si no existe, crear uno nuevo
        Apoderado apoderado = new Apoderado();
        apoderado.setDni(dto.getDni());
        apoderado.setNombre(dto.getNombres());
        apoderado.setApellido(dto.getApellidos());
        apoderado.setEmail(dto.getCorreoElectronico());
        try {
            apoderado.setParentesco(Apoderado.Parentesco.fromDisplayName(dto.getParentesco()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Parentesco inválido: '" + dto.getParentesco() + "'. Valores válidos: Padre, Madre, Abuelo/a, Tío/a, Hermano/a, Tutor Legal, Otro");
        }
        apoderado.setTelefono(dto.getTelefono());
        apoderado.setDireccion(dto.getDireccionLaboral());
        apoderado.setCargo(dto.getSituacionLaboral());

        return apoderadoRepository.save(apoderado);
    }
}
