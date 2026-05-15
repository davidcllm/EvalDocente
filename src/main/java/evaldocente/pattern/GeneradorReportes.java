package evaldocente.pattern;

import evaldocente.model.Respuesta;
import evaldocente.repository.DataRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * PATRÓN STRATEGY - Contexto y estrategias concretas de generación de reportes.
 * Permite intercambiar el algoritmo en tiempo de ejecución sin cambiar el cliente.
 */
public class GeneradorReportes {

    public static final String POR_DOCENTE  = "DOCENTE";
    public static final String POR_MATERIA  = "MATERIA";
    public static final String POR_CARRERA  = "CARRERA";
    public static final String POR_SEMESTRE = "SEMESTRE";

    private EstrategiaReporte estrategia;

    public void setEstrategia(String tipo) {
        switch (tipo.toUpperCase()) {
            case POR_DOCENTE:   estrategia = new ReporteDocenteEstrategia();   break;
            case POR_MATERIA:   estrategia = new ReporteMateriaEstrategia();   break;
            case POR_CARRERA:   estrategia = new ReporteCarreraEstrategia();   break;
            case POR_SEMESTRE:  estrategia = new ReporteSemestreEstrategia();  break;
            default: throw new IllegalArgumentException("Estrategia desconocida: " + tipo);
        }
    }

    public String generar(String parametro) {
        if (estrategia == null) throw new IllegalStateException("Estrategia no configurada.");
        return estrategia.generarReporte(DataRepository.getInstance().todasRespuestas(), parametro);
    }

    // ─── Clase base con utilidades compartidas ───────────────────────────────

    static abstract class BaseEstrategia implements EstrategiaReporte {

        protected String construirReporte(String titulo, List<Respuesta> filtradas) {
            if (filtradas.isEmpty()) {
                return titulo + "\n" + "=".repeat(60) + "\nSin respuestas registradas.\n";
            }
            DataRepository repo = DataRepository.getInstance();
            Map<String, List<Integer>> escalasPorPreg = new LinkedHashMap<>();
            Map<String, List<String>> comentariosPorPreg = new LinkedHashMap<>();

            for (Respuesta r : filtradas) {
                r.getRespuestas().forEach((pregId, valor) -> {
                    if (valor instanceof Integer) {
                        escalasPorPreg.computeIfAbsent(pregId, k -> new ArrayList<>()).add((Integer) valor);
                    } else if (valor instanceof String) {
                        comentariosPorPreg.computeIfAbsent(pregId, k -> new ArrayList<>()).add((String) valor);
                    }
                });
            }

            StringBuilder sb = new StringBuilder();
            sb.append("\n").append("=".repeat(60)).append("\n");
            sb.append("  ").append(titulo).append("\n");
            sb.append("=".repeat(60)).append("\n");
            sb.append(String.format("  Total de evaluaciones: %d\n\n", filtradas.size()));

            if (!escalasPorPreg.isEmpty()) {
                sb.append("  RESULTADOS DE ESCALA (1-5):\n");
                sb.append("  ").append("-".repeat(56)).append("\n");
                double suma = 0; int cnt = 0;
                for (Map.Entry<String, List<Integer>> e : escalasPorPreg.entrySet()) {
                    List<Integer> vals = e.getValue();
                    double prom = vals.stream().mapToInt(i -> i).average().orElse(0);
                    suma += prom; cnt++;
                    String texto = repo.buscarPregunta(e.getKey()).map(p -> p.getTexto()).orElse(e.getKey());
                    sb.append(String.format("  %-42s %s %.2f\n", trunc(texto, 42), barra(prom), prom));
                }
                if (cnt > 0) sb.append(String.format("\n  Promedio general: %.2f / 5.00\n", suma / cnt));
            }

            if (!comentariosPorPreg.isEmpty()) {
                sb.append("\n  COMENTARIOS ABIERTOS:\n");
                sb.append("  ").append("-".repeat(56)).append("\n");
                comentariosPorPreg.forEach((pregId, coms) -> {
                    String texto = repo.buscarPregunta(pregId).map(p -> p.getTexto()).orElse(pregId);
                    sb.append("  >> ").append(texto).append("\n");
                    coms.forEach(c -> sb.append("     - ").append(c).append("\n"));
                    sb.append("\n");
                });
            }
            sb.append("=".repeat(60)).append("\n");
            return sb.toString();
        }

        private String barra(double p) {
            int llenos = (int) Math.round(p);
            StringBuilder b = new StringBuilder("[");
            for (int i = 1; i <= 5; i++) b.append(i <= llenos ? "█" : "░");
            return b.append("]").toString();
        }

        private String trunc(String s, int max) {
            return s.length() > max ? s.substring(0, max - 3) + "..." : s;
        }
    }

    // ─── Estrategias concretas ────────────────────────────────────────────────

    static class ReporteDocenteEstrategia extends BaseEstrategia {
        @Override
        public String generarReporte(List<Respuesta> respuestas, String docenteId) {
            String nombre = DataRepository.getInstance().buscarDocente(docenteId)
                    .map(d -> d.getNombre()).orElse("Desconocido");
            List<Respuesta> f = respuestas.stream()
                    .filter(r -> r.getDocenteId().equals(docenteId)).collect(Collectors.toList());
            return construirReporte("REPORTE POR DOCENTE: " + nombre, f);
        }
    }

    static class ReporteMateriaEstrategia extends BaseEstrategia {
        @Override
        public String generarReporte(List<Respuesta> respuestas, String materiaId) {
            String nombre = DataRepository.getInstance().buscarMateria(materiaId)
                    .map(m -> m.getNombre()).orElse("Desconocida");
            List<Respuesta> f = respuestas.stream()
                    .filter(r -> r.getMateriaId().equals(materiaId)).collect(Collectors.toList());
            return construirReporte("REPORTE POR MATERIA: " + nombre, f);
        }
    }

    static class ReporteCarreraEstrategia extends BaseEstrategia {
        @Override
        public String generarReporte(List<Respuesta> respuestas, String carrera) {
            List<Respuesta> f = respuestas.stream()
                    .filter(r -> r.getCarreraEstudiante().equalsIgnoreCase(carrera))
                    .collect(Collectors.toList());
            return construirReporte("REPORTE POR CARRERA: " + carrera, f);
        }
    }

    static class ReporteSemestreEstrategia extends BaseEstrategia {
        @Override
        public String generarReporte(List<Respuesta> respuestas, String semestre) {
            int sem = Integer.parseInt(semestre);
            List<Respuesta> f = respuestas.stream()
                    .filter(r -> r.getSemestreEstudiante() == sem).collect(Collectors.toList());
            return construirReporte("REPORTE POR SEMESTRE: " + semestre, f);
        }
    }
}
