package entidades;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;

public class UsuarioEntidad {
    private ObjectId id;
    private String nombre;
    private String email;
    private LocalDateTime fechaCreacion;

    public UsuarioEntidad() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}

