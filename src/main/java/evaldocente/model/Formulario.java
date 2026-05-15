package evaldocente.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Formulario {
    public enum EstadoFormulario {
        ACTIVO, CERRADO, PENDIENTE
    }

    private String id;
    private String titulo;
    private String periodo;        // Ej: "2025-1"
    private String docenteId;
    private String materiaId;
    private List<Pregunta> preguntas;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private EstadoFormulario estado;
    private int totalRespuestas;

    public Formulario(String id, String titulo, String periodo, String docenteId,
                      String materiaId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.id = id;
        this.titulo = titulo;
        this.periodo = periodo;
        this.docenteId = docenteId;
        this.materiaId = materiaId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.preguntas = new ArrayList<>();
        this.totalRespuestas = 0;
        actualizarEstado();
    }

    public void actualizarEstado() {
        LocalDateTime ahora = LocalDateTime.now();
        if (ahora.isBefore(fechaInicio)) {
            this.estado = EstadoFormulario.PENDIENTE;
        } else if (ahora.isAfter(fechaFin)) {
            this.estado = EstadoFormulario.CERRADO;
        } else {
            this.estado = EstadoFormulario.ACTIVO;
        }
    }

    public boolean estaActivo() {
        actualizarEstado();
        return estado == EstadoFormulario.ACTIVO;
    }

    public void agregarPregunta(Pregunta p) { preguntas.add(p); }
    public void incrementarRespuestas() { totalRespuestas++; }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getPeriodo() { return periodo; }
    public String getDocenteId() { return docenteId; }
    public String getMateriaId() { return materiaId; }
    public List<Pregunta> getPreguntas() { return preguntas; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public EstadoFormulario getEstado() { actualizarEstado(); return estado; }
    public int getTotalRespuestas() { return totalRespuestas; }

    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    @Override
    public String toString() {
        actualizarEstado();
        return String.format("[%s] %s | Periodo: %s | Respuestas: %d",
                estado, titulo, periodo, totalRespuestas);
    }
}
