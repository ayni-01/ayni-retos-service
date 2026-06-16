package com.somosayni.retos.infrastructure.rest;

import com.somosayni.retos.application.command.*;
import com.somosayni.retos.application.query.BuscarRetosQuery;
import com.somosayni.retos.application.query.BuscarRetosQueryHandler;
import com.somosayni.retos.domain.model.Reto;
import com.somosayni.retos.infrastructure.rest.dto.PublicarRetoRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/retos")
public class RetoController {

    private final PublicarRetoCommandHandler publicarHandler;
    private final DuplicarRetoCommandHandler duplicarHandler;
    private final CerrarRetoCommandHandler cerrarHandler;
    private final ArchivarRetoCommandHandler archivarHandler;
    private final BuscarRetosQueryHandler buscarHandler;

    public RetoController(
            PublicarRetoCommandHandler publicarHandler,
            DuplicarRetoCommandHandler duplicarHandler,
            CerrarRetoCommandHandler cerrarHandler,
            ArchivarRetoCommandHandler archivarHandler,
            BuscarRetosQueryHandler buscarHandler) {
        this.publicarHandler = publicarHandler;
        this.duplicarHandler = duplicarHandler;
        this.cerrarHandler = cerrarHandler;
        this.archivarHandler = archivarHandler;
        this.buscarHandler = buscarHandler;
    }

    @PostMapping
    public ResponseEntity<Reto> publicarReto(
            @RequestHeader("X-User-Id") String empresaId,
            @Valid @RequestBody PublicarRetoRequest request) {
        PublicarRetoCommand command = new PublicarRetoCommand(
                empresaId, request.titulo(), request.descripcion(), request.categoria(),
                request.requisitos(), request.entregables(), request.tipoRecompensa(),
                request.montoRecompensa(), request.fechaLimite(), request.nivelDificultad(), request.cuposDisponibles());
        Reto reto = publicarHandler.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(reto);
    }

    @PostMapping("/{id}/duplicar")
    public ResponseEntity<Reto> duplicarReto(@PathVariable String id) {
        Reto reto = duplicarHandler.handle(new DuplicarRetoCommand(id));
        return ResponseEntity.status(HttpStatus.CREATED).body(reto);
    }

    @PostMapping("/{id}/cerrar")
    public ResponseEntity<Reto> cerrarReto(@PathVariable String id) {
        Reto reto = cerrarHandler.handle(new CerrarRetoCommand(id));
        return ResponseEntity.ok(reto);
    }

    @PostMapping("/{id}/archivar")
    public ResponseEntity<Void> archivarReto(@PathVariable String id) {
        archivarHandler.handle(new ArchivarRetoCommand(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Reto>> buscarRetos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String nivelDificultad,
            @RequestParam(required = false) String empresaId,
            @RequestParam(required = false) String estado) {
        List<Reto> retos = buscarHandler.handle(new BuscarRetosQuery(categoria, nivelDificultad, empresaId, estado));
        return ResponseEntity.ok(retos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reto> obtenerReto(@PathVariable String id) {
        return ResponseEntity.ok(buscarHandler.handle(new BuscarRetosQuery(null, null, null, null))
                .stream().filter(r -> r.getId().equals(id)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Reto no encontrado")));
    }
}
