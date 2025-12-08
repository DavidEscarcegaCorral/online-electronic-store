package fachada;

import dao.IProductoDAO;
import dao.ProductoDAO;
import dao.IPedidoDAO;
import dao.PedidoDAO;
import dto.CarritoDTO;
import dto.ItemCarritoDTO;
import dto.MetodoPagoDTO;
import entidades.PedidoEntidad;
import entidades.ProductoEntidad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementación de la fachada para el subsistema de Venta.
 * Gestiona el carrito de compras y el proceso de pago (mock).
 */
public class VentaFacade implements IVentaFacade {
    private static VentaFacade instancia;
    private final IProductoDAO productoDAO;
    private final IPedidoDAO pedidoDAO;
    private CarritoDTO carritoActual;

    private VentaFacade() {
        this.productoDAO = new ProductoDAO();
        this.pedidoDAO = new PedidoDAO();
    }

    public static synchronized VentaFacade getInstance() {
        if (instancia == null) {
            instancia = new VentaFacade();
        }
        return instancia;
    }

    @Override
    public CarritoDTO crearCarrito(String clienteId) {
        this.carritoActual = new CarritoDTO();
        this.carritoActual.setClienteId(clienteId);
        this.carritoActual.setItems(new ArrayList<>());
        return this.carritoActual;
    }

    @Override
    public CarritoDTO getCarritoActual() {
        return this.carritoActual;
    }

    @Override
    public boolean agregarItemAlCarrito(ItemCarritoDTO item) {
        if (this.carritoActual == null) {
            return false;
        }

        // Verificar stock disponible
        ProductoEntidad producto = productoDAO.obtenerPorId(item.getProductoId());
        if (producto == null || producto.getStock() < item.getCantidad()) {
            return false;
        }

        // Verificar si el item ya está en el carrito
        ItemCarritoDTO itemExistente = buscarItemEnCarrito(item.getProductoId());
        if (itemExistente != null) {
            int nuevaCantidad = itemExistente.getCantidad() + item.getCantidad();
            if (nuevaCantidad > producto.getStock()) {
                return false;
            }
            itemExistente.setCantidad(nuevaCantidad);
        } else {
            this.carritoActual.getItems().add(item);
        }

        return true;
    }

    @Override
    public void removerItemDelCarrito(String productoId) {
        if (this.carritoActual != null) {
            this.carritoActual.getItems().removeIf(item ->
                    item.getProductoId().equals(productoId));
        }
    }

    @Override
    public boolean actualizarCantidadItem(String productoId, int nuevaCantidad) {
        if (this.carritoActual == null) {
            return false;
        }

        if (nuevaCantidad <= 0) {
            removerItemDelCarrito(productoId);
            return true;
        }

        // Verificar stock
        ProductoEntidad producto = productoDAO.obtenerPorId(productoId);
        if (producto == null || producto.getStock() < nuevaCantidad) {
            return false;
        }

        ItemCarritoDTO item = buscarItemEnCarrito(productoId);
        if (item != null) {
            item.setCantidad(nuevaCantidad);
            return true;
        }

        return false;
    }

    @Override
    public double calcularTotalCarrito() {
        if (this.carritoActual == null || this.carritoActual.getItems().isEmpty()) {
            return 0.0;
        }

        return this.carritoActual.getItems().stream()
                .mapToDouble(item -> item.getPrecioUnitario() * item.getCantidad())
                .sum();
    }

    @Override
    public String realizarPago(MetodoPagoDTO metodoPago) {
        if (this.carritoActual == null || this.carritoActual.getItems().isEmpty()) {
            return null;
        }

        // Verificar stock antes de procesar
        if (!verificarStockCarrito()) {
            return null;
        }

        // Crear el pedido
        PedidoEntidad pedido = new PedidoEntidad();
        pedido.setClienteId(this.carritoActual.getClienteId());
        pedido.setTotal(calcularTotalCarrito());
        pedido.setEstado("PROCESANDO");
        pedido.setFechaCreacion(new Date());

        // Metodo de pago
        PedidoEntidad.MetodoPagoInfo metodoPagoInfo = new PedidoEntidad.MetodoPagoInfo();
        metodoPagoInfo.setTipo(metodoPago.getTipo().toString());
        metodoPagoInfo.setDetalles("Mock - Pago procesado exitosamente");
        pedido.setMetodoPago(metodoPagoInfo);

        // Gestionar items del carrito
        List<PedidoEntidad.ItemPedido> itemsPedido = new ArrayList<>();
        for (ItemCarritoDTO item : this.carritoActual.getItems()) {
            PedidoEntidad.ItemPedido itemPedido = new PedidoEntidad.ItemPedido();
            itemPedido.setProductoId(item.getProductoId());
            itemPedido.setNombre(item.getNombre());
            itemPedido.setCantidad(item.getCantidad());
            itemPedido.setPrecioUnitario(item.getPrecioUnitario());
            itemsPedido.add(itemPedido);
        }
        pedido.setItems(itemsPedido);

        // Guardar el pedido
        String pedidoId = pedidoDAO.crearPedido(pedido);

        // Actualizar stock
        for (ItemCarritoDTO item : this.carritoActual.getItems()) {
            productoDAO.actualizarStock(item.getProductoId(), item.getCantidad());
        }

        // Mock: Simular proceso de pago exitoso
        pedidoDAO.actualizarEstado(pedidoId, "COMPLETADO");

        // Limpiar el carrito
        this.carritoActual = null;

        return pedidoId;
    }

    @Override
    public boolean verificarStockCarrito() {
        if (this.carritoActual == null || this.carritoActual.getItems().isEmpty()) {
            return false;
        }

        for (ItemCarritoDTO item : this.carritoActual.getItems()) {
            ProductoEntidad producto = productoDAO.obtenerPorId(item.getProductoId());
            if (producto == null || producto.getStock() < item.getCantidad()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Busca un item en el carrito por ID de producto.
     */
    private ItemCarritoDTO buscarItemEnCarrito(String productoId) {
        if (this.carritoActual == null || this.carritoActual.getItems() == null) {
            return null;
        }

        return this.carritoActual.getItems().stream()
                .filter(item -> item.getProductoId().equals(productoId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String agregarConfiguracionAlCarrito(dto.EnsamblajeDTO ensamblaje) {
        if (ensamblaje == null || ensamblaje.obtenerTodosComponentes().isEmpty()) {
            return null;
        }

        try {
            entidades.ConfiguracionEntidad configuracion = new entidades.ConfiguracionEntidad();
            configuracion.setNombre("Configuración " + java.time.LocalDateTime.now());

            java.util.List<java.util.Map<String, Object>> componentesList = new java.util.ArrayList<>();
            for (dto.ComponenteDTO comp : ensamblaje.obtenerTodosComponentes()) {
                java.util.Map<String, Object> compMap = new java.util.HashMap<>();
                compMap.put("categoria", comp.getCategoria());
                compMap.put("id", comp.getId());
                compMap.put("nombre", comp.getNombre());
                compMap.put("precio", comp.getPrecio());
                compMap.put("marca", comp.getMarca());
                componentesList.add(compMap);
            }
            configuracion.setComponentes(componentesList);
            configuracion.setPrecioTotal(ensamblaje.getPrecioTotal());

            dao.ConfiguracionDAO configuracionDAO = new dao.ConfiguracionDAO();
            configuracionDAO.guardar(configuracion);

            entidades.CarritoEntidad carrito = dao.CarritoDAO.getCarritoActual();
            carrito.agregarConfiguracion(configuracion.getId());

            dao.CarritoDAO carritoDAO = new dao.CarritoDAO();
            carritoDAO.guardar(carrito);

            return configuracion.getId().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public java.util.List<entidades.ConfiguracionEntidad> obtenerConfiguracionesEnCarrito() {
        java.util.List<entidades.ConfiguracionEntidad> configuraciones = new java.util.ArrayList<>();

        try {
            entidades.CarritoEntidad carrito = dao.CarritoDAO.getCarritoActual();
            dao.ConfiguracionDAO configuracionDAO = new dao.ConfiguracionDAO();

            if (carrito.getConfiguracionesIds() != null) {
                for (org.bson.types.ObjectId configId : carrito.getConfiguracionesIds()) {
                    entidades.ConfiguracionEntidad config = configuracionDAO.obtenerPorId(configId);
                    if (config != null) {
                        configuraciones.add(config);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return configuraciones;
    }

    @Override
    public boolean removerConfiguracionDelCarrito(String configuracionId) {
        try {
            entidades.CarritoEntidad carrito = dao.CarritoDAO.getCarritoActual();

            if (carrito.getConfiguracionesIds() == null) {
                return false;
            }

            org.bson.types.ObjectId objectId = new org.bson.types.ObjectId(configuracionId);
            boolean removed = carrito.getConfiguracionesIds().remove(objectId);

            if (removed) {
                dao.CarritoDAO carritoDAO = new dao.CarritoDAO();
                carritoDAO.guardar(carrito);
                return true;
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
