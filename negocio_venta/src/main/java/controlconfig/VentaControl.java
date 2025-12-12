package controlconfig;

import dao.CarritoDAO;
import dao.ConfiguracionDAO;
import dao.PedidoDAO;
import dto.MetodoPagoDTO;
import entidades.CarritoEntidad;
import entidades.ConfiguracionEntidad;
import entidades.PedidoEntidad;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentaControl implements IVentaControl {
    private static VentaControl instancia;
    private final CarritoDAO carritoDAO;
    private final ConfiguracionDAO configuracionDAO;
    private final PedidoDAO pedidoDAO;

    private VentaControl() {
        this.carritoDAO = new CarritoDAO();
        this.configuracionDAO = new ConfiguracionDAO();
        this.pedidoDAO = new PedidoDAO();
    }

    public static synchronized VentaControl getInstance() {
        if (instancia == null) {
            instancia = new VentaControl();
        }
        return instancia;
    }

    @Override
    public String agregarConfiguracionAlCarrito(String configuracionId) {
        if (configuracionId == null) {
            return null;
        }

        try {
            CarritoEntidad carrito = CarritoDAO.getCarritoActual();
            carrito.agregarConfiguracion(new ObjectId(configuracionId));
            carritoDAO.guardar(carrito);
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
            CarritoEntidad carrito = CarritoDAO.getCarritoActual();
            carrito.agregarProducto(productoId, cantidad);
            carritoDAO.guardar(carrito);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void vaciarCarrito() {
        try {
            CarritoEntidad carritoEntidad = CarritoDAO.getCarritoActual();

            if (carritoEntidad.getConfiguracionesIds() != null && !carritoEntidad.getConfiguracionesIds().isEmpty()) {
                List<ObjectId> idsAEliminar = new ArrayList<>(carritoEntidad.getConfiguracionesIds());
                configuracionDAO.eliminarMultiples(idsAEliminar);
            }

            carritoEntidad.setConfiguracionesIds(new ArrayList<>());
            carritoEntidad.setProductos(new ArrayList<>());
            carritoEntidad.setFechaActualizacion(java.time.LocalDateTime.now());
            carritoDAO.guardar(carritoEntidad);

        } catch (Exception e) {
        }
    }

    @Override
    public double calcularTotalCarrito() {
        try {
            List<ConfiguracionEntidad> configuraciones = obtenerConfiguracionesEnCarrito();
            if (configuraciones == null || configuraciones.isEmpty()) {
                return 0.0;
            }

            return configuraciones.stream()
                    .mapToDouble(config -> {
                        Double precioTotal = config.getPrecioTotal();
                        return precioTotal != null ? precioTotal : 0.0;
                    })
                    .sum();
        } catch (Exception e) {
            return 0.0;
        }
    }

    @Override
    public List<ConfiguracionEntidad> obtenerConfiguracionesEnCarrito() {
        List<ConfiguracionEntidad> configuraciones = new ArrayList<>();

        try {
            CarritoEntidad carrito = CarritoDAO.getCarritoActual();

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
            List<ConfiguracionEntidad> configuraciones = obtenerConfiguracionesEnCarrito();

            if (configuraciones == null || configuraciones.isEmpty()) {
                return null;
            }

            PedidoEntidad pedido = new PedidoEntidad();
            pedido.setClienteId(obtenerClienteIdActual());
            pedido.setTotal(calcularTotalCarrito());
            pedido.setEstado("PROCESANDO");
            pedido.setFechaCreacion(new Date());

            if (metodoPago != null) {
                PedidoEntidad.MetodoPagoInfo metodoPagoInfo = new PedidoEntidad.MetodoPagoInfo();
                metodoPagoInfo.setTipo(metodoPago.getTipo() != null ? metodoPago.getTipo().toString() : "TARJETA");
                metodoPagoInfo.setDetalles("Pago procesado exitosamente");
                pedido.setMetodoPago(metodoPagoInfo);
            }

            List<PedidoEntidad.ItemPedido> itemsPedido = new ArrayList<>();
            for (ConfiguracionEntidad config : configuraciones) {
                PedidoEntidad.ItemPedido itemPedido = new PedidoEntidad.ItemPedido();
                itemPedido.setProductoId(config.getId() != null ? config.getId().toString() : "");
                itemPedido.setNombre(config.getNombre() != null ? config.getNombre() : "Configuración PC");
                itemPedido.setCantidad(1);
                itemPedido.setPrecioUnitario(config.getPrecioTotal() != null ? config.getPrecioTotal() : 0.0);
                itemsPedido.add(itemPedido);
            }
            pedido.setItems(itemsPedido);

            String pedidoId = pedidoDAO.crearPedido(pedido);

            if (pedidoId != null) {
                pedidoDAO.actualizarEstado(pedidoId, "COMPLETADO");
                vaciarCarrito();
                return pedidoId;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    private String obtenerClienteIdActual() {
        try {
            CarritoEntidad carrito = CarritoDAO.getCarritoActual();
            return carrito.getClienteId();
        } catch (Exception e) {
            return "cliente_default";
        }
    }
}

