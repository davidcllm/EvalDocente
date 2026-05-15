package evaldocente.pattern;

/**
 * PATRÓN OBSERVER
 * Permite notificar automáticamente a los observadores cuando se registra
 * una nueva respuesta o cuando cambia el estado de un formulario.
 */
public interface Observer {
    void actualizar(String evento, Object datos);
}
