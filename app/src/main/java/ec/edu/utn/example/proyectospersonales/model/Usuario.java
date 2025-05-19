package ec.edu.utn.example.proyectospersonales.model;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String contrasena;
    private String correo;

    public Usuario(int idUsuario, String nombreUsuario, String contrasena, String correo) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.correo = correo;
    }

    public Usuario(String nombreUsuario, String contrasena, String correo) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.correo = correo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}