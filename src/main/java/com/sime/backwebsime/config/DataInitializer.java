package com.sime.backwebsime.config;

import com.sime.backwebsime.model.Aula;
import com.sime.backwebsime.model.Grado;
import com.sime.backwebsime.repository.AulaRepository;
import com.sime.backwebsime.repository.GradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private GradoRepository gradoRepository;

    @Autowired
    private AulaRepository aulaRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            System.out.println("🚀 Iniciando inicialización de datos...");
            
            // Solo inicializar datos si no existen
            if (gradoRepository.count() == 0) {
                System.out.println("📚 Inicializando grados...");
                initializeGrados();
            } else {
                System.out.println("ℹ️ Los grados ya existen, omitiendo inicialización");
            }
            
            if (aulaRepository.count() == 0) {
                System.out.println("🏫 Inicializando aulas...");
                initializeAulas();
            } else {
                System.out.println("ℹ️ Las aulas ya existen, omitiendo inicialización");
            }
            
            System.out.println("✅ Inicialización de datos completada exitosamente");
            
        } catch (Exception e) {
            System.err.println("❌ Error durante la inicialización de datos: " + e.getMessage());
            e.printStackTrace();
            // No relanzar la excepción para que no falle el inicio de la aplicación
        }
    }

    private void initializeGrados() {
        try {
            // Crear grados básicos
            Grado primero = new Grado();
            primero.setNombre("1er Grado");
            primero.setNivel(Grado.Nivel.BASICO);
            gradoRepository.save(primero);

            Grado segundo = new Grado();
            segundo.setNombre("2do Grado");
            segundo.setNivel(Grado.Nivel.BASICO);
            gradoRepository.save(segundo);

            Grado tercero = new Grado();
            tercero.setNombre("3er Grado");
            tercero.setNivel(Grado.Nivel.BASICO);
            gradoRepository.save(tercero);

            Grado cuarto = new Grado();
            cuarto.setNombre("4to Grado");
            cuarto.setNivel(Grado.Nivel.MEDIO);
            gradoRepository.save(cuarto);

            Grado quinto = new Grado();
            quinto.setNombre("5to Grado");
            quinto.setNivel(Grado.Nivel.MEDIO);
            gradoRepository.save(quinto);

            Grado sexto = new Grado();
            sexto.setNombre("6to Grado");
            sexto.setNivel(Grado.Nivel.SUPERIOR);
            gradoRepository.save(sexto);

            System.out.println("✅ Grados inicializados correctamente");
            
        } catch (Exception e) {
            System.err.println("❌ Error al inicializar grados: " + e.getMessage());
            throw new RuntimeException("Error al inicializar grados", e);
        }
    }

    private void initializeAulas() {
        try {
            // Obtener todos los grados
            var grados = gradoRepository.findAll();
            
            if (grados.isEmpty()) {
                System.out.println("⚠️ No hay grados disponibles para crear aulas");
                return;
            }
            
            for (Grado grado : grados) {
                // Crear 2 aulas por grado
                Aula aula1 = new Aula();
                aula1.setNombre("Aula " + grado.getNombre() + "A");
                aula1.setCapacidad(30);
                aula1.setGrado(grado);
                aulaRepository.save(aula1);

                Aula aula2 = new Aula();
                aula2.setNombre("Aula " + grado.getNombre() + "B");
                aula2.setCapacidad(25);
                aula2.setGrado(grado);
                aulaRepository.save(aula2);
            }

            System.out.println("✅ Aulas inicializadas correctamente");
            
        } catch (Exception e) {
            System.err.println("❌ Error al inicializar aulas: " + e.getMessage());
            throw new RuntimeException("Error al inicializar aulas", e);
        }
    }
}
