package com.somosayni.retos.application.command;

import com.somosayni.retos.domain.model.Reto;
import java.time.LocalDate;
import java.util.List;

public record PublicarRetoCommand(
        String empresaId,
        String titulo,
        String descripcion,
        String categoria,
        List<String> requisitos,
        List<String> entregables,
        String tipoRecompensa,
        int montoRecompensa,
        LocalDate fechaLimite,
        String nivelDificultad,
        int cuposDisponibles
) {}
