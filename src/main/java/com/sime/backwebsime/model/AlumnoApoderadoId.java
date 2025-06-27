package com.sime.backwebsime.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoApoderadoId implements Serializable {
    
    private Long alumno;
    private Long apoderado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlumnoApoderadoId that = (AlumnoApoderadoId) o;
        return Objects.equals(alumno, that.alumno) && 
               Objects.equals(apoderado, that.apoderado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alumno, apoderado);
    }
}
