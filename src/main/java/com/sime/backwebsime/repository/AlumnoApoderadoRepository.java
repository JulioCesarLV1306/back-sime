package com.sime.backwebsime.repository;

import com.sime.backwebsime.model.Alumno_Apoderado;
import com.sime.backwebsime.model.AlumnoApoderadoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AlumnoApoderadoRepository extends JpaRepository<Alumno_Apoderado, AlumnoApoderadoId> {
    boolean existsByAlumno_IdAndApoderado_Id(Long alumnoId, Long apoderadoId);
    
    @Query("SELECT aa FROM Alumno_Apoderado aa " +
           "LEFT JOIN FETCH aa.apoderado " +
           "WHERE aa.alumno.id = :alumnoId AND aa.esPrincipal = true")
    Optional<Alumno_Apoderado> findApoderadoPrincipalByAlumnoId(@Param("alumnoId") Long alumnoId);
}
