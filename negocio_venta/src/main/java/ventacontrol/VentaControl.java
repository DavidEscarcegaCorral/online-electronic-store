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
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Control de negocio para gestión de ventas y carritos.
 *
 * PATRÓN: Stateless (Sin Estado)
 * - NO mantiene carritoActual como variable de instancia.
 * - Cada método recibe clienteId y recupera el carrito de la BD.
 * - Seguro para concurrencia y multi-usuario.
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

    public CarritoEntidad crearCarrito(String clienteId) {
        try {
            CarritoEntidad carrito = new CarritoEntidad();
            carrito.setClienteId(clienteId);
            carrito.setFechaActualizacion(LocalDateTime.now());
            carritoDAO.guardar(carrito);

            logger.info("Carrito creado para cliente {}", clienteId);
            return carrito;
        } catch (Exception e) {
            logger.error("Error al crear carrito para cliente {}", clienteId, e);
            throw new RuntimeException("No se pudo crear el carrito", e);
        }
    }

    public CarritoEntidad obtenerCarrito(String clienteId) {
        try {
            CarritoEntidad carrito = carritoDAO.obtenerCarrito(clienteId);
            logger.debug("Carrito obtenido para cliente {}", clienteId);
            return carrito;
        } catch (Exception e) {
            logger.error("Error al obtener carrito para cliente {}", clienteId, e);
            throw new RuntimeException("No se pudo obtener el carrito", e);
        }
    }

    public String agregarConfiguracionAlCarrito(String clienteId, String configuracionId) {
        if (configuracionId == null) {
            logger.warn("Intento de agregar configuración nula al carrito");
            return null;
        }

        try {
            CarritoEntidad carrito = carritoDAO.obtenerCarrito(clienteId);
            carrito.agregarConfiguracion(new ObjectId(configuracionId));
            carrito.setFechaActualizacion(LocalDateTime.now());
            carritoDAO.guardar(carrito);

            logger.info("Configuración {} agregada al carrito del cliente {}", configuracionId, clienteId);
            return configuracionId;
        } catch (Exception e) {
            logger.error("Error al agregar configuración {} al carrito del cliente {}", configuracionId, clienteId, e);
            throw new RuntimeException("No se pudo agregar la configuración al carrito", e);
        }
    }

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

            CarritoEntidad carrito = carritoDAO.obtenerCarrito(clienteId);
            carrito.agregarProducto(productoId, cantidad);
            carrito.setFechaActualizacion(LocalDateTime.now());
            carritoDAO.guardar(carrito);

            logger.info("Producto {} (cantidad: {}) agregado al carrito del cliente {}",
                productoId, cantidad, clienteId);
            return true;
        } catch (Exception e) {
            logger.error("Error al agregar producto {} al carrito del cliente {}", productoId, clienteId, e);
            throw new RuntimeException("No se pudo agregar el producto al carrito", e);
        }
    }

    public void vaciarCarrito(String clienteId) {
        try {
            CarritoEntidad carrito = carritoDAO.obtenerCarrito(clienteId);

            if (carrito.getConfiguracionesIds() != null && !carrito.getConfiguracionesIds().isEmpty()) {
                List<ObjectId> idsAEliminar = new ArrayList<>(carrito.getConfiguracionesIds());
                configuracionDAO.eliminarMultiples(idsAEliminar);
                logger.debug("Eliminadas {} configuraciones del carrito", idsAEliminar.size());
            }

            carrito.setConfiguracionesIds(new ArrayList<>());
            carrito.setProductos(new ArrayList<>());
            carrito.setFechaActualizacion(LocalDateTime.now());
            carritoDAO.guardar(carrito);

            logger.info("Carrito vaciado para cliente {}", clienteId);
        } catch (Exception e) {
            logger.error("Error al vaciar carrito del cliente {}", clienteId, e);
            throw new RuntimeException("No se pudo vaciar el carrito", e);
        }
    }

    public double calcularTotalCarrito(String clienteId) {
        try {
            CarritoEntidad carrito = carritoDAO.obtenerCarrito(clienteId);
            return calcularTotalCarritoInterno(carrito);
        } catch (Exception e) {
            logger.error("Error al calcular total del carrito para cliente {}", clienteId, e);
            return 0.0;
        }
    }

    private double calcularTotalCarritoInterno(CarritoEntidad carrito) {
        double total = 0.0;

        if (carrito.getConfiguracionesIds() != null) {
            for (ObjectId configId : carrito.getConfiguracionesIds()) {
                ConfiguracionEntidad config = configuracionDAO.obtenerPorId(configId);
                if (config != null && config.getPrecioTotal() != null) {
                    total += config.getPrecioTotal();
                }
            }
        }

        if (carrito.getProductos() != null) {
            for (Map<String, Object> prodMap : carrito.getProductos()) {
                String productoId = (String) prodMap.get("productoId");
                Integer cantidad = (Integer) prodMap.get("cantidad");

                if (productoId != null && cantidad != null && cantidad > 0) {
                    ProductoEntidad producto = productoDAO.obtenerPorId(productoId);
                    if (producto != null) {
                        total += producto.getPrecio() * cantidad;
                    }
                }
            }
        }

        return total;
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

            PedidoEntidad pedido = new PedidoEntidad();
            pedido.setClienteId(clienteId);
            pedido.setEstado("PROCESANDO");
            pedido.setFechaCreacion(new Date());

            if (metodoPago != null) {
                PedidoEntidad.MetodoPagoInfo metodoPagoInfo = new PedidoEntidad.MetodoPagoInfo();
                metodoPagoInfo.setTipo(metodoPago.getTipo() != null ? metodoPago.getTipo().toString() : "TARJETA");
                metodoPagoInfo.setDetalles("Pago procesado exitosamente");
                pedido.setMetodoPago(metodoPagoInfo);
            }

            List<PedidoEntidad.ItemPedido> itemsPedido = construirItemsPedido(configuraciones, productosIndividuales);
            pedido.setItems(itemsPedido);
            pedido.setTotal(calcularTotalCarritoInterno(carrito));

            String pedidoId = pedidoDAO.crearPedido(pedido);

            if (pedidoId != null) {
                pedidoDAO.actualizarEstado(pedidoId, "COMPLETADO");
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

    private List<PedidoEntidad.ItemPedido> construirItemsPedido(
            List<ConfiguracionEntidad> configuraciones,
            List<Map<String, Object>> productosIndividuales) {

        List<PedidoEntidad.ItemPedido> itemsPedido = new ArrayList<>();

        if (configuraciones != null) {
            for (ConfiguracionEntidad config : configuraciones) {
                PedidoEntidad.ItemPedido itemPedido = new PedidoEntidad.ItemPedido();
                itemPedido.setProductoId(config.getId() != null ? config.getId().toString() : "");
                itemPedido.setNombre(config.getNombre() != null ? config.getNombre() : "Configuración PC");
                itemPedido.setCantidad(1);
                double precio = config.getPrecioTotal() != null ? config.getPrecioTotal() : 0.0;
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
                        PedidoEntidad.ItemPedido itemPedido = new PedidoEntidad.ItemPedido();
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

