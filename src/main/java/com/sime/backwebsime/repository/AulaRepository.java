package com.sime.backwebsime.repository;

import com.sime.backwebsime.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface AulaRepository extends JpaRepository<Aula, Long> {
    @Query("SELECT a FROM Aula a WHERE a.grado.id = :idGrado")
    List<Aula> findByGradoId(@Param("idGrado") Integer idGrado);

    @Query("SELECT a FROM Aula a JOIN FETCH a.grado")
    List<Aula> findAllWithGrado();

}