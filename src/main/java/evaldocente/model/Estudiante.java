package evaldocente.model;

import java.util.ArrayList;
import java.util.List;

public class Estudiante extends Usuario {
    private String matricula;
    private String carrera;
    private int semestre;
    private List<String> evaluacionesRealizadas; // IDs de evaluaciones ya contestadas

    public Estudiante(String id, String nombre, String password, String matricula, String carrera, int semestre) {
        super(id, nombre, password);
        this.matricula = matricula;
        this.carrera = carrera;
        this.semestre = semestre;
        this.evaluacionesRealizadas = new ArrayList<>();
    }

    @Override
    public String getRol() { return "ESTUDIANTE"; }

    public String getMatricula() { return matricula; }
    public String getCarrera() { return carrera; }
    public int getSemestre() { return semestre; }
    public List<String> getEvaluacionesRealizadas() { return evaluacionesRealizadas; }

    public void agregarEvaluacionRealizada(String evaluacionId) {
        evaluacionesRealizadas.add(evaluacionId);
    }

    public boolean yaEvaluo(String formularioId, String docenteId) {
        String clave = formularioId + "_" + docenteId;
        return evaluacionesRealizadas.contains(clave);
    }
}
