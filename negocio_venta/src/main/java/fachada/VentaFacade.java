package fachada;

import dao.*;
import dto.CarritoDTO;
import dto.ItemCarritoDTO;
import dto.MetodoPagoDTO;
import entidades.PedidoEntidad;
import entidades.ProductoEntidad;
import entidades.UsuarioEntidad;
import org.bson.types.ObjectId;

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
        if (clienteId == null) {
            try {
                entidades.UsuarioEntidad usuario = obtenerOCrearUsuarioDefault();
                clienteId = usuario != null && usuario.getId() != null
                    ? usuario.getId().toString()
                    : "cliente_default";
            } catch (Exception ignored) {
                clienteId = "cliente_default";
            }
        }
        this.carritoActual.setClienteId(clienteId);
        this.carritoActual.setItems(new ArrayList<>());
        return this.carritoActual;
    }

    @Override
    public CarritoDTO getCarritoActual() {
        return this.carritoActual;
    }

    @Override
    public UsuarioEntidad getUsuarioActual() {
        try {
            if (this.carritoActual == null) {
                crearCarrito(null);
            }
            String clienteId = this.carritoActual != null ? this.carritoActual.getClienteId() : null;
            if (clienteId == null) return null;
            UsuarioDAO usuarioDAO = new dao.UsuarioDAO();
            UsuarioEntidad usuario = usuarioDAO.obtenerPorId(clienteId);
            return usuario;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean agregarItemAlCarrito(ItemCarritoDTO item) {
        if (this.carritoActual == null) {
            return false;
        }

        ProductoEntidad producto = productoDAO.obtenerPorId(item.getProductoId());
        if (producto == null || producto.getStock() < item.getCantidad()) {
            return false;
        }

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

        if (!verificarStockCarrito()) {
            return null;
        }

        PedidoEntidad pedido = new PedidoEntidad();
        pedido.setClienteId(this.carritoActual.getClienteId());
        pedido.setTotal(calcularTotalCarrito());
        pedido.setEstado("PROCESANDO");
        pedido.setFechaCreacion(new Date());

        PedidoEntidad.MetodoPagoInfo metodoPagoInfo = new PedidoEntidad.MetodoPagoInfo();
        metodoPagoInfo.setTipo(metodoPago.getTipo().toString());
        metodoPagoInfo.setDetalles("Mock - Pago procesado exitosamente");
        pedido.setMetodoPago(metodoPagoInfo);

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

        String pedidoId = pedidoDAO.crearPedido(pedido);

        for (ItemCarritoDTO item : this.carritoActual.getItems()) {
            productoDAO.actualizarStock(item.getProductoId(), item.getCantidad());
        }

        // Mock: Simular proceso de pago exitoso
        pedidoDAO.actualizarEstado(pedidoId, "COMPLETADO");

        this.carritoActual = null;

        return pedidoId;
    }

    @Override
    public void vaciarCarrito() {
        try {
            entidades.UsuarioEntidad usuario = obtenerOCrearUsuarioDefault();
            String clienteId = usuario != null && usuario.getId() != null
                ? usuario.getId().toString()
                : "cliente_default";

            this.carritoActual = new CarritoDTO();
            this.carritoActual.setClienteId(clienteId);
            this.carritoActual.setItems(new ArrayList<>());

            dao.CarritoDAO carritoDAO = new dao.CarritoDAO();
            entidades.CarritoEntidad carritoEntidad = carritoDAO.obtenerPorClienteId(clienteId);
            if (carritoEntidad != null) {
                carritoEntidad.setConfiguracionesIds(new java.util.ArrayList<>());
                carritoDAO.guardar(carritoEntidad);
            } else {
                carritoEntidad = new entidades.CarritoEntidad();
                carritoEntidad.setClienteId(clienteId);
                carritoDAO.guardar(carritoEntidad);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * Obtiene o crea el usuario por defecto con sus métodos de pago registrados.
     */
    private UsuarioEntidad obtenerOCrearUsuarioDefault() {
        try {
            dao.UsuarioDAO usuarioDAO = new dao.UsuarioDAO();
            entidades.UsuarioEntidad usuario = usuarioDAO.obtenerPorEmail("cliente_default@local");

            if (usuario == null) {
                usuario = new entidades.UsuarioEntidad();
                usuario.setNombre("Cliente Default");
                usuario.setEmail("cliente_default@local");

                entidades.UsuarioEntidad.MetodoPagoRegistrado efectivo = new entidades.UsuarioEntidad.MetodoPagoRegistrado();
                efectivo.setId("mp_efectivo_001");
                efectivo.setTipo("Efectivo en sucursal");
                efectivo.setAlias("Pago en efectivo");
                efectivo.setEsPredeterminado(true);
                usuario.agregarMetodoPago(efectivo);

                entidades.UsuarioEntidad.MetodoPagoRegistrado tarjeta = new entidades.UsuarioEntidad.MetodoPagoRegistrado();
                tarjeta.setId("mp_tarjeta_001");
                tarjeta.setTipo("Tarjeta de crédito/débito");
                tarjeta.setAlias("Tarjeta principal");
                tarjeta.setUltimos4Digitos("1234");
                tarjeta.setNombreTitular("Cliente Default");
                tarjeta.setEsPredeterminado(false);
                usuario.agregarMetodoPago(tarjeta);

                usuarioDAO.guardar(usuario);
            }

            return usuario;
        } catch (Exception e) {
            return null;
        }
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
    public List<entidades.ConfiguracionEntidad> obtenerConfiguracionesEnCarrito() {
        List<entidades.ConfiguracionEntidad> configuraciones = new ArrayList<>();

        try {
            entidades.CarritoEntidad carrito = dao.CarritoDAO.getCarritoActual();
            dao.ConfiguracionDAO configuracionDAO = new dao.ConfiguracionDAO();

            if (carrito.getConfiguracionesIds() != null) {
                for (ObjectId configId : carrito.getConfiguracionesIds()) {
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

            ObjectId objectId = new ObjectId(configuracionId);
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
