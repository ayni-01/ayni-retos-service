package com.somosayni.retos.application.query;

import com.somosayni.retos.application.port.RetoRepository;
import com.somosayni.retos.domain.model.Reto;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BuscarRetosQueryHandler {

    private final RetoRepository repository;

    public BuscarRetosQueryHandler(RetoRepository repository) {
        this.repository = repository;
    }

    public List<Reto> handle(BuscarRetosQuery query) {
        if (query.estado() != null) {
            return repository.findByEstado(Reto.EstadoReto.valueOf(query.estado().toUpperCase()));
        }
        if (query.empresaId() != null) {
            return repository.findByEmpresaId(query.empresaId());
        }
        if (query.categoria() != null || query.nivelDificultad() != null) {
            return repository.findActivos().stream()
                    .filter(r -> query.categoria() == null || r.getCategoria().name().equalsIgnoreCase(query.categoria()))
                    .filter(r -> query.nivelDificultad() == null || r.getNivelDificultad().name().equalsIgnoreCase(query.nivelDificultad()))
                    .toList();
        }
        return repository.findAll();
    }
}
