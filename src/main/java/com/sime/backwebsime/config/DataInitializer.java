package com.sime.backwebsime.config;

import com.sime.backwebsime.model.Aula;
import com.sime.backwebsime.model.Grado;
import com.sime.backwebsime.repository.AulaRepository;
import com.sime.backwebsime.repository.GradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private GradoRepository gradoRepository;

    @Autowired
    private AulaRepository aulaRepository;

    @Override
    public void run(String... args) throws Exception {
        // Solo inicializar datos si no existen
        if (gradoRepository.count() == 0) {
            initializeGrados();
        }
        
        if (aulaRepository.count() == 0) {
            initializeAulas();
        }
    }

    private void initializeGrados() {
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
    }

    private void initializeAulas() {
        // Obtener todos los grados
        var grados = gradoRepository.findAll();
        
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
    }
}
