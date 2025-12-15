package controlvista;

import compartido.FramePrincipal;
import armadoPC.ArmarPcPanel;
import dto.ComponenteDTO;
import dto.ConfiguracionDTO;
import dto.EnsamblajeDTO;
import entidades.ProductoEntidad;
import venta.carrito.CarritoPanel;
import venta.pedido.ConfirmarDetallesPedidoPanel;
import venta.producto.ProductoPanel;
import compartido.BarraNavegacion;
import menuprincipal.MenuPrincipalPanel;
import javax.swing.*;
import java.util.List;
import java.util.function.Consumer;
import controlpresentacion.ControlPresentacionArmado;
import controlpresentacion.ControlPresentacionVenta;
import controlpresentacion.IControlPresentacionArmado;
import controlpresentacion.IControlPresentacionVenta;
import ventacontrol.IVentaFacade;
import ventacontrol.VentaFacade;

/**
 * Controlador principal de navegación entre pantallas principales.
 *
 * PATRÓN: Mediador
 */
public class ControlDeNavegacion implements IControlDeNavegacion {

    private static final String MENSAJE_CONFIG_VACIA = "No hay configuración para ";
    private static final String TITULO_EXITO = "Éxito";
    private static final String TITULO_ERROR = "Error";

    private final FramePrincipal frame;
    private final ArmarPcPanel armarPcPanel;
    private final CarritoPanel carritoPanel;
    private final ConfirmarDetallesPedidoPanel confirmarDetallesPedidoPanel;
    private final ProductoPanel productoPanel;
    private final MenuPrincipalPanel menuPrincipalPanel;
    private final IControlPresentacionVenta controlVenta;
    private final IControlPresentacionArmado controlPresentacionArmado;
    private final ControlFlujoArmado controlFlujoArmado;

    public ControlDeNavegacion(FramePrincipal frame) {
        this.frame = frame;
        this.armarPcPanel = new ArmarPcPanel();
        this.carritoPanel = new CarritoPanel(frame);
        this.confirmarDetallesPedidoPanel = new ConfirmarDetallesPedidoPanel();
        this.productoPanel = new ProductoPanel();
        this.menuPrincipalPanel = new MenuPrincipalPanel();

        BarraNavegacion barraNavegacion = new BarraNavegacion();
        IVentaFacade ventaFacade = VentaFacade.getInstance();
        this.controlVenta = new ControlPresentacionVenta(ventaFacade);
        this.controlPresentacionArmado = new ControlPresentacionArmado();
        this.controlFlujoArmado = new ControlFlujoArmado(armarPcPanel, controlPresentacionArmado, frame);

        this.frame.setBarraDeNavegacion(barraNavegacion);
        this.frame.cambiarPanel(menuPrincipalPanel);

        inicializarVista();
        configurarBarraNavegacion(barraNavegacion);
        configurarCallbacksGuardarConfiguracion();
        configurarCallbacksAgregarAlCarrito();
        configurarCallbacksCarrito();
    }

    private void inicializarVista() {
        menuPrincipalPanel.setOnProductoSeleccionado(this::mostrarProducto);
        cargarProductosDestacados();
        frame.cambiarPanel(menuPrincipalPanel);
        frame.setVisible(true);
    }

    /**
     * Carga 5 productos aleatorios como productos destacados en el menú principal.
     * Los ProductoCard se configuran con el callback para abrir ProductoPanel.
     */
    private void cargarProductosDestacados() {
        try {
            List<dto.ComponenteDTO> productosDestacados = controlPresentacionArmado.obtenerProductosAleatorios(5);
            menuPrincipalPanel.cargarProductosDestacados(productosDestacados);

            menuPrincipalPanel.setOnProductoSeleccionado(this::mostrarProducto);
        } catch (Exception e) {
            System.err.println("Error al cargar productos destacados: " + e.getMessage());
        }
    }

    private void configurarBarraNavegacion(BarraNavegacion barra) {
        barra.getBoton().addActionListener(e -> mostrarMenuPrincipal());
        barra.getArmarPcBtn().addActionListener(e -> mostrarArmarPc());
        barra.getCarritoBtn().addActionListener(e -> mostrarCarrito());
    }


    private void configurarCallbacksGuardarConfiguracion() {
        Consumer<Void> guardarConfigHandler = unused -> {
            try {
                EnsamblajeDTO ensamblaje = controlPresentacionArmado.getEnsamblajeActual();

                if (ensamblaje == null || ensamblaje.obtenerTodosComponentes().isEmpty()) {
                    mostrarMensaje(MENSAJE_CONFIG_VACIA + "guardar", "Configuración vacía", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String configuracionId = controlVenta.agregarConfiguracionAlCarrito(ensamblaje);

                if (configuracionId != null) {
                    mostrarMensaje("Configuración guardada exitosamente", TITULO_EXITO, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    mostrarMensaje("Error al guardar la configuración", TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                manejarExcepcion("Error al guardar configuración", ex);
            }
        };

        armarPcPanel.getMenuOpcionesPanel().setOnGuardarConfiguracion(guardarConfigHandler);
    }

    private void configurarCallbacksAgregarAlCarrito() {
        Consumer<Void> agregarCarritoHandler = unused -> {
            try {
                EnsamblajeDTO ensamblaje = controlPresentacionArmado.getEnsamblajeActual();

                if (ensamblaje == null || ensamblaje.obtenerTodosComponentes().isEmpty()) {
                    mostrarMensaje(MENSAJE_CONFIG_VACIA + "añadir al carrito", "Configuración vacía", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String configuracionId = controlVenta.agregarConfiguracionAlCarrito(ensamblaje);

                if (configuracionId != null) {
                    limpiarConfiguracionActual();
                    mostrarMensaje("Configuración añadida al carrito exitosamente", TITULO_EXITO, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    mostrarMensaje("Error al añadir la configuración al carrito", TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                manejarExcepcion("Error al añadir al carrito", ex);
            }
        };

        armarPcPanel.getMenuOpcionesPanel().setOnAgregarAlCarrito(agregarCarritoHandler);
    }

    private void configurarCallbacksCarrito() {
        carritoPanel.setOnRealizarPedido(() -> {
            try {
                List<ConfiguracionDTO> configuraciones = controlVenta.obtenerConfiguracionesEnCarrito();
                double totalCarrito = controlVenta.calcularTotalCarrito();

                if ((configuraciones == null || configuraciones.isEmpty()) && totalCarrito == 0.0) {
                    JOptionPane.showMessageDialog(
                        frame,
                        "No hay productos en el carrito para realizar el pedido.",
                        "Carrito vacío",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                mostrarNuevaPantalla(confirmarDetallesPedidoPanel);
                confirmarDetallesPedidoPanel.actualizarContenido();
            } catch (Exception ex) {
                System.err.println("Error al verificar el carrito: " + ex.getMessage());
                JOptionPane.showMessageDialog(
                    frame,
                    "Error al verificar el carrito: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        confirmarDetallesPedidoPanel.setOnPedidoConfirmado(() -> {
            mostrarNuevaPantalla(menuPrincipalPanel);

            carritoPanel.actualizarCarrito();
        });
    }

    private void mostrarNuevaPantalla(JPanel nuevoPanel) {
        frame.cambiarPanel(nuevoPanel);
        if (nuevoPanel == carritoPanel) {
            carritoPanel.actualizarCarrito();
        }
    }

    @Override
    public void mostrarProducto(String productoId) {
        ComponenteDTO producto = controlPresentacionArmado.convertirProductoADTO(productoId);
        if (producto != null) {
            mostrarProducto(producto);
        } else {
            mostrarMensaje("Producto no encontrado", TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void mostrarProducto(Object producto) {
        productoPanel.cargarProducto(producto);
        configurarListenerAgregarAlCarrito();
        mostrarNuevaPantalla(productoPanel);
    }

    private void configurarListenerAgregarAlCarrito() {
        productoPanel.limpiarListeners();

        productoPanel.setOnAgregarAlCarrito(unused -> {
            Object productoActual = productoPanel.getProductoActual();
            int cantidad = productoPanel.getCantidadSeleccionada();

            if (productoActual == null) {
                mostrarMensaje("No hay producto seleccionado", TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (productoActual instanceof entidades.ProductoEntidad) {
                agregarProductoEntidadAlCarrito((entidades.ProductoEntidad) productoActual, cantidad);
            } else if (productoActual instanceof dto.ComponenteDTO) {
                agregarComponenteDTOAlCarrito((dto.ComponenteDTO) productoActual, cantidad);
            } else {
                mostrarMensaje("Tipo de producto no soportado", TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void agregarProductoEntidadAlCarrito(ProductoEntidad producto, int cantidad) {
        try {
            String productoId = producto.getId().toString();
            String nombreProducto = producto.getNombre();

            boolean agregado = controlVenta.agregarProductoAlCarrito(productoId, cantidad);

            if (agregado) {
                mostrarMensaje(
                    cantidad + " unidad(es) de " + nombreProducto + " agregadas al carrito",
                    "Producto Agregado",
                    JOptionPane.INFORMATION_MESSAGE
                );
                carritoPanel.actualizarCarrito();
            } else {
                mostrarMensaje("Error al agregar el producto al carrito", TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            manejarExcepcion("Error al agregar producto entidad al carrito", ex);
        }
    }

    private void agregarComponenteDTOAlCarrito(ComponenteDTO componente, int cantidad) {
        try {
            String productoId = componente.getId();
            String nombreProducto = componente.getNombre();

            boolean agregado = controlVenta.agregarProductoAlCarrito(productoId, cantidad);

            if (agregado) {
                mostrarMensaje(
                    cantidad + " unidad(es) de " + nombreProducto + " agregadas al carrito",
                    "Producto Agregado",
                    JOptionPane.INFORMATION_MESSAGE
                );
                carritoPanel.actualizarCarrito();
            } else {
                mostrarMensaje("Error al agregar el producto al carrito", TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            manejarExcepcion("Error al agregar componente DTO al carrito", ex);
        }
    }


    private void limpiarConfiguracionActual() {
        controlFlujoArmado.limpiarEstado();
        carritoPanel.actualizarCarrito();
    }

    private void mostrarMensaje(String mensaje, String titulo, int tipoMensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, titulo, tipoMensaje);
    }

    private void manejarExcepcion(String contexto, Exception ex) {
        System.err.println(contexto + ": " + ex.getMessage());
        mostrarMensaje(contexto + ": " + ex.getMessage(), TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
    }


    @Override
    public void mostrarMenuPrincipal() {
        frame.cambiarPanel(menuPrincipalPanel);
    }

    @Override
    public void mostrarArmarPc() {
        controlPresentacionArmado.iniciarNuevoEnsamblaje();
        controlFlujoArmado.iniciar();
        frame.cambiarPanel(armarPcPanel);
    }

    @Override
    public void mostrarCarrito() {
        carritoPanel.actualizarCarrito();
        frame.cambiarPanel(carritoPanel);
    }

    public void mostrarConfirmarPedido() {
        frame.cambiarPanel(confirmarDetallesPedidoPanel);
    }
}
