package ec.edu.utn.example.proyectospersonales.model;

public class Proyecto {
    private int idProyecto;
    private int idUsuario;
    private String nombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;

    public Proyecto(int idProyecto, int idUsuario, String nombre, String descripcion,
                    String fechaInicio, String fechaFin) {
        this.idProyecto = idProyecto;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Proyecto(int idUsuario, String nombre, String descripcion,
                    String fechaInicio, String fechaFin) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
}