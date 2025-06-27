package com.sime.backwebsime.repository;

import com.sime.backwebsime.model.Grado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GradoRepository extends JpaRepository<Grado, Integer> {

}
