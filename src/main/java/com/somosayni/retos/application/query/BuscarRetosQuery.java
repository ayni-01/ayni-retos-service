package com.somosayni.retos.application.query;

import com.somosayni.retos.domain.model.Reto;

public record BuscarRetosQuery(
        String categoria,
        String nivelDificultad,
        String empresaId,
        String estado
) {}
