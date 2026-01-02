package controlpresentacion;

import dto.ItemCarritoDTO;
import objetosNegocio.CarritoBO;
import ventacontrol.IVentaFacade;
import ventacontrol.VentaFacade;
import dto.ConfiguracionDTO;
import dto.EnsamblajeDTO;
import dto.MetodoPagoDTO;
import entidades.ConfiguracionEntidad;
import objetosNegocio.ConfiguracionBO;
import objetosNegocio.mappers.ConfiguracionMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del control de presentación para gestionar ventas y carritos.
 */
public class ControlPresentacionVenta implements IControlPresentacionVenta {
    private static ControlPresentacionVenta instancia;
    private final IVentaFacade ventaFacade;

    /**
     * Constructor para inyección de dependencias.
     * @param ventaFacade La fachada a utilizar.
     */
    public ControlPresentacionVenta(IVentaFacade ventaFacade) {
        this.ventaFacade = ventaFacade;
    }

    private ControlPresentacionVenta() {
        this(VentaFacade.getInstance());
    }

    public static synchronized ControlPresentacionVenta getInstance() {
        if (instancia == null) {
            instancia = new ControlPresentacionVenta();
        }
        return instancia;
    }

    @Override
    public void vaciarCarrito() {
        ventaFacade.vaciarCarrito();
    }

    @Override
    public double calcularTotalCarrito() {
        return ventaFacade.calcularTotalCarrito();
    }

    @Override
    public List<ConfiguracionDTO> obtenerConfiguracionesEnCarrito() {
        List<ConfiguracionEntidad> entidades = ventaFacade.obtenerConfiguracionesEnCarrito();

        if (entidades == null || entidades.isEmpty()) {
            return new ArrayList<>();
        }

        return entidades.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public String confirmarPedido(MetodoPagoDTO metodoPago) {
        return ventaFacade.realizarPago(metodoPago);
    }

    @Override
    public boolean agregarProductoAlCarrito(String productoId, int cantidad) {
        return ventaFacade.agregarProductoAlCarrito(productoId, cantidad);
    }

    @Override
    public void removerProductoDelCarrito(String productoId) {
        ventaFacade.removerItemDelCarrito(productoId);
    }

    @Override
    public boolean actualizarCantidadItem(String productoId, int nuevaCantidad) {
        return ventaFacade.actualizarCantidadItem(productoId, nuevaCantidad);
    }

    @Override
    public boolean verificarStockCarrito() {
        return ventaFacade.verificarStockCarrito();
    }

    @Override
    public String agregarConfiguracionAlCarrito(EnsamblajeDTO ensamblaje) {
        return ventaFacade.agregarConfiguracionAlCarrito(ensamblaje);
    }

    @Override
    public List<ItemCarritoDTO> obtenerProductosDelCarrito() {
        try {
            CarritoBO carritoBO = ventaFacade.obtenerCarrito();

            if (carritoBO == null || carritoBO.getProductos() == null || carritoBO.getProductos().isEmpty()) {
                System.out.println("DEBUG: Carrito vacío o nulo");
                return new ArrayList<>();
            }

            System.out.println("DEBUG: Carrito tiene " + carritoBO.getProductos().size() + " productos");

            // Convertir ItemCarritoBO a ItemCarritoDTO
            List<ItemCarritoDTO> items = new ArrayList<>();
            for (CarritoBO.ItemCarritoBO itemBO : carritoBO.getProductos()) {
                System.out.println("DEBUG: Item - ID: " + itemBO.getProductoId() +
                    ", Nombre: " + itemBO.getNombre() +
                    ", Precio: " + itemBO.getPrecio() +
                    ", Cantidad: " + itemBO.getCantidad());

                ItemCarritoDTO itemDTO = new ItemCarritoDTO();
                itemDTO.setProductoId(itemBO.getProductoId());
                itemDTO.setNombre(itemBO.getNombre());
                itemDTO.setCantidad(itemBO.getCantidad());
                itemDTO.setPrecioUnitario(itemBO.getPrecio() != null ? itemBO.getPrecio().doubleValue() : 0.0);
                items.add(itemDTO);
            }

            return items;
        } catch (Exception e) {
            System.err.println("Error al obtener productos del carrito: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Convierte una ConfiguracionEntidad a ConfiguracionDTO.
     * Usa ConfiguracionMapper para hacer la conversión.
     */
    private ConfiguracionDTO convertirADTO(ConfiguracionEntidad entidad) {
        if (entidad == null) {
            return null;
        }

        ConfiguracionBO bo = ConfiguracionMapper.entidadABO(entidad);
        return ConfiguracionMapper.boADTO(bo);
    }
}
