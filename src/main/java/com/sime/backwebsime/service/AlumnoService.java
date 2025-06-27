package com.sime.backwebsime.service;


import com.sime.backwebsime.DTO.AlumnoCrearDTO;
import com.sime.backwebsime.model.Alumno;
import com.sime.backwebsime.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    public Alumno crearAlumno(AlumnoCrearDTO dto) {
        Alumno alumno = new Alumno();
        alumno.setDniAlumno(dto.getDni());
        alumno.setNombreAlumno(dto.getNombres());
        alumno.setApellidoAlumno(dto.getApellidos());
        alumno.setGeneroAlumno(Alumno.Genero.valueOf(dto.getGenero()));
        alumno.setDireccionAlumno(dto.getDireccion());
        alumno.setTelefonoEmergencia(dto.getTelefonoEmergencia());

        return alumnoRepository.save(alumno);
    }
}