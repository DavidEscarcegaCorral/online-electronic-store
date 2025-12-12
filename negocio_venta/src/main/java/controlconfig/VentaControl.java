package controlconfig;

import dao.CarritoDAO;
import dao.ConfiguracionDAO;
import dao.PedidoDAO;
import dao.UsuarioDAO;
import dto.MetodoPagoDTO;
import entidades.CarritoEntidad;
import entidades.ConfiguracionEntidad;
import entidades.PedidoEntidad;
import entidades.UsuarioEntidad;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class VentaControl implements IVentaControl {
    private static VentaControl instancia;
    private final CarritoDAO carritoDAO;
    private final ConfiguracionDAO configuracionDAO;
    private final PedidoDAO pedidoDAO;
    private final UsuarioDAO usuarioDAO;
    private CarritoEntidad carritoActual;

    private VentaControl() {
        this.carritoDAO = new CarritoDAO();
        this.configuracionDAO = new ConfiguracionDAO();
        this.pedidoDAO = new PedidoDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.carritoActual = null;
    }

    public static synchronized VentaControl getInstance() {
        if (instancia == null) {
            instancia = new VentaControl();
        }
        return instancia;
    }

    private String obtenerClienteIdDefecto() {
        UsuarioEntidad usuario = usuarioDAO.obtenerPorEmail("cliente_default@local");
        if (usuario != null && usuario.getId() != null) {
            return usuario.getId().toString();
        }
        throw new IllegalStateException("Usuario por defecto no encontrado en la base de datos");
    }

    private CarritoEntidad obtenerCarritoActual() {
        if (carritoActual == null) {
            String clienteId = obtenerClienteIdDefecto();
            carritoActual = carritoDAO.obtenerCarrito(clienteId);
        }
        return carritoActual;
    }

    private void guardarCarrito() {
        if (carritoActual != null) {
            carritoDAO.guardar(carritoActual);
        }
    }

    public void recargarCarrito() {
        String clienteId = obtenerClienteIdDefecto();
        carritoActual = carritoDAO.obtenerCarrito(clienteId);
    }

    @Override
    public String agregarConfiguracionAlCarrito(String configuracionId) {
        if (configuracionId == null) {
            return null;
        }

        try {
            CarritoEntidad carrito = obtenerCarritoActual();
            carrito.agregarConfiguracion(new ObjectId(configuracionId));
            guardarCarrito();
            return configuracionId;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean agregarProductoAlCarrito(String productoId, int cantidad) {
        if (productoId == null || cantidad <= 0) {
            return false;
        }

        try {
            CarritoEntidad carrito = obtenerCarritoActual();
            carrito.agregarProducto(productoId, cantidad);
            guardarCarrito();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void vaciarCarrito() {
        try {
            CarritoEntidad carrito = obtenerCarritoActual();

            if (carrito.getConfiguracionesIds() != null && !carrito.getConfiguracionesIds().isEmpty()) {
                List<ObjectId> idsAEliminar = new ArrayList<>(carrito.getConfiguracionesIds());
                configuracionDAO.eliminarMultiples(idsAEliminar);
            }

            carrito.setConfiguracionesIds(new ArrayList<>());
            carrito.setProductos(new ArrayList<>());
            carrito.setFechaActualizacion(java.time.LocalDateTime.now());
            guardarCarrito();

            // Recargar el carrito desde BD para asegurar sincronización
            recargarCarrito();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public double calcularTotalCarrito() {
        try {
            double total = 0.0;

            // Sumar configuraciones
            List<ConfiguracionEntidad> configuraciones = obtenerConfiguracionesEnCarrito();
            if (configuraciones != null && !configuraciones.isEmpty()) {
                total += configuraciones.stream()
                        .mapToDouble(config -> {
                            Double precioTotal = config.getPrecioTotal();
                            return precioTotal != null ? precioTotal : 0.0;
                        })
                        .sum();
            }

            // Sumar productos individuales
            CarritoEntidad carrito = obtenerCarritoActual();
            List<java.util.Map<String, Object>> productos = carrito.getProductos();
            if (productos != null && !productos.isEmpty()) {
                dao.ProductoDAO productoDAO = new dao.ProductoDAO();
                for (java.util.Map<String, Object> prodMap : productos) {
                    String productoId = (String) prodMap.get("productoId");
                    Integer cantidad = (Integer) prodMap.get("cantidad");

                    if (productoId != null && cantidad != null && cantidad > 0) {
                        entidades.ProductoEntidad producto = productoDAO.obtenerPorId(productoId);
                        if (producto != null) {
                            total += producto.getPrecio() * cantidad;
                        }
                    }
                }
            }

            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    @Override
    public List<ConfiguracionEntidad> obtenerConfiguracionesEnCarrito() {
        List<ConfiguracionEntidad> configuraciones = new ArrayList<>();

        try {
            CarritoEntidad carrito = obtenerCarritoActual();

            if (carrito.getConfiguracionesIds() != null) {
                for (ObjectId configId : carrito.getConfiguracionesIds()) {
                    ConfiguracionEntidad config = configuracionDAO.obtenerPorId(configId);
                    if (config != null) {
                        configuraciones.add(config);
                    }
                }
            }
        } catch (Exception e) {
        }

        return configuraciones;
    }

    @Override
    public String confirmarPedido(MetodoPagoDTO metodoPago) {
        try {
            CarritoEntidad carrito = obtenerCarritoActual();
            List<ConfiguracionEntidad> configuraciones = obtenerConfiguracionesEnCarrito();
            List<java.util.Map<String, Object>> productosIndividuales = carrito.getProductos();

            // Validar que haya al menos configuraciones o productos
            boolean tieneConfiguraciones = configuraciones != null && !configuraciones.isEmpty();
            boolean tieneProductos = productosIndividuales != null && !productosIndividuales.isEmpty();

            if (!tieneConfiguraciones && !tieneProductos) {
                return null;
            }

            PedidoEntidad pedido = new PedidoEntidad();
            pedido.setClienteId(obtenerClienteIdActual());
            pedido.setEstado("PROCESANDO");
            pedido.setFechaCreacion(new Date());

            if (metodoPago != null) {
                PedidoEntidad.MetodoPagoInfo metodoPagoInfo = new PedidoEntidad.MetodoPagoInfo();
                metodoPagoInfo.setTipo(metodoPago.getTipo() != null ? metodoPago.getTipo().toString() : "TARJETA");
                metodoPagoInfo.setDetalles("Pago procesado exitosamente");
                pedido.setMetodoPago(metodoPagoInfo);
            }

            List<PedidoEntidad.ItemPedido> itemsPedido = new ArrayList<>();
            double totalPedido = 0.0;

            // Agregar configuraciones como items
            if (tieneConfiguraciones) {
                for (ConfiguracionEntidad config : configuraciones) {
                    PedidoEntidad.ItemPedido itemPedido = new PedidoEntidad.ItemPedido();
                    itemPedido.setProductoId(config.getId() != null ? config.getId().toString() : "");
                    itemPedido.setNombre(config.getNombre() != null ? config.getNombre() : "Configuración PC");
                    itemPedido.setCantidad(1);
                    double precio = config.getPrecioTotal() != null ? config.getPrecioTotal() : 0.0;
                    itemPedido.setPrecioUnitario(precio);
                    itemsPedido.add(itemPedido);
                    totalPedido += precio;
                }
            }

            // Agregar productos individuales como items
            if (tieneProductos) {
                dao.ProductoDAO productoDAO = new dao.ProductoDAO();
                for (Map<String, Object> prodMap : productosIndividuales) {
                    String productoId = (String) prodMap.get("productoId");
                    Integer cantidad = (Integer) prodMap.get("cantidad");

                    if (productoId != null && cantidad != null && cantidad > 0) {
                        entidades.ProductoEntidad producto = productoDAO.obtenerPorId(productoId);
                        if (producto != null) {
                            PedidoEntidad.ItemPedido itemPedido = new PedidoEntidad.ItemPedido();
                            itemPedido.setProductoId(productoId);
                            itemPedido.setNombre(producto.getNombre());
                            itemPedido.setCantidad(cantidad);
                            itemPedido.setPrecioUnitario(producto.getPrecio());
                            itemsPedido.add(itemPedido);
                            totalPedido += producto.getPrecio() * cantidad;
                        }
                    }
                }
            }

            pedido.setItems(itemsPedido);
            pedido.setTotal(totalPedido);

            String pedidoId = pedidoDAO.crearPedido(pedido);

            if (pedidoId != null) {
                pedidoDAO.actualizarEstado(pedidoId, "COMPLETADO");
                vaciarCarrito();
                return pedidoId;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String obtenerClienteIdActual() {
        try {
            CarritoEntidad carrito = obtenerCarritoActual();
            return carrito.getClienteId();
        } catch (Exception e) {
            return "cliente_default";
        }
    }
}

