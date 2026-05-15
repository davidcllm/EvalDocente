package evaldocente.model;

import java.util.ArrayList;
import java.util.List;

public class Docente {
    private String id;
    private String nombre;
    private String departamento;
    private List<String> materiasIds;

    public Docente(String id, String nombre, String departamento) {
        this.id = id;
        this.nombre = nombre;
        this.departamento = departamento;
        this.materiasIds = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDepartamento() { return departamento; }
    public List<String> getMateriasIds() { return materiasIds; }

    public void agregarMateria(String materiaId) {
        if (!materiasIds.contains(materiaId)) materiasIds.add(materiaId);
    }

    @Override
    public String toString() {
        return nombre + " (ID: " + id + ", Depto: " + departamento + ")";
    }
}
