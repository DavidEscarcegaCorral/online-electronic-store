package entidades;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CarritoEntidad {
    private ObjectId id;
    private String clienteId;
    private List<ObjectId> configuracionesIds;
    private LocalDateTime fechaActualizacion;

    public CarritoEntidad() {
        this.configuracionesIds = new ArrayList<>();
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

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public void agregarConfiguracion(ObjectId configuracionId) {
        if (this.configuracionesIds == null) {
            this.configuracionesIds = new ArrayList<>();
        }
        this.configuracionesIds.add(configuracionId);
        this.fechaActualizacion = LocalDateTime.now();
    }
}

