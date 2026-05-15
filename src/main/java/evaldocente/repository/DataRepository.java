package evaldocente.repository;

import evaldocente.model.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repositorio en memoria (patrón Singleton).
 * Centraliza el almacenamiento de todas las entidades del sistema.
 */
public class DataRepository {

    private static DataRepository instance;

    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();
    private final Map<String, Docente> docentes = new LinkedHashMap<>();
    private final Map<String, Materia> materias = new LinkedHashMap<>();
    private final Map<String, Formulario> formularios = new LinkedHashMap<>();
    private final Map<String, Pregunta> preguntas = new LinkedHashMap<>();
    private final List<Respuesta> respuestas = new ArrayList<>();

    private DataRepository() {}

    public static DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    // --- Usuarios ---
    public void guardarUsuario(Usuario u) { usuarios.put(u.getId(), u); }
    public Optional<Usuario> buscarUsuario(String id) { return Optional.ofNullable(usuarios.get(id)); }
    public Optional<Usuario> autenticar(String id, String password) {
        return usuarios.values().stream()
                .filter(u -> u.getId().equals(id) && u.getPassword().equals(password))
                .findFirst();
    }
    public Collection<Usuario> todosUsuarios() { return usuarios.values(); }

    // --- Docentes ---
    public void guardarDocente(Docente d) { docentes.put(d.getId(), d); }
    public Optional<Docente> buscarDocente(String id) { return Optional.ofNullable(docentes.get(id)); }
    public Collection<Docente> todosDocentes() { return docentes.values(); }

    // --- Materias ---
    public void guardarMateria(Materia m) { materias.put(m.getId(), m); }
    public Optional<Materia> buscarMateria(String id) { return Optional.ofNullable(materias.get(id)); }
    public Collection<Materia> todasMaterias() { return materias.values(); }
    public List<Materia> materiasPorDocente(String docenteId) {
        return materias.values().stream()
                .filter(m -> docenteId.equals(m.getDocenteId()))
                .collect(Collectors.toList());
    }
    public List<Materia> materiasPorCarreraYSemestre(String carrera, int semestre) {
        return materias.values().stream()
                .filter(m -> m.getCarrera().equalsIgnoreCase(carrera) && m.getSemestre() == semestre)
                .collect(Collectors.toList());
    }

    // --- Formularios ---
    public void guardarFormulario(Formulario f) { formularios.put(f.getId(), f); }
    public Optional<Formulario> buscarFormulario(String id) { return Optional.ofNullable(formularios.get(id)); }
    public Collection<Formulario> todosFormularios() { return formularios.values(); }
    public List<Formulario> formulariosActivos() {
        return formularios.values().stream()
                .filter(Formulario::estaActivo)
                .collect(Collectors.toList());
    }
    public List<Formulario> formulariosParaEstudiante(String carrera, int semestre) {
        return formulariosActivos().stream()
                .filter(f -> {
                    Optional<Materia> m = buscarMateria(f.getMateriaId());
                    return m.map(mat -> mat.getCarrera().equalsIgnoreCase(carrera)
                            && mat.getSemestre() == semestre).orElse(false);
                })
                .collect(Collectors.toList());
    }

    // --- Preguntas ---
    public void guardarPregunta(Pregunta p) { preguntas.put(p.getId(), p); }
    public Optional<Pregunta> buscarPregunta(String id) { return Optional.ofNullable(preguntas.get(id)); }
    public Collection<Pregunta> todasPreguntas() { return preguntas.values(); }

    // --- Respuestas ---
    public void guardarRespuesta(Respuesta r) { respuestas.add(r); }
    public List<Respuesta> todasRespuestas() { return respuestas; }
    public List<Respuesta> respuestasPorFormulario(String formularioId) {
        return respuestas.stream()
                .filter(r -> r.getFormularioId().equals(formularioId))
                .collect(Collectors.toList());
    }
    public List<Respuesta> respuestasPorDocente(String docenteId) {
        return respuestas.stream()
                .filter(r -> r.getDocenteId().equals(docenteId))
                .collect(Collectors.toList());
    }
    public List<Respuesta> respuestasPorMateria(String materiaId) {
        return respuestas.stream()
                .filter(r -> r.getMateriaId().equals(materiaId))
                .collect(Collectors.toList());
    }
    public List<Respuesta> respuestasPorCarrera(String carrera) {
        return respuestas.stream()
                .filter(r -> r.getCarreraEstudiante().equalsIgnoreCase(carrera))
                .collect(Collectors.toList());
    }
    public List<Respuesta> respuestasPorSemestre(int semestre) {
        return respuestas.stream()
                .filter(r -> r.getSemestreEstudiante() == semestre)
                .collect(Collectors.toList());
    }
}
