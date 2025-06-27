package com.sime.backwebsime.repository;

import com.sime.backwebsime.model.Apoderado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApoderadoRepository extends JpaRepository<Apoderado,Long> {
    boolean existsByDni(String dni);
    Optional<Apoderado> findByDni(String dni);
}
