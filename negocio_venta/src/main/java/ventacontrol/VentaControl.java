package ventacontrol;

import dao.CarritoDAO;
import dao.ConfiguracionDAO;
import dao.PedidoDAO;
import dao.ProductoDAO;
import dto.MetodoPagoDTO;
import entidades.CarritoEntidad;
import entidades.ConfiguracionEntidad;
import entidades.PedidoEntidad;
import entidades.ProductoEntidad;
import objetosNegocio.CarritoBO;
//import objetosNegocio.PedidoBO;
import objetosNegocio.PedidoBO;
import objetosNegocio.mappers.CarritoMapper;
import objetosNegocio.mappers.PedidoMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Control de negocio para gestión de ventas y carritos.
 */
public class VentaControl {
    private static final Logger logger = LoggerFactory.getLogger(VentaControl.class);

    private final CarritoDAO carritoDAO;
    private final ConfiguracionDAO configuracionDAO;
    private final PedidoDAO pedidoDAO;
    private final ProductoDAO productoDAO;

    public VentaControl() {
        this.carritoDAO = new CarritoDAO();
        this.configuracionDAO = new ConfiguracionDAO();
        this.pedidoDAO = new PedidoDAO();
        this.productoDAO = new ProductoDAO();
    }

    public static VentaControl getInstance() {
        return new VentaControl();
    }

    /**
     * Crea un carrito nuevo para un cliente.
     */
    public CarritoBO crearCarrito(String clienteId) {
        try {
            CarritoBO carritoBO = new CarritoBO();
            carritoBO.setClienteId(clienteId);

            CarritoEntidad entidad = CarritoMapper.boAEntidad(carritoBO);
            entidad.setFechaActualizacion(LocalDateTime.now());

            carritoDAO.guardar(entidad);

            CarritoBO resultado = CarritoMapper.entidadABO(entidad);

            logger.info("Carrito creado para cliente {}", clienteId);
            return resultado;
        } catch (Exception e) {
            logger.error("Error al crear carrito para cliente {}", clienteId, e);
            throw new RuntimeException("No se pudo crear el carrito", e);
        }
    }

    /**
     * Obtiene el carrito de un cliente.
     */
    public CarritoBO obtenerCarrito(String clienteId) {
        try {
            CarritoEntidad entidad = carritoDAO.obtenerCarrito(clienteId);

            CarritoBO carritoBO = CarritoMapper.entidadABO(entidad);

            logger.debug("Carrito obtenido para cliente {}", clienteId);
            return carritoBO;
        } catch (Exception e) {
            logger.error("Error al obtener carrito para cliente {}", clienteId, e);
            throw new RuntimeException("No se pudo obtener el carrito", e);
        }
    }

    /**
     * Agrega una configuración al carrito.
     */
    public String agregarConfiguracionAlCarrito(String clienteId, String configuracionId) {
        if (configuracionId == null) {
            logger.warn("No se puede agregar una configuración nula al carrito");
            return null;
        }

        try {
            CarritoEntidad entidad = carritoDAO.obtenerCarrito(clienteId);
            CarritoBO carritoBO = CarritoMapper.entidadABO(entidad);

            carritoBO.agregarConfiguracion(configuracionId);

            CarritoEntidad entidadActualizada = CarritoMapper.boAEntidad(carritoBO);
            entidadActualizada.setFechaActualizacion(LocalDateTime.now());

            carritoDAO.guardar(entidadActualizada);

            logger.info("Configuración {} agregada al carrito del cliente {}", configuracionId, clienteId);
            return configuracionId;
        } catch (Exception e) {
            logger.error("Error al agregar configuración {} al carrito del cliente {}", configuracionId, clienteId, e);
            throw new RuntimeException("No se pudo agregar la configuración al carrito", e);
        }
    }

    /**
     * Agrega un producto al carrito con validaciones de stock.
     */
    public boolean agregarProductoAlCarrito(String clienteId, String productoId, int cantidad) {
        if (productoId == null || cantidad <= 0) {
            logger.warn("Intento de agregar producto inválido: productoId={}, cantidad={}", productoId, cantidad);
            return false;
        }

        try {
            ProductoEntidad producto = productoDAO.obtenerPorId(productoId);
            if (producto == null) {
                logger.warn("Producto {} no encontrado", productoId);
                return false;
            }

            if (producto.getStock() < cantidad) {
                logger.warn("Stock insuficiente para producto {}: requerido={}, disponible={}",
                    productoId, cantidad, producto.getStock());
                return false;
            }

            CarritoEntidad entidad = carritoDAO.obtenerCarrito(clienteId);
            CarritoBO carritoBO = CarritoMapper.entidadABO(entidad);

            carritoBO.agregarProducto(productoId, producto.getNombre(), producto.getPrecio(), cantidad);

            CarritoEntidad entidadActualizada = CarritoMapper.boAEntidad(carritoBO);
            entidadActualizada.setFechaActualizacion(LocalDateTime.now());

            carritoDAO.guardar(entidadActualizada);

            logger.info("Producto {} (cantidad: {}) agregado al carrito del cliente {}",
                productoId, cantidad, clienteId);
            return true;
        } catch (Exception e) {
            logger.error("Error al agregar producto {} al carrito del cliente {}", productoId, clienteId, e);
            throw new RuntimeException("No se pudo agregar el producto al carrito", e);
        }
    }

    /**
     * Vacía el carrito de un cliente.
     */
    public void vaciarCarrito(String clienteId) {
        try {
            CarritoEntidad entidad = carritoDAO.obtenerCarrito(clienteId);
            CarritoBO carritoBO = CarritoMapper.entidadABO(entidad);

            carritoBO.vaciar();

            CarritoEntidad entidadActualizada = CarritoMapper.boAEntidad(carritoBO);

            if (entidad.getConfiguracionesIds() != null && !entidad.getConfiguracionesIds().isEmpty()) {
                List<ObjectId> idsAEliminar = new ArrayList<>(entidad.getConfiguracionesIds());
                configuracionDAO.eliminarMultiples(idsAEliminar);
                logger.debug("Eliminadas {} configuraciones del carrito", idsAEliminar.size());
            }

            entidadActualizada.setFechaActualizacion(LocalDateTime.now());

            carritoDAO.guardar(entidadActualizada);

            logger.info("Carrito vaciado para cliente {}", clienteId);
        } catch (Exception e) {
            logger.error("Error al vaciar carrito del cliente {}", clienteId, e);
            throw new RuntimeException("No se pudo vaciar el carrito", e);
        }
    }

    /**
     * Calcula el total del carrito usando la lógica de negocio del BO.
     */
    public double calcularTotalCarrito(String clienteId) {
        try {
            CarritoEntidad entidad = carritoDAO.obtenerCarrito(clienteId);
            CarritoBO carritoBO = CarritoMapper.entidadABO(entidad);

            BigDecimal subtotalProductos = carritoBO.calcularSubtotal();

            BigDecimal totalConfiguraciones = calcularTotalConfiguraciones(carritoBO.getConfiguracionesIds());

            BigDecimal total = subtotalProductos.add(totalConfiguraciones);

            return total.doubleValue();
        } catch (Exception e) {
            logger.error("Error al calcular total del carrito para cliente {}", clienteId, e);
            return 0.0;
        }
    }

    /**
     * Calcula el total de las configuraciones en el carrito.
     */
    private BigDecimal calcularTotalConfiguraciones(List<String> configuracionesIds) {
        if (configuracionesIds == null || configuracionesIds.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (String configId : configuracionesIds) {
            try {
                ConfiguracionEntidad config = configuracionDAO.obtenerPorId(new ObjectId(configId));
                if (config != null && config.getPrecioTotal() != null) {
                    total = total.add(config.getPrecioTotal());
                }
            } catch (Exception e) {
                logger.warn("Error al obtener configuración {}: {}", configId, e.getMessage());
            }
        }
        return total;
    }

    private double calcularTotalCarritoInterno(CarritoEntidad carrito) {
        BigDecimal total = BigDecimal.ZERO;

        if (carrito.getConfiguracionesIds() != null) {
            for (ObjectId configId : carrito.getConfiguracionesIds()) {
                ConfiguracionEntidad config = configuracionDAO.obtenerPorId(configId);
                if (config != null && config.getPrecioTotal() != null) {
                    total = total.add(config.getPrecioTotal());
                }
            }
        }

        if (carrito.getProductos() != null) {
            for (Map<String, Object> prodMap : carrito.getProductos()) {
                String productoId = (String) prodMap.get("productoId");
                Integer cantidad = (Integer) prodMap.get("cantidad");

                if (productoId != null && cantidad != null && cantidad > 0) {
                    ProductoEntidad producto = productoDAO.obtenerPorId(productoId);
                    if (producto != null && producto.getPrecio() != null) {
                        BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
                        total = total.add(subtotal);
                    }
                }
            }
        }

        return total.doubleValue();
    }

    public List<ConfiguracionEntidad> obtenerConfiguracionesEnCarrito(String clienteId) {
        List<ConfiguracionEntidad> configuraciones = new ArrayList<>();

        try {
            CarritoEntidad carrito = carritoDAO.obtenerCarrito(clienteId);

            if (carrito.getConfiguracionesIds() != null) {
                for (ObjectId configId : carrito.getConfiguracionesIds()) {
                    ConfiguracionEntidad config = configuracionDAO.obtenerPorId(configId);
                    if (config != null) {
                        configuraciones.add(config);
                    }
                }
            }

            logger.debug("Se encontraron {} configuraciones en el carrito del cliente {}",
                configuraciones.size(), clienteId);
        } catch (Exception e) {
            logger.error("Error al obtener configuraciones del carrito para cliente {}", clienteId, e);
        }

        return configuraciones;
    }

    public String confirmarPedido(String clienteId, MetodoPagoDTO metodoPago) {
        try {
            CarritoEntidad carrito = carritoDAO.obtenerCarrito(clienteId);
            List<ConfiguracionEntidad> configuraciones = obtenerConfiguracionesEnCarrito(clienteId);
            List<Map<String, Object>> productosIndividuales = carrito.getProductos();

            boolean tieneConfiguraciones = configuraciones != null && !configuraciones.isEmpty();
            boolean tieneProductos = productosIndividuales != null && !productosIndividuales.isEmpty();

            if (!tieneConfiguraciones && !tieneProductos) {
                logger.warn("Intento de confirmar pedido con carrito vacío para cliente {}", clienteId);
                return null;
            }

            PedidoBO pedidoBO = new PedidoBO();
            pedidoBO.setClienteId(clienteId);
            pedidoBO.setEstado(PedidoBO.EstadoPedido.PROCESANDO);
            pedidoBO.setFechaCreacion(LocalDateTime.now());

            if (metodoPago != null) {
                PedidoBO.MetodoPagoInfo metodoPagoInfo = new PedidoBO.MetodoPagoInfo();
                metodoPagoInfo.setTipo(metodoPago.getTipo() != null ? metodoPago.getTipo().toString() : "TARJETA");
                metodoPagoInfo.setDetalles("Pago procesado exitosamente");
                pedidoBO.setMetodoPago(metodoPagoInfo);
            }

            List<PedidoBO.ItemPedido> itemsPedidoBO = construirItemsPedidoBO(configuraciones, productosIndividuales);
            pedidoBO.setItems(itemsPedidoBO);
            pedidoBO.setTotal(BigDecimal.valueOf(calcularTotalCarritoInterno(carrito)));

            PedidoEntidad pedidoEntidad = PedidoMapper.boAEntidad(pedidoBO);

            String pedidoId = pedidoDAO.crearPedido(pedidoEntidad);

            if (pedidoId != null) {
                pedidoDAO.actualizarEstado(pedidoId, PedidoEntidad.EstadoPedido.ENTREGADO);
                vaciarCarrito(clienteId);
                logger.info("Pedido {} confirmado exitosamente para cliente {}", pedidoId, clienteId);
                return pedidoId;
            } else {
                logger.error("No se pudo crear el pedido para cliente {}", clienteId);
                return null;
            }

        } catch (Exception e) {
            logger.error("Error al confirmar pedido para cliente {}", clienteId, e);
            throw new RuntimeException("No se pudo confirmar el pedido", e);
        }
    }

    /**
     * Construye los items del pedido usando PedidoBO.
     */
    private List<PedidoBO.ItemPedido> construirItemsPedidoBO(
            List<ConfiguracionEntidad> configuraciones,
            List<Map<String, Object>> productosIndividuales) {

        List<PedidoBO.ItemPedido> itemsPedido = new ArrayList<>();

        if (configuraciones != null) {
            for (ConfiguracionEntidad config : configuraciones) {
                PedidoBO.ItemPedido itemPedido = new PedidoBO.ItemPedido();
                itemPedido.setProductoId(config.getId() != null ? config.getId().toString() : "");
                itemPedido.setNombre(config.getNombre() != null ? config.getNombre() : "Configuración PC");
                itemPedido.setCantidad(1);
                BigDecimal precio = config.getPrecioTotal() != null ? config.getPrecioTotal() : BigDecimal.ZERO;
                itemPedido.setPrecioUnitario(precio);
                itemsPedido.add(itemPedido);
            }
        }

        if (productosIndividuales != null) {
            for (Map<String, Object> prodMap : productosIndividuales) {
                String productoId = (String) prodMap.get("productoId");
                Integer cantidad = (Integer) prodMap.get("cantidad");

                if (productoId != null && cantidad != null && cantidad > 0) {
                    ProductoEntidad producto = productoDAO.obtenerPorId(productoId);
                    if (producto != null) {
                        PedidoBO.ItemPedido itemPedido = new PedidoBO.ItemPedido();
                        itemPedido.setProductoId(productoId);
                        itemPedido.setNombre(producto.getNombre());
                        itemPedido.setCantidad(cantidad);
                        itemPedido.setPrecioUnitario(producto.getPrecio());
                        itemsPedido.add(itemPedido);
                    }
                }
            }
        }

        return itemsPedido;
    }

    public void removerItemDelCarrito(String clienteId, String productoId) {
        try {
            CarritoEntidad carrito = carritoDAO.obtenerCarrito(clienteId);

            if (carrito.getProductos() != null) {
                carrito.getProductos().removeIf(p -> productoId.equals(p.get("productoId")));
                carrito.setFechaActualizacion(LocalDateTime.now());
                carritoDAO.guardar(carrito);
                logger.info("Producto {} removido del carrito del cliente {}", productoId, clienteId);
            }
        } catch (Exception e) {
            logger.error("Error al remover producto {} del carrito del cliente {}", productoId, clienteId, e);
            throw new RuntimeException("No se pudo remover el producto del carrito", e);
        }
    }

    public boolean actualizarCantidadItem(String clienteId, String productoId, int nuevaCantidad) {
        try {
            if (nuevaCantidad <= 0) {
                removerItemDelCarrito(clienteId, productoId);
                return true;
            }

            ProductoEntidad producto = productoDAO.obtenerPorId(productoId);
            if (producto == null) {
                logger.warn("Producto {} no encontrado al actualizar cantidad", productoId);
                return false;
            }

            if (producto.getStock() < nuevaCantidad) {
                logger.warn("Stock insuficiente para producto {}: requerido={}, disponible={}",
                    productoId, nuevaCantidad, producto.getStock());
                return false;
            }

            CarritoEntidad carrito = carritoDAO.obtenerCarrito(clienteId);

            if (carrito.getProductos() != null) {
                for (Map<String, Object> p : carrito.getProductos()) {
                    if (productoId.equals(p.get("productoId"))) {
                        p.put("cantidad", nuevaCantidad);
                        carrito.setFechaActualizacion(LocalDateTime.now());
                        carritoDAO.guardar(carrito);
                        logger.info("Cantidad actualizada para producto {} en carrito de cliente {}: nueva cantidad={}",
                            productoId, clienteId, nuevaCantidad);
                        return true;
                    }
                }
            }

            logger.warn("Producto {} no encontrado en carrito de cliente {}", productoId, clienteId);
            return false;
        } catch (Exception e) {
            logger.error("Error al actualizar cantidad de producto {} en carrito de cliente {}",
                productoId, clienteId, e);
            throw new RuntimeException("No se pudo actualizar la cantidad del producto", e);
        }
    }

    public boolean removerConfiguracionDelCarrito(String clienteId, String configuracionId) {
        try {
            CarritoEntidad carrito = carritoDAO.obtenerCarrito(clienteId);

            if (carrito.getConfiguracionesIds() == null) {
                logger.warn("No hay configuraciones en el carrito del cliente {}", clienteId);
                return false;
            }

            ObjectId objectId = new ObjectId(configuracionId);
            boolean removed = carrito.getConfiguracionesIds().remove(objectId);

            if (removed) {
                carrito.setFechaActualizacion(LocalDateTime.now());
                carritoDAO.guardar(carrito);
                logger.info("Configuración {} removida del carrito del cliente {}", configuracionId, clienteId);
                return true;
            }

            logger.warn("Configuración {} no encontrada en carrito de cliente {}", configuracionId, clienteId);
            return false;
        } catch (Exception e) {
            logger.error("Error al remover configuración {} del carrito del cliente {}",
                configuracionId, clienteId, e);
            throw new RuntimeException("No se pudo remover la configuración del carrito", e);
        }
    }

    public boolean verificarStockCarrito(String clienteId) {
        try {
            CarritoEntidad carrito = carritoDAO.obtenerCarrito(clienteId);

            if (carrito.getProductos() != null) {
                for (Map<String, Object> prodMap : carrito.getProductos()) {
                    String productoId = (String) prodMap.get("productoId");
                    Integer cantidad = (Integer) prodMap.get("cantidad");

                    if (productoId != null && cantidad != null && cantidad > 0) {
                        ProductoEntidad producto = productoDAO.obtenerPorId(productoId);
                        if (producto == null) {
                            logger.warn("Producto {} no encontrado al verificar stock", productoId);
                            return false;
                        }
                        if (producto.getStock() < cantidad) {
                            logger.warn("Stock insuficiente para producto {}: requerido={}, disponible={}",
                                productoId, cantidad, producto.getStock());
                            return false;
                        }
                    }
                }
            }

            logger.debug("Stock verificado correctamente para carrito de cliente {}", clienteId);
            return true;
        } catch (Exception e) {
            logger.error("Error al verificar stock del carrito del cliente {}", clienteId, e);
            return false;
        }
    }
}

