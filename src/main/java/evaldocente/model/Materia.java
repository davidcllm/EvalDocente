package evaldocente.model;

public class Materia {
    private String id;
    private String nombre;
    private String carrera;
    private int semestre;
    private String docenteId;

    public Materia(String id, String nombre, String carrera, int semestre, String docenteId) {
        this.id = id;
        this.nombre = nombre;
        this.carrera = carrera;
        this.semestre = semestre;
        this.docenteId = docenteId;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCarrera() { return carrera; }
    public int getSemestre() { return semestre; }
    public String getDocenteId() { return docenteId; }
    public void setDocenteId(String docenteId) { this.docenteId = docenteId; }

    @Override
    public String toString() {
        return nombre + " (Carrera: " + carrera + ", Semestre: " + semestre + ")";
    }
}
