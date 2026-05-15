package evaldocente.pattern;

import evaldocente.model.Respuesta;

/**
 * Observador que registra en la bitácora del sistema cada nueva evaluación recibida.
 */
public class BitacoraObserver implements Observer {

    private static final java.util.List<String> bitacora = new java.util.ArrayList<>();

    @Override
    public void actualizar(String evento, Object datos) {
        String entrada;
        if ("NUEVA_RESPUESTA".equals(evento) && datos instanceof Respuesta) {
            Respuesta r = (Respuesta) datos;
            entrada = String.format("[%s] Nueva evaluación registrada | Formulario: %s | Docente: %s | Materia: %s",
                    java.time.LocalDateTime.now().toString().substring(0, 19),
                    r.getFormularioId(), r.getDocenteId(), r.getMateriaId());
        } else {
            entrada = String.format("[%s] Evento: %s | Datos: %s",
                    java.time.LocalDateTime.now().toString().substring(0, 19), evento, datos);
        }
        bitacora.add(entrada);
    }

    public static java.util.List<String> getBitacora() { return bitacora; }
}
