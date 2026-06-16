package com.somosayni.retos.application.command;

import com.somosayni.retos.application.port.RetoRepository;
import com.somosayni.retos.domain.model.Reto;
import org.springframework.stereotype.Component;

@Component
public class CerrarRetoCommandHandler {

    private final RetoRepository repository;

    public CerrarRetoCommandHandler(RetoRepository repository) {
        this.repository = repository;
    }

    public Reto handle(CerrarRetoCommand command) {
        Reto reto = repository.findById(command.retoId())
                .orElseThrow(() -> new IllegalArgumentException("Reto no encontrado"));

        reto.cerrar();
        return repository.save(reto);
    }
}
