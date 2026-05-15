package evaldocente.model;

import java.time.LocalDateTime;

public abstract class Usuario {
    protected String id;
    protected String nombre;
    protected String password;
    protected LocalDateTime fechaCreacion;

    public Usuario(String id, String nombre, String password) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
        this.fechaCreacion = LocalDateTime.now();
    }

    public abstract String getRol();

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getPassword() { return password; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    @Override
    public String toString() {
        return "[" + getRol() + "] " + nombre + " (ID: " + id + ")";
    }
}
