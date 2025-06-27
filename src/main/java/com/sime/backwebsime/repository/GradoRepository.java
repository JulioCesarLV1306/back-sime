package com.sime.backwebsime.repository;

import com.sime.backwebsime.model.Aula;
import com.sime.backwebsime.model.Grado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradoRepository extends JpaRepository<Grado, Integer> {

}
