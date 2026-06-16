package com.somosayni.retos.application.command;

import com.somosayni.retos.application.port.RetoRepository;
import com.somosayni.retos.domain.model.Reto;
import org.springframework.stereotype.Component;

@Component
public class PublicarRetoCommandHandler {

    private final RetoRepository repository;

    public PublicarRetoCommandHandler(RetoRepository repository) {
        this.repository = repository;
    }

    public Reto handle(PublicarRetoCommand command) {
        Reto reto = new Reto(command.empresaId(), command.titulo(), command.descripcion());

        if (command.categoria() != null) {
            reto.setCategoria(Reto.Categoria.valueOf(command.categoria().toUpperCase()));
        }
        if (command.requisitos() != null) {
            reto.setRequisitos(command.requisitos().stream().map(Reto.Requisito::new).toList());
        }
        if (command.entregables() != null) {
            reto.setEntregables(command.entregables().stream().map(Reto.Entregable::new).toList());
        }
        if (command.tipoRecompensa() != null) {
            Reto.Recompensa.Tipo tipo = Reto.Recompensa.Tipo.valueOf(command.tipoRecompensa().toUpperCase());
            reto.setRecompensa(new Reto.Recompensa(tipo, command.montoRecompensa(), ""));
        }
        if (command.fechaLimite() != null) {
            reto.setFechaLimite(command.fechaLimite());
        }
        if (command.nivelDificultad() != null) {
            reto.setNivelDificultad(Reto.NivelDificultad.valueOf(command.nivelDificultad().toUpperCase()));
        }
        if (command.cuposDisponibles() > 0) {
            reto.setCuposDisponibles(command.cuposDisponibles());
        }

        reto.publicar();
        return repository.save(reto);
    }
}
