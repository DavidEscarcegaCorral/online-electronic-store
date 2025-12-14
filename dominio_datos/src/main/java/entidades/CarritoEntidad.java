package entidades;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarritoEntidad {
    private ObjectId id;
    private String clienteId;
    private List<ObjectId> configuracionesIds;
    private List<Map<String, Object>> productos;
    private LocalDateTime fechaActualizacion;

    public CarritoEntidad() {
        this.configuracionesIds = new ArrayList<>();
        this.productos = new ArrayList<>();
        this.fechaActualizacion = LocalDateTime.now();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public List<ObjectId> getConfiguracionesIds() {
        return configuracionesIds;
    }

    public void setConfiguracionesIds(List<ObjectId> configuracionesIds) {
        this.configuracionesIds = configuracionesIds;
    }

    public List<Map<String, Object>> getProductos() {
        return productos;
    }

    public void setProductos(List<Map<String, Object>> productos) {
        this.productos = productos;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}

