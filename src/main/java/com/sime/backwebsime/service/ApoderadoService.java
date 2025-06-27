package com.sime.backwebsime.service;

import com.sime.backwebsime.DTO.ApoderadoCrearDTO;
import com.sime.backwebsime.model.Apoderado;
import com.sime.backwebsime.repository.ApoderadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        
        if (apoderadoRepository.existsByDni(dto.getDni())) {
            throw new RuntimeException("Ya existe un apoderado con este DNI");
        }
        
        Apoderado apoderado = new Apoderado();
        apoderado.setDni(dto.getDni());
        apoderado.setNombre(dto.getNombres());
        apoderado.setApellido(dto.getApellidos());
        apoderado.setEmail(dto.getCorreoElectronico());
        apoderado.setParentesco(Apoderado.Parentesco.valueOf(dto.getParentesco()));
        apoderado.setTelefono(dto.getTelefono());
        apoderado.setDireccion(dto.getDireccionLaboral());
        apoderado.setCargo(dto.getSituacionLaboral());

        return apoderadoRepository.save(apoderado);
    }
}
