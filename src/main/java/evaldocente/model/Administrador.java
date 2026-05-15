package evaldocente.model;

public class Administrador extends Usuario {

    public Administrador(String id, String nombre, String password) {
        super(id, nombre, password);
    }

    @Override
    public String getRol() { return "ADMINISTRADOR"; }
}
