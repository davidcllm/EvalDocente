package evaldocente.pattern;

import evaldocente.model.Respuesta;
import java.util.List;

/**
 * PATRÓN STRATEGY
 * Permite intercambiar el algoritmo de generación de reportes en tiempo de ejecución.
 * Cada estrategia implementa una forma distinta de calcular/presentar el reporte.
 */
public interface EstrategiaReporte {
    String generarReporte(List<Respuesta> respuestas, String parametro);
}
