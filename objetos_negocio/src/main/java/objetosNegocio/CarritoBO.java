package objetosNegocio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Business Object para Carrito de Compras.
 * Representa la lógica de negocio pura del carrito.
 */
public class CarritoBO implements ICarritoBO {
    private String id;
    private String clienteId;
    private List<ItemCarritoBO> productos;
    private List<String> configuracionesIds;

    public CarritoBO() {
        this.productos = new ArrayList<>();
        this.configuracionesIds = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemCarritoBO> getProductos() {
        return productos;
    }

    public void setProductos(List<ItemCarritoBO> productos) {
        this.productos = productos != null ? productos : new ArrayList<>();
    }

    public List<String> getConfiguracionesIds() {
        return configuracionesIds;
    }

    public void setConfiguracionesIds(List<String> configuracionesIds) {
        this.configuracionesIds = configuracionesIds != null ? configuracionesIds : new ArrayList<>();
    }

    public boolean estaVacio() {
        return (productos == null || productos.isEmpty()) &&
                (configuracionesIds == null || configuracionesIds.isEmpty());
    }

    public int getCantidadProductos() {
        if (productos == null) return 0;

        int total = 0;
        for (ItemCarritoBO producto : productos) {
            total += producto.getCantidad();
        }
        return total;
    }

    public int getCantidadConfiguraciones() {
        return configuracionesIds != null ? configuracionesIds.size() : 0;
    }

    public int getCantidadTotalItems() {
        return getCantidadProductos() + getCantidadConfiguraciones();
    }

    public BigDecimal calcularSubtotal() {
        if (productos == null || productos.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        for (ItemCarritoBO producto : productos) {
            subtotal = subtotal.add(producto.getSubtotal());
        }
        return subtotal;
    }

    public void agregarProducto(String productoId, String nombre, BigDecimal precio, int cantidad) {
        if (productos == null) {
            productos = new ArrayList<>();
        }

        ItemCarritoBO productoExistente = buscarProductoPorId(productoId);

        if (productoExistente != null) {
            productoExistente.setCantidad(productoExistente.getCantidad() + cantidad);
        } else {
            ItemCarritoBO nuevoProducto = new ItemCarritoBO();
            nuevoProducto.setProductoId(productoId);
            nuevoProducto.setNombre(nombre);
            nuevoProducto.setPrecio(precio);
            nuevoProducto.setCantidad(cantidad);
            productos.add(nuevoProducto);
        }
    }

    public void removerProducto(String productoId) {
        if (productos != null) {
            productos.removeIf(p -> productoId.equals(p.getProductoId()));
        }
    }

    public void actualizarCantidadProducto(String productoId, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            removerProducto(productoId);
            return;
        }

        ItemCarritoBO producto = buscarProductoPorId(productoId);
        if (producto != null) {
            producto.setCantidad(nuevaCantidad);
        }
    }

    public void agregarConfiguracion(String configuracionId) {
        if (configuracionesIds == null) {
            configuracionesIds = new ArrayList<>();
        }

        if (!configuracionesIds.contains(configuracionId)) {
            configuracionesIds.add(configuracionId);
        }
    }

    public void removerConfiguracion(String configuracionId) {
        if (configuracionesIds != null) {
            configuracionesIds.remove(configuracionId);
        }
    }

    public void vaciar() {
        if (productos != null) {
            productos.clear();
        }
        if (configuracionesIds != null) {
            configuracionesIds.clear();
        }
    }

    public boolean tieneProducto(String productoId) {
        return buscarProductoPorId(productoId) != null;
    }

    public boolean tieneConfiguracion(String configuracionId) {
        return configuracionesIds != null && configuracionesIds.contains(configuracionId);
    }

    private ItemCarritoBO buscarProductoPorId(String productoId) {
        if (productos == null) return null;

        for (ItemCarritoBO producto : productos) {
            if (productoId.equals(producto.getProductoId())) {
                return producto;
            }
        }
        return null;
    }

    /**
     * Clase interna para representar un item del carrito.
     */
    public static class ItemCarritoBO {
        private String productoId;
        private String nombre;
        private BigDecimal precio;
        private int cantidad;

        public ItemCarritoBO() {
        }


        public String getProductoId() {
            return productoId;
        }

        public void setProductoId(String productoId) {
            this.productoId = productoId;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public BigDecimal getPrecio() {
            return precio;
        }

        public void setPrecio(BigDecimal precio) {
            this.precio = precio;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }

        public BigDecimal getSubtotal() {
            if (precio == null) return BigDecimal.ZERO;
            return precio.multiply(BigDecimal.valueOf(cantidad));
        }
    }
}

