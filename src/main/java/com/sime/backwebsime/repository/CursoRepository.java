package com.sime.backwebsime.repository;

import com.sime.backwebsime.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso,Long> {
}
