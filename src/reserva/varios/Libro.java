package reserva.varios;

public class Libro {

    private String categoria;
    private String codigo;
    private String nombre;
    private String autor;
    private String editorial;
    private String materia;
    private int añoEdicion;
    private int numeroCopias;
    private int numeroPrestamos;
    private int numeroDisponibles;

    public Libro(String categoria, String codigo, String nombre, String autor, String editorial, String materia, int añoEdicion, int numeroCopias, int numeroDisponibles) {
        this.categoria = categoria;
        this.codigo = codigo;
        this.nombre = nombre;
        this.autor = autor;
        this.editorial = editorial;
        this.materia = materia;
        this.añoEdicion = añoEdicion;
        this.numeroCopias = numeroCopias;
        this.numeroPrestamos = 0;
        this.numeroDisponibles = numeroDisponibles;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAutor() {
        return autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public String getMateria() {
        return materia;
    }

    public int getAñoEdicion() {
        return añoEdicion;
    }

    public int getNumeroCopias() {
        return numeroCopias;
    }

    public int getNumeroPrestamos() {
        return numeroPrestamos;
    }

    public int getNumeroDisponibles() {
        return numeroDisponibles;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public void setAñoEdicion(int añoEdicion) {
        this.añoEdicion = añoEdicion;
    }

    public void setNumeroCopias(int numeroCopias) {
        this.numeroCopias = numeroCopias;
    }

    public void setNumeroPrestamos(int numeroPrestamos) {
        this.numeroPrestamos = numeroPrestamos;
    }

    public void setNumeroDisponibles(int numeroDisponibles) {
        this.numeroDisponibles = numeroDisponibles;
    }

    @Override
    public String toString() {
        return "_______________________________________________________________________"
                + "\n" + "Nombre: " + nombre + "\n"
                + "Autor: " + autor + "\n"
                + "Categoria: " + categoria + "\n"
                + "Código: " + codigo + "\n"
                + "Editorial: " + editorial + "\n"
                + "Materia: " + materia + "\n"
                + "Año de edición: " + añoEdicion + "\n"
                + "Número de copias: " + numeroCopias + "\n"
                + "Número de prestamos: " + numeroPrestamos + "\n"
                + "Número disponibles: " + numeroDisponibles + "\n";
    }
}
