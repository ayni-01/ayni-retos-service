package com.somosayni.retos.infrastructure.persistence.repository;

import com.somosayni.retos.domain.model.Reto;
import com.somosayni.retos.infrastructure.persistence.entity.RetoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaRetoRepository extends JpaRepository<RetoEntity, String> {
    List<RetoEntity> findByEmpresaId(String empresaId);
    List<RetoEntity> findByEstado(Reto.EstadoReto estado);
    List<RetoEntity> findByEstadoIn(List<Reto.EstadoReto> estados);
}
