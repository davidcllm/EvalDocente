package evaldocente.pattern;

public interface Observable {
    void agregarObserver(Observer observer);
    void eliminarObserver(Observer observer);
    void notificarObservers(String evento, Object datos);
}
