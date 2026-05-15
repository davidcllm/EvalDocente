package evaldocente.model;

public class Pregunta {
    public enum TipoPregunta {
        ESCALA,        // 1-5
        TEXTO_LIBRE    // Comentario abierto
    }

    private String id;
    private String texto;
    private TipoPregunta tipo;
    private String categoria; // "DOCENTE" o "CURSO"

    public Pregunta(String id, String texto, TipoPregunta tipo, String categoria) {
        this.id = id;
        this.texto = texto;
        this.tipo = tipo;
        this.categoria = categoria;
    }

    public String getId() { return id; }
    public String getTexto() { return texto; }
    public TipoPregunta getTipo() { return tipo; }
    public String getCategoria() { return categoria; }

    @Override
    public String toString() {
        String tipoStr = tipo == TipoPregunta.ESCALA ? "[Escala 1-5]" : "[Comentario]";
        return tipoStr + " " + texto;
    }
}
