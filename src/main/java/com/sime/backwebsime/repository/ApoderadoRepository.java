package com.sime.backwebsime.repository;

import com.sime.backwebsime.model.Apoderado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApoderadoRepository extends JpaRepository<Apoderado,Long> {
    default boolean existsByDni(String dni) {
        return false;
    }
}
