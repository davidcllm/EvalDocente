package evaldocente.model;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class Respuesta {
    private String id;
    private String formularioId;
    private String docenteId;
    private String materiaId;
    private String carreraEstudiante;
    private int semestreEstudiante;
    // Mapa preguntaId -> valor (Integer para escala, String para texto)
    private Map<String, Object> respuestas;
    private LocalDateTime fechaRespuesta;

    public Respuesta(String id, String formularioId, String docenteId, String materiaId,
                     String carreraEstudiante, int semestreEstudiante) {
        this.id = id;
        this.formularioId = formularioId;
        this.docenteId = docenteId;
        this.materiaId = materiaId;
        this.carreraEstudiante = carreraEstudiante;
        this.semestreEstudiante = semestreEstudiante;
        this.respuestas = new LinkedHashMap<>();
        this.fechaRespuesta = LocalDateTime.now();
    }

    public void agregarRespuesta(String preguntaId, Object valor) {
        respuestas.put(preguntaId, valor);
    }

    public String getId() { return id; }
    public String getFormularioId() { return formularioId; }
    public String getDocenteId() { return docenteId; }
    public String getMateriaId() { return materiaId; }
    public String getCarreraEstudiante() { return carreraEstudiante; }
    public int getSemestreEstudiante() { return semestreEstudiante; }
    public Map<String, Object> getRespuestas() { return respuestas; }
    public LocalDateTime getFechaRespuesta() { return fechaRespuesta; }
}
