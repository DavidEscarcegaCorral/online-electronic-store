package ventacontrol;

import ensamblajecontrol.ConfiguracionFacade;
import ensamblajecontrol.IEnsamblajeControl;
import dao.UsuarioDAO;
import dto.CarritoDTO;
import dto.MetodoPagoDTO;
import entidades.CarritoEntidad;
import entidades.ConfiguracionEntidad;
import entidades.UsuarioEntidad;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la fachada para el subsistema de Venta.
 *
 * PATRÓN: Facade
 */
public class VentaFacade implements IVentaFacade {
    private final VentaControl ventaControl;
    private final UsuarioDAO usuarioDAO;

    public VentaFacade() {
        this.ventaControl = VentaControl.getInstance();
        this.usuarioDAO = new UsuarioDAO();
    }

    public static VentaFacade getInstance() {
        return new VentaFacade();
    }

    private String obtenerClienteIdDefecto() {
        try {
            UsuarioEntidad usuario = usuarioDAO.obtenerPorEmail("cliente_default@local");
            if (usuario != null && usuario.getId() != null) {
                return usuario.getId().toString();
            }
            throw new IllegalStateException("Usuario por defecto no encontrado. Debe ser creado en la inicialización de la BD.");
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo obtener el cliente por defecto", e);
        }
    }

    @Override
    public CarritoDTO getCarritoActual() {
        String clienteId = obtenerClienteIdDefecto();
        CarritoEntidad carrito = ventaControl.obtenerCarrito(clienteId);
        return convertirCarritoADTO(carrito);
    }


    @Override
    public void removerItemDelCarrito(String productoId) {
        String clienteId = obtenerClienteIdDefecto();
        ventaControl.removerItemDelCarrito(clienteId, productoId);
    }

    @Override
    public boolean actualizarCantidadItem(String productoId, int nuevaCantidad) {
        String clienteId = obtenerClienteIdDefecto();
        return ventaControl.actualizarCantidadItem(clienteId, productoId, nuevaCantidad);
    }

    @Override
    public double calcularTotalCarrito() {
        String clienteId = obtenerClienteIdDefecto();
        return ventaControl.calcularTotalCarrito(clienteId);
    }

    @Override
    public String realizarPago(MetodoPagoDTO metodoPago) {
        String clienteId = obtenerClienteIdDefecto();
        return ventaControl.confirmarPedido(clienteId, metodoPago);
    }

    @Override
    public void vaciarCarrito() {
        String clienteId = obtenerClienteIdDefecto();
        ventaControl.vaciarCarrito(clienteId);
    }

    @Override
    public boolean verificarStockCarrito() {
        String clienteId = obtenerClienteIdDefecto();
        return ventaControl.verificarStockCarrito(clienteId);
    }

    @Override
    public String agregarConfiguracionAlCarrito(dto.EnsamblajeDTO ensamblaje) {
        String clienteId = obtenerClienteIdDefecto();

        IEnsamblajeControl fachadaControl = ConfiguracionFacade.getInstance();
        String configuracionId = fachadaControl.guardarConfiguracion(ensamblaje, clienteId);

        if (configuracionId == null) {
            return null;
        }

        return ventaControl.agregarConfiguracionAlCarrito(clienteId, configuracionId);
    }

    @Override
    public boolean agregarProductoAlCarrito(String productoId, int cantidad) {
        String clienteId = obtenerClienteIdDefecto();
        return ventaControl.agregarProductoAlCarrito(clienteId, productoId, cantidad);
    }


    @Override
    public List<ConfiguracionEntidad> obtenerConfiguracionesEnCarrito() {
        String clienteId = obtenerClienteIdDefecto();
        return ventaControl.obtenerConfiguracionesEnCarrito(clienteId);
    }

    @Override
    public boolean removerConfiguracionDelCarrito(String configuracionId) {
        String clienteId = obtenerClienteIdDefecto();
        return ventaControl.removerConfiguracionDelCarrito(clienteId, configuracionId);
    }

    private CarritoDTO convertirCarritoADTO(CarritoEntidad entidad) {
        if (entidad == null) return null;

        CarritoDTO dto = new CarritoDTO();
        dto.setClienteId(entidad.getClienteId());
        dto.setItems(new ArrayList<>());

        return dto;
    }
}
