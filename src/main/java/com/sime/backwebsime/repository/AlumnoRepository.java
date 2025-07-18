package com.sime.backwebsime.repository;

import com.sime.backwebsime.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno,Long> {
    boolean existsByDniAlumno(String dniAlumno);
    Optional<Alumno> findByDniAlumno(String dniAlumno);
    List<Alumno> findByEstadoAlumno(Boolean estado);
}
