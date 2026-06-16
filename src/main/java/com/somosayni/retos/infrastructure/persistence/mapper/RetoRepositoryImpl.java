package com.somosayni.retos.infrastructure.persistence.mapper;

import com.somosayni.retos.application.port.RetoRepository;
import com.somosayni.retos.domain.model.Reto;
import com.somosayni.retos.infrastructure.persistence.entity.RetoEntity;
import com.somosayni.retos.infrastructure.persistence.repository.JpaRetoRepository;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RetoRepositoryImpl implements RetoRepository {

    private final JpaRetoRepository jpaRepository;

    public RetoRepositoryImpl(JpaRetoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Reto> findAll() {
        return jpaRepository.findAll().stream().map(RetoEntity::toDomain).toList();
    }

    @Override
    public Reto save(Reto reto) {
        return jpaRepository.save(RetoEntity.fromDomain(reto)).toDomain();
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Reto> findActivos() {
        return jpaRepository.findByEstadoIn(List.of(Reto.EstadoReto.ACTIVO)).stream().map(RetoEntity::toDomain).toList();
    }

    @Override
    public List<Reto> findByEmpresaId(String empresaId) {
        return jpaRepository.findByEmpresaId(empresaId).stream().map(RetoEntity::toDomain).toList();
    }

    @Override
    public List<Reto> findByEstado(Reto.EstadoReto estado) {
        return jpaRepository.findByEstado(estado).stream().map(RetoEntity::toDomain).toList();
    }

    @Override
    public java.util.Optional<Reto> findById(String id) {
        return jpaRepository.findById(id).map(RetoEntity::toDomain);
    }
}
