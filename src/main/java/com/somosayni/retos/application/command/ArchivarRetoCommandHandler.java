package com.somosayni.retos.application.command;

import com.somosayni.retos.application.port.RetoRepository;
import com.somosayni.retos.domain.model.Reto;
import org.springframework.stereotype.Component;

@Component
public class ArchivarRetoCommandHandler {

    private final RetoRepository repository;

    public ArchivarRetoCommandHandler(RetoRepository repository) {
        this.repository = repository;
    }

    public void handle(ArchivarRetoCommand command) {
        Reto reto = repository.findById(command.retoId())
                .orElseThrow(() -> new IllegalArgumentException("Reto no encontrado"));

        reto.archivar();
        repository.save(reto);
    }
}
