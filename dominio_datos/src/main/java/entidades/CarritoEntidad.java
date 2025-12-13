package entidades;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

    public void agregarConfiguracion(ObjectId configuracionId) {
        if (this.configuracionesIds == null) {
            this.configuracionesIds = new ArrayList<>();
        }
        this.configuracionesIds.add(configuracionId);
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void agregarProducto(String productoId, int cantidad) {
        if (this.productos == null) {
            this.productos = new ArrayList<>();
        }

        Map<String, Object> productoExistente = buscarProducto(productoId);
        if (productoExistente != null) {
            int cantidadActual = (Integer) productoExistente.get("cantidad");
            productoExistente.put("cantidad", cantidadActual + cantidad);
        } else {
            Map<String, Object> nuevoProducto = new HashMap<>();
            nuevoProducto.put("productoId", productoId);
            nuevoProducto.put("cantidad", cantidad);
            this.productos.add(nuevoProducto);
        }

        this.fechaActualizacion = LocalDateTime.now();
    }

    private Map<String, Object> buscarProducto(String productoId) {
        if (this.productos == null) {
            return null;
        }

        for (Map<String, Object> producto : this.productos) {
            if (productoId.equals(producto.get("productoId"))) {
                return producto;
            }
        }
        return null;
    }
}

