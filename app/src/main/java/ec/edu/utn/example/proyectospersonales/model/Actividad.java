package ec.edu.utn.example.proyectospersonales.model;

public class Actividad {
    private int idActividad;
    private int idProyecto;
    private String nombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String estado;

    // Constructor para nueva actividad
    public Actividad(int idProyecto, String nombre, String descripcion, String fechaInicio, String fechaFin, String estado) {
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    // Constructor para actividad existente
    public Actividad(int idActividad, int idProyecto, String nombre, String descripcion, String fechaInicio, String fechaFin, String estado) {
        this.idActividad = idActividad;
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método para calcular el progreso de la actividad
    public int getProgreso() {
        switch (estado) {
            case "Planificado":
                return 0;
            case "En ejecución":
                return 50;
            case "Realizado":
                return 100;
            default:
                return 0; // En caso de estado inválido
        }
    }
}