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
}

