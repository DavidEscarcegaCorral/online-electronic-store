package entidades;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioEntidad {
    private ObjectId id;
    private String nombre;
    private String email;
    private LocalDateTime fechaCreacion;
    private List<MetodoPagoRegistrado> metodosPago;

    public UsuarioEntidad() {
        this.fechaCreacion = LocalDateTime.now();
        this.metodosPago = new ArrayList<>();
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

    public List<MetodoPagoRegistrado> getMetodosPago() {
        return metodosPago;
    }

    public void setMetodosPago(List<MetodoPagoRegistrado> metodosPago) {
        this.metodosPago = metodosPago;
    }

    public void agregarMetodoPago(MetodoPagoRegistrado metodoPago) {
        if (this.metodosPago == null) {
            this.metodosPago = new ArrayList<>();
        }
        this.metodosPago.add(metodoPago);
    }

    public static class MetodoPagoRegistrado {
        private String id;
        private String tipo;
        private String alias;
        private String ultimos4Digitos;
        private String nombreTitular;
        private boolean esPredeterminado;

        public MetodoPagoRegistrado() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getUltimos4Digitos() {
            return ultimos4Digitos;
        }

        public void setUltimos4Digitos(String ultimos4Digitos) {
            this.ultimos4Digitos = ultimos4Digitos;
        }

        public String getNombreTitular() {
            return nombreTitular;
        }

        public void setNombreTitular(String nombreTitular) {
            this.nombreTitular = nombreTitular;
        }

        public boolean isEsPredeterminado() {
            return esPredeterminado;
        }

        public void setEsPredeterminado(boolean esPredeterminado) {
            this.esPredeterminado = esPredeterminado;
        }
    }
}

