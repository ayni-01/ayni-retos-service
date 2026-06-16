package com.somosayni.retos.infrastructure.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record PublicarRetoRequest(
        @NotBlank(message = "Título es obligatorio")
        String titulo,

        @NotBlank(message = "Descripción es obligatoria")
        String descripcion,

        String categoria,
        java.util.List<String> requisitos,
        java.util.List<String> entregables,
        String tipoRecompensa,
        int montoRecompensa,
        java.time.LocalDate fechaLimite,
        String nivelDificultad,
        int cuposDisponibles
) {}
