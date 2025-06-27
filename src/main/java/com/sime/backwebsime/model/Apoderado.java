package com.sime.backwebsime.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "APODERADOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apoderado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_APODERADO", nullable = false)
    private Long id;

    @Column(name = "DNI", length = 8, nullable = false, unique = true)
    private String dni;

    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String nombre;

    @Column(name = "APELLIDO", length = 50, nullable = false)
    private String apellido;

    @Column(name = "PARENTESCO", nullable = false)
    @Enumerated(EnumType.STRING)
    private Parentesco parentesco;

    @Column(name = "DIRECCION", length = 255)
    private String direccion;

    @Column(name = "DEPARTAMENTO", length = 100)
    private String departamento;

    @Column(name = "PROVINCIA", length = 100)
    private String provincia;

    @Column(name = "DISTRITO", length = 100)
    private String distrito;

    @Column(name = "TELEFONO", length = 15)
    private String telefono;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "LUGAR_TRABAJO", length = 100)
    private String lugarTrabajo;

    @Column(name = "CARGO", length = 100)
    private String cargo;

    public enum Parentesco {
        PADRE("Padre"),
        MADRE("Madre"), 
        ABUELO_A("Abuelo/a"),
        TIO_A("Tío/a"),
        HERMANO_A("Hermano/a"),
        TUTOR_LEGAL("Tutor Legal"),
        OTRO("Otro");
        
        private final String displayName;
        
        Parentesco(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public static Parentesco fromDisplayName(String displayName) {
            for (Parentesco parentesco : values()) {
                if (parentesco.getDisplayName().equals(displayName)) {
                    return parentesco;
                }
            }
            throw new IllegalArgumentException("Parentesco no válido: " + displayName);
        }
    }
}
