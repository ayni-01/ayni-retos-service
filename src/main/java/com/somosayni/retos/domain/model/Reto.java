package com.somosayni.retos.domain.model;

import com.somosayni.shared.domain.model.AggregateRoot;
import java.time.LocalDate;
import java.util.List;

public class Reto extends AggregateRoot {

    private String empresaId;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private List<Requisito> requisitos;
    private List<Entregable> entregables;
    private Recompensa recompensa;
    private LocalDate fechaLimite;
    private NivelDificultad nivelDificultad;
    private EstadoReto estado;
    private int cuposDisponibles;

    public Reto() {}

    public Reto(String empresaId, String titulo, String descripcion) {
        this.empresaId = empresaId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = EstadoReto.BORRADOR;
        this.cuposDisponibles = 10;
    }

    public void publicar() {
        if (this.estado != EstadoReto.BORRADOR) {
            throw new IllegalStateException("Solo retos en estado BORRADOR pueden ser publicados");
        }
        this.estado = EstadoReto.ACTIVO;
        registerEvent(new AggregateRoot.DomainEvent("RetoPublicado", this.getId(), java.time.LocalDateTime.now()));
    }

    public void cerrar() {
        if (this.estado != EstadoReto.ACTIVO) {
            throw new IllegalStateException("Solo retos activos pueden ser cerrados");
        }
        this.estado = EstadoReto.CERRADO;
        registerEvent(new AggregateRoot.DomainEvent("RetoCerrado", this.getId(), java.time.LocalDateTime.now()));
    }

    public void archivar() {
        if (this.estado == EstadoReto.ARCHIVADO) {
            throw new IllegalStateException("El reto ya está archivado");
        }
        this.estado = EstadoReto.ARCHIVADO;
    }

    public void duplicar() {
        Reto duplicado = new Reto(this.empresaId, this.titulo + " (Copia)", this.descripcion);
        duplicado.setCategoria(this.categoria);
        duplicado.setRequisitos(this.requisitos);
        duplicado.setEntregables(this.entregables);
        duplicado.setRecompensa(this.recompensa);
        duplicado.setNivelDificultad(this.nivelDificultad);
        duplicado.setCuposDisponibles(this.cuposDisponibles);
    }

    public String getEmpresaId() { return empresaId; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public Categoria getCategoria() { return categoria; }
    public List<Requisito> getRequisitos() { return requisitos; }
    public List<Entregable> getEntregables() { return entregables; }
    public Recompensa getRecompensa() { return recompensa; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public NivelDificultad getNivelDificultad() { return nivelDificultad; }
    public EstadoReto getEstado() { return estado; }
    public int getCuposDisponibles() { return cuposDisponibles; }

    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public void setRequisitos(List<Requisito> requisitos) { this.requisitos = requisitos; }
    public void setEntregables(List<Entregable> entregables) { this.entregables = entregables; }
    public void setRecompensa(Recompensa recompensa) { this.recompensa = recompensa; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }
    public void setNivelDificultad(NivelDificultad nivelDificultad) { this.nivelDificultad = nivelDificultad; }
    public void setCuposDisponibles(int cuposDisponibles) { this.cuposDisponibles = cuposDisponibles; }
    public void setEstado(EstadoReto estado) { this.estado = estado; }


    public enum EstadoReto {
        BORRADOR, ACTIVO, CERRADO, ARCHIVADO
    }

    public enum NivelDificultad {
        JUNIOR, TRAINEE, SENIOR
    }

    public enum Categoria {
        FRONTEND, BACKEND, FULLSTACK, DATA, DEVOPS, UX_UI, QA, MOBILE
    }

    public record Requisito(String descripcion) {}
    public record Entregable(String descripcion) {}
    public record Recompensa(Tipo tipo, int monto, String descripcion) {
        public enum Tipo { MONETARIA, CONTRATACION, DIPLOMA }
    }


}
