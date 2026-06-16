package com.somosayni.retos.infrastructure.persistence.entity;

import com.somosayni.retos.domain.model.Reto;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reto")
public class RetoEntity {

    @Id
    private String id;

    @Column(name = "empresa_id", nullable = false)
    private String empresaId;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 4000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private Reto.Categoria categoria;

    @ElementCollection
    @CollectionTable(name = "reto_requisito", joinColumns = @JoinColumn(name = "reto_id"))
    private List<RequisitoEmbed> requisitos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "reto_entregable", joinColumns = @JoinColumn(name = "reto_id"))
    private List<EntregableEmbed> entregables = new ArrayList<>();

    @Embedded
    private RecompensaEmbed recompensa;

    @Column(name = "fecha_limite")
    private LocalDate fechaLimite;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_dificultad")
    private Reto.NivelDificultad nivelDificultad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Reto.EstadoReto estado;

    @Column(name = "cupos_disponibles")
    private int cuposDisponibles;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (id == null) id = UUID.randomUUID().toString();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Reto toDomain() {
        Reto reto = new Reto(empresaId, titulo, descripcion);
        reto.setId(id);
        reto.setCategoria(categoria);
        if (requisitos != null) reto.setRequisitos(requisitos.stream().map(r -> new Reto.Requisito(r.descripcion)).toList());
        if (entregables != null) reto.setEntregables(entregables.stream().map(e -> new Reto.Entregable(e.descripcion)).toList());
        if (recompensa != null) reto.setRecompensa(new Reto.Recompensa(recompensa.tipo, recompensa.monto, recompensa.descripcion));
        reto.setFechaLimite(fechaLimite);
        reto.setNivelDificultad(nivelDificultad);
        reto.setCuposDisponibles(cuposDisponibles);
        reto.setEstado(estado);
        return reto;

    }

    public static RetoEntity fromDomain(Reto reto) {
        RetoEntity entity = new RetoEntity();
        entity.id = reto.getId();
        entity.empresaId = reto.getEmpresaId();
        entity.titulo = reto.getTitulo();
        entity.descripcion = reto.getDescripcion();
        entity.categoria = reto.getCategoria();
        if (reto.getRequisitos() != null) entity.requisitos = reto.getRequisitos().stream().map(r -> { RequisitoEmbed e = new RequisitoEmbed(); e.descripcion = r.descripcion(); return e; }).toList();
        if (reto.getEntregables() != null) entity.entregables = reto.getEntregables().stream().map(e -> { EntregableEmbed em = new EntregableEmbed(); em.descripcion = e.descripcion(); return em; }).toList();
        if (reto.getRecompensa() != null) { entity.recompensa = new RecompensaEmbed(); entity.recompensa.tipo = reto.getRecompensa().tipo(); entity.recompensa.monto = reto.getRecompensa().monto(); entity.recompensa.descripcion = reto.getRecompensa().descripcion(); }
        entity.fechaLimite = reto.getFechaLimite();
        entity.nivelDificultad = reto.getNivelDificultad();
        entity.estado = reto.getEstado();
        entity.cuposDisponibles = reto.getCuposDisponibles();
        return entity;
    }

    @Embeddable
    public static class RequisitoEmbed { public String descripcion; }
    @Embeddable
    public static class EntregableEmbed { public String descripcion; }
    @Embeddable
    public static class RecompensaEmbed {
        @Column(name = "recompensa_tipo")
        public Reto.Recompensa.Tipo tipo;

        @Column(name = "recompensa_monto")
        public int monto;

        @Column(name = "recompensa_descripcion")
        public String descripcion;
    }
}
