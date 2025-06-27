package com.sime.backwebsime.repository;

import com.sime.backwebsime.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    List<Matricula> findByAula_IdAula(Long ID_AULA);
    boolean existsByAlumno_Id(Matricula matricula);
}