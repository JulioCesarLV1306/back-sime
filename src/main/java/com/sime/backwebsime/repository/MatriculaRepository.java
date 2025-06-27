package com.sime.backwebsime.repository;

import com.sime.backwebsime.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    
    // Consultas básicas
    List<Matricula> findByAula_IdAula(Long aulaId);
    List<Matricula> findByAlumno_IdAndAnioEscolar(Long alumnoId, String anioEscolar);
    boolean existsByAlumno_Id(Long alumnoId);
    
    // Consultas para el año escolar actual
    List<Matricula> findByAnioEscolar(String anioEscolar);
    List<Matricula> findByAnioEscolarAndEstado(String anioEscolar, Matricula.EstadoMatricula estado);
    
    // Consultas para vacantes
    @Query("SELECT COUNT(m) FROM Matricula m WHERE m.aula.idAula = :aulaId AND m.estado = 'activo'")
    int countMatriculasActivasByAula(@Param("aulaId") Long aulaId);
    
    @Query("SELECT COUNT(m) FROM Matricula m WHERE m.aula.grado.id = :gradoId AND m.estado = 'activo' AND m.anioEscolar = :anioEscolar")
    int countMatriculasActivasByGradoAndAnio(@Param("gradoId") Long gradoId, @Param("anioEscolar") String anioEscolar);
    
    // Verificar matrícula existente
    @Query("SELECT m FROM Matricula m WHERE m.alumno.id = :alumnoId AND m.anioEscolar = :anioEscolar AND m.estado IN ('activo', 'pendiente')")
    Optional<Matricula> findMatriculaActivaByAlumnoAndAnio(@Param("alumnoId") Long alumnoId, @Param("anioEscolar") String anioEscolar);
    
    // Estadísticas
    @Query("SELECT COUNT(m) FROM Matricula m WHERE m.estado = :estado AND m.anioEscolar = :anioEscolar")
    int countByEstadoAndAnio(@Param("estado") Matricula.EstadoMatricula estado, @Param("anioEscolar") String anioEscolar);
}