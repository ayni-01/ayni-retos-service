package com.somosayni.retos.application.command;

import com.somosayni.retos.application.port.RetoRepository;
import com.somosayni.retos.domain.model.Reto;
import org.springframework.stereotype.Component;

@Component
public class DuplicarRetoCommandHandler {

    private final RetoRepository repository;

    public DuplicarRetoCommandHandler(RetoRepository repository) {
        this.repository = repository;
    }

    public Reto handle(DuplicarRetoCommand command) {
        Reto original = repository.findById(command.retoId())
                .orElseThrow(() -> new IllegalArgumentException("Reto no encontrado"));

        Reto duplicado = new Reto(original.getEmpresaId(), original.getTitulo() + " (Copia)", original.getDescripcion());
        duplicado.setCategoria(original.getCategoria());
        duplicado.setRequisitos(original.getRequisitos());
        duplicado.setEntregables(original.getEntregables());
        duplicado.setRecompensa(original.getRecompensa());
        duplicado.setNivelDificultad(original.getNivelDificultad());
        duplicado.setCuposDisponibles(original.getCuposDisponibles());

        return repository.save(duplicado);
    }
}
