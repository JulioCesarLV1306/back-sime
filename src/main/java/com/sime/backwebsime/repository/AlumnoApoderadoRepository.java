package com.sime.backwebsime.repository;

import com.sime.backwebsime.model.Alumno_Apoderado;
import com.sime.backwebsime.model.AlumnoApoderadoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoApoderadoRepository extends JpaRepository<Alumno_Apoderado, AlumnoApoderadoId> {
}
