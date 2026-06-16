package com.somosayni.retos.application.port;

import com.somosayni.retos.domain.model.Reto;
import java.util.List;
import java.util.Optional;

public interface RetoRepository {
    Optional<Reto> findById(String id);
    List<Reto> findAll();
    List<Reto> findByEmpresaId(String empresaId);
    List<Reto> findByEstado(Reto.EstadoReto estado);
    List<Reto> findActivos();
    Reto save(Reto reto);
    void deleteById(String id);
}
