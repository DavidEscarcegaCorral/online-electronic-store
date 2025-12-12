package controlvista;

import compartido.FramePrincipal;
import armadoPC.ArmarPcPanel;
import venta.carrito.CarritoPanel;
import venta.pedido.ConfirmarDetallesPedidoPanel;
import venta.producto.ProductoPanel;
import compartido.BarraNavegacion;
import menuprincipal.MenuPrincipalPanel;
import javax.swing.*;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import dao.ProductoDAO;
import controlconfig.FachadaControl;

/**
 * Controlador principal de navegación entre pantallas y gestión del flujo de armado de PC.
 * Coordina la comunicación entre la vista y las fachadas de negocio.
 * Implementa IControlDeNavegacion para definir un contrato claro y permitir testing.
 */
public class ControlDeNavegacion implements IControlDeNavegacion {

    private static final int INDICE_CATEGORIA = 0;
    private static final int INDICE_MARCA_PROCESADOR = 1;
    private static final int INDICE_PROCESADOR = 2;
    private static final int INDICE_TARJETA_MADRE = 3;
    private static final int INDICE_MEMORIA_RAM = 4;
    private static final int INDICE_TARJETA_VIDEO = 5;
    private static final int INDICE_ALMACENAMIENTO = 6;
    private static final int INDICE_FUENTE_PODER = 7;
    private static final int INDICE_GABINETE = 8;
    private static final int INDICE_DISIPADOR = 9;
    private static final int INDICE_VENTILADOR = 10;
    private static final int INDICE_MONITOR = 11;
    private static final int INDICE_KIT_TECLADO_RATON = 12;
    private static final int INDICE_RED = 13;
    private static final int INDICE_RESUMEN = 14;

    private static final Set<Integer> PASOS_OBLIGATORIOS = Set.of(
        INDICE_PROCESADOR,
        INDICE_TARJETA_MADRE,
        INDICE_MEMORIA_RAM,
        INDICE_GABINETE,
        INDICE_TARJETA_VIDEO,
        INDICE_FUENTE_PODER,
        INDICE_DISIPADOR
    );

    private static final int MINIMO_POR_CATEGORIA = 2;

    private static final String MENSAJE_ERROR_PRODUCTO_NO_ENCONTRADO = "Producto no encontrado";
    private static final String MENSAJE_CONFIG_VACIA = "No hay configuración para ";
    private static final String TITULO_EXITO = "Éxito";
    private static final String TITULO_ERROR = "Error";

    private final FramePrincipal framePrincipal;
    private final MenuPrincipalPanel menuPrincipalPanel;
    private final ArmarPcPanel armarEquipoPantalla;
    private final CarritoPanel carritoPantalla;
    private final ConfirmarDetallesPedidoPanel confirmarDetallesPedidoPanel;
    private final ProductoPanel productoPantalla;


    private int indiceActual = 0;
    private String seleccionCategoria = null;
    private String seleccionMarca = null;
    private boolean categoriaConfirmada = false;
    private boolean marcaConfirmada = false;

    public ControlDeNavegacion(FramePrincipal framePrincipal) {
        this.framePrincipal = framePrincipal;
        this.menuPrincipalPanel = new MenuPrincipalPanel();
        this.armarEquipoPantalla = new ArmarPcPanel();
        this.carritoPantalla = new CarritoPanel();
        this.confirmarDetallesPedidoPanel = new ConfirmarDetallesPedidoPanel();
        this.productoPantalla = new ProductoPanel();

        inicializarVista();
        configurarBarraNavegacion();
        configurarBotonesComponentes();
        configurarNavegacionInterna();
        configurarCallbacksNegocio();
        configurarCallbacksGuardarConfiguracion();
        configurarCallbacksAgregarAlCarrito();
        configurarCallbacksCarrito();
    }

    private void inicializarVista() {
        menuPrincipalPanel.setOnProductoSeleccionado(productId -> {
            try {
                dao.ProductoDAO productoDAO = new dao.ProductoDAO();
                entidades.ProductoEntidad producto = productoDAO.obtenerPorId(productId);
                if (producto != null) {
                    mostrarProducto(producto);
                }
            } catch (Exception e) {
            }
        });
        framePrincipal.setPanelContenido(menuPrincipalPanel);
        framePrincipal.setVisible(true);
        armarEquipoPantalla.mostrarMenusLaterales();
    }

    private void configurarBarraNavegacion() {
        BarraNavegacion barra = framePrincipal.getBarraNavegacion();

        barra.getBoton().addActionListener(e -> mostrarNuevaPantalla(menuPrincipalPanel));

        barra.getArmarPcBtn().addActionListener(e -> {
            mostrarNuevaPantalla(armarEquipoPantalla);
            mostrarPasoInicialArmar();
        });

        barra.getCarritoBtn().addActionListener(e -> mostrarNuevaPantalla(carritoPantalla));
    }

    private void configurarBotonesComponentes() {
        var menuComponentes = armarEquipoPantalla.getMenuComponentesPanel();
        if (menuComponentes == null) return;

        // Botones para categoría y marca procesador
        menuComponentes.getCategoriasBtn()
            .addActionListener(e -> navegarAIndice(INDICE_CATEGORIA));

        menuComponentes.getMarcaProcesadorBtn()
            .addActionListener(e -> navegarAIndice(INDICE_MARCA_PROCESADOR));

        // Mapear cada botón al índice correcto en PASOS
        menuComponentes.getProcesadorBtn()
            .addActionListener(e -> navegarAIndice(INDICE_PROCESADOR));

        menuComponentes.getTarjetaMadreBtn()
            .addActionListener(e -> navegarAIndice(INDICE_TARJETA_MADRE));

        menuComponentes.getMemoriaRAMBtn()
            .addActionListener(e -> navegarAIndice(INDICE_MEMORIA_RAM));

        menuComponentes.getTarjetaDeVideoBtn()
            .addActionListener(e -> navegarAIndice(INDICE_TARJETA_VIDEO));

        // Unidad SSD y Almacenamiento ambos llevan al paso 'Almacenamiento'
        menuComponentes.getAlmacenamientoBtn()
            .addActionListener(e -> navegarAIndice(INDICE_ALMACENAMIENTO));
        menuComponentes.getUnidadSSDBtn()
            .addActionListener(e -> navegarAIndice(INDICE_ALMACENAMIENTO));

        menuComponentes.getFuenteDePoderBtn()
            .addActionListener(e -> navegarAIndice(INDICE_FUENTE_PODER));

        // Nuevo: Gabinete
        menuComponentes.getGabineteBtn()
            .addActionListener(e -> navegarAIndice(INDICE_GABINETE));

        menuComponentes.getDisipadorBtn()
            .addActionListener(e -> navegarAIndice(INDICE_DISIPADOR));

        menuComponentes.getVentiladorBtn()
            .addActionListener(e -> navegarAIndice(INDICE_VENTILADOR));

        menuComponentes.getMonitorBtn()
            .addActionListener(e -> navegarAIndice(INDICE_MONITOR));

        menuComponentes.getKitTecladoRatonBtn()
            .addActionListener(e -> navegarAIndice(INDICE_KIT_TECLADO_RATON));

        menuComponentes.getRedBtn()
            .addActionListener(e -> navegarAIndice(INDICE_RED));

        menuComponentes.getResumenConfiguracionBtn()
            .addActionListener(e -> navegarAIndice(INDICE_RESUMEN));
    }

    private void configurarNavegacionInterna() {
        var siguiente = armarEquipoPantalla.getContinuarBtn();
        var retroceder = armarEquipoPantalla.getRetrocederBtn();
        if (siguiente != null) {
            siguiente.addActionListener(e -> avanzarPaso());
        }
        if (retroceder != null) {
            retroceder.addActionListener(e -> retrocederPaso());
        }
    }

    private void configurarCallbacksNegocio() {
        armarEquipoPantalla.setOnProductoSelected(this::procesarSeleccionProducto);
        armarEquipoPantalla.setOnCategoriaSeleccionada(this::procesarSeleccionCategoria);
        armarEquipoPantalla.setOnMarcaSeleccionada(this::procesarSeleccionMarca);
    }

    private void procesarSeleccionProducto(String productoId) {
        try {
            dto.ComponenteDTO componente = convertirAComponenteDTO(productoId);
            if (componente == null) {
                mostrarMensaje(MENSAJE_ERROR_PRODUCTO_NO_ENCONTRADO, TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
                return;
            }

            FachadaControl fachada = FachadaControl.getInstance();
            List<String> errores = fachada.agregarComponente(componente);

            // Los errores no deberían ocurrir ya que el catálogo muestra solo productos compatibles
            // Pero los manejamos por si acaso
            if (!errores.isEmpty()) {
                System.err.println("Advertencia: Se encontraron errores de compatibilidad inesperados: " + String.join(", ", errores));
                // No mostramos mensaje al usuario, ya que el catálogo debería prevenir esto
            }

            armarEquipoPantalla.updateResumen(fachada.getEnsamblajeActual());
            var btn = armarEquipoPantalla.getContinuarBtn();
            if (btn != null) btn.setEnabled(true);
        } catch (Exception ex) {
            manejarExcepcion("Error procesando selección", ex);
        }
    }

    private void procesarSeleccionCategoria(String categoria) {
        this.seleccionCategoria = categoria;
        this.categoriaConfirmada = false;

        if (categoria == null) {
            armarEquipoPantalla.habilitarSoloCategorias();
            controlpresentacion.ControlPresentacion.getInstance().seleccionarCategoria(null);
            SwingUtilities.invokeLater(armarEquipoPantalla::actualizarEstadoContinuarDesdeUI);
            return;
        }

        // Validar que hay suficientes productos para configuración básica
        FachadaControl fachada = FachadaControl.getInstance();
        if (!fachada.tieneMinimoPorCategoria("Procesador", MINIMO_POR_CATEGORIA)) {
            mostrarMensaje(
                "No hay suficientes procesadores disponibles para la categoría seleccionada.",
                "Inventario Insuficiente",
                JOptionPane.WARNING_MESSAGE
            );
            armarEquipoPantalla.getCategoriasPanel().limpiarSeleccion();
            armarEquipoPantalla.habilitarSoloCategorias();
            return;
        }

        // Validar componentes mínimos necesarios
        String[] componentesObligatorios = {"Procesador", "Tarjeta Madre", "RAM", "Gabinete", "Tarjeta de video", "Fuente de poder", "Disipador"};
        for (String componente : componentesObligatorios) {
            if (!fachada.tieneMinimoPorCategoria(componente, MINIMO_POR_CATEGORIA)) {
                mostrarMensaje(
                    "No hay suficientes productos de '" + componente + "' disponibles para armar una configuración básica.",
                    "Inventario Insuficiente",
                    JOptionPane.WARNING_MESSAGE
                );
                armarEquipoPantalla.getCategoriasPanel().limpiarSeleccion();
                armarEquipoPantalla.habilitarSoloCategorias();
                return;
            }
        }

        // Si pasa las validaciones, confirmar categoría
        this.categoriaConfirmada = true;
        armarEquipoPantalla.habilitarCategoriasYMarca();
        controlpresentacion.ControlPresentacion.getInstance().seleccionarCategoria(categoria);
        SwingUtilities.invokeLater(armarEquipoPantalla::actualizarEstadoContinuarDesdeUI);
    }

    private void procesarSeleccionMarca(String marca) {
        this.seleccionMarca = marca;
        this.marcaConfirmada = false;

        if (marca == null) {
            armarEquipoPantalla.habilitarCategoriasYMarca();
            controlpresentacion.ControlPresentacion.getInstance().seleccionarMarca(null);
            SwingUtilities.invokeLater(armarEquipoPantalla::actualizarEstadoContinuarDesdeUI);
            return;
        }

        // Validar que hay categoría seleccionada primero
        if (!categoriaConfirmada || seleccionCategoria == null) {
            mostrarMensaje(
                "Debe seleccionar una categoría antes de elegir marca de procesador.",
                "Categoría no seleccionada",
                JOptionPane.WARNING_MESSAGE
            );
            armarEquipoPantalla.getMarcasPanel().limpiarSeleccion();
            armarEquipoPantalla.habilitarCategoriasYMarca();
            return;
        }

        // Validar que hay procesadores de esa marca en inventario para la categoría seleccionada
        FachadaControl fachada = FachadaControl.getInstance();
        if (!fachada.tieneMarcaEnCategoria("Procesador", marca)) {
            mostrarMensaje(
                "No hay procesadores de la marca '" + marca + "' disponibles en inventario.",
                "Marca no disponible",
                JOptionPane.WARNING_MESSAGE
            );
            armarEquipoPantalla.getMarcasPanel().limpiarSeleccion();
            armarEquipoPantalla.habilitarCategoriasYMarca();
            return;
        }

        // Si pasa las validaciones, confirmar marca
        this.marcaConfirmada = true;
        controlpresentacion.ControlPresentacion.getInstance().seleccionarMarca(marca);
        armarEquipoPantalla.habilitarTodo();
        SwingUtilities.invokeLater(armarEquipoPantalla::actualizarEstadoContinuarDesdeUI);
    }

    private void configurarCallbacksGuardarConfiguracion() {
        Consumer<Void> guardarConfigHandler = v -> {
            try {
                FachadaControl fachada = FachadaControl.getInstance();
                dto.EnsamblajeDTO ensamblaje = fachada.getEnsamblajeActual();

                if (!validarEnsamblaje(ensamblaje)) {
                    mostrarMensaje(MENSAJE_CONFIG_VACIA + "guardar", "Configuración vacía", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                controlconfig.IVentaFacade ventaFacade = controlconfig.VentaFacade.getInstance();
                String configuracionId = ventaFacade.agregarConfiguracionAlCarrito(ensamblaje);

                if (configuracionId != null) {
                    mostrarMensaje("Configuración guardada exitosamente", TITULO_EXITO, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    mostrarMensaje("Error al guardar la configuración", TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                manejarExcepcion("Error al guardar configuración", ex);
            }
        };

        armarEquipoPantalla.getMenuOpcionesPanel().setOnGuardarConfiguracion(guardarConfigHandler);
    }

    private void configurarCallbacksAgregarAlCarrito() {
        Consumer<Void> agregarCarritoHandler = v -> {
            try {
                FachadaControl fachada = FachadaControl.getInstance();
                dto.EnsamblajeDTO ensamblaje = fachada.getEnsamblajeActual();

                if (!validarEnsamblaje(ensamblaje)) {
                    mostrarMensaje(MENSAJE_CONFIG_VACIA + "añadir al carrito", "Configuración vacía", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                controlconfig.IVentaFacade ventaFacade = controlconfig.VentaFacade.getInstance();
                String configuracionId = ventaFacade.agregarConfiguracionAlCarrito(ensamblaje);

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

        armarEquipoPantalla.getMenuOpcionesPanel().setOnAgregarAlCarrito(agregarCarritoHandler);
    }

    private void configurarCallbacksCarrito() {
        carritoPantalla.setOnRealizarPedido(() -> {
            try {
                controlconfig.IVentaFacade ventaFacade = controlconfig.VentaFacade.getInstance();
                java.util.List<entidades.ConfiguracionEntidad> configuraciones = ventaFacade.obtenerConfiguracionesEnCarrito();

                if (configuraciones == null || configuraciones.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        framePrincipal,
                        "No hay productos en el carrito para realizar el pedido.",
                        "Carrito vacío",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                mostrarNuevaPantalla(confirmarDetallesPedidoPanel);
                confirmarDetallesPedidoPanel.actualizarContenido();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    framePrincipal,
                    "Error al verificar el carrito: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Configurar callback para cuando se confirme el pedido
        confirmarDetallesPedidoPanel.setOnPedidoConfirmado(() -> {
            // Regresar al menú principal después de confirmar el pedido
            mostrarNuevaPantalla(menuPrincipalPanel);

            // Actualizar el carrito para reflejar que está vacío
            carritoPantalla.actualizarCarrito();
        });
    }

    private void mostrarNuevaPantalla(JPanel nuevoPanel) {
        framePrincipal.setPanelContenido(nuevoPanel);
        if (nuevoPanel == armarEquipoPantalla) {
            armarEquipoPantalla.mostrarMenusLaterales();
            armarEquipoPantalla.habilitarSoloCategorias();

            var contBtn = armarEquipoPantalla.getContinuarBtn();
            controlpresentacion.ControlPresentacion cfg = controlpresentacion.ControlPresentacion.getInstance();
            String uiCategoriaSel = armarEquipoPantalla.getCategoriasPanel().getSeleccionActual();
            boolean enable = uiCategoriaSel != null || cfg.getCategoriaActual() != null;
            if (contBtn != null) contBtn.setEnabled(enable);
        } else if (nuevoPanel == carritoPantalla) {
            carritoPantalla.actualizarCarrito();
        }
    }

    private void mostrarPasoInicialArmar() {
        String[] pasos = armarEquipoPantalla.getListaPasosArmado();
        if (pasos != null && pasos.length > 0) {
            indiceActual = 0;
            armarEquipoPantalla.mostrarPaso(0);
            var cont = armarEquipoPantalla.getContinuarBtn();
            if (cont != null) cont.setEnabled(true);
        }
    }

    public void mostrarProducto(Object producto) {
        productoPantalla.cargarProducto(producto);
        configurarListenerAgregarAlCarrito();
        mostrarNuevaPantalla(productoPantalla);
    }

    private void configurarListenerAgregarAlCarrito() {
        productoPantalla.limpiarListeners();

        productoPantalla.setOnAgregarAlCarrito(e -> {
            try {
                Object productoActual = productoPantalla.getProductoActual();
                int cantidad = productoPantalla.getCantidadSeleccionada();

                if (productoActual != null) {
                    Object idObj = productoActual.getClass().getMethod("getId").invoke(productoActual);
                    String productoId = idObj.toString();
                    String nombreProducto = (String) productoActual.getClass().getMethod("getNombre").invoke(productoActual);

                    controlconfig.IVentaFacade ventaFacade = controlconfig.VentaFacade.getInstance();
                    boolean agregado = ventaFacade.agregarProductoAlCarrito(productoId, cantidad);

                    if (agregado) {
                        JOptionPane.showMessageDialog(
                            framePrincipal,
                            cantidad + " unidad(es) de " + nombreProducto + " agregadas al carrito",
                            "Producto Agregado",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        carritoPantalla.actualizarCarrito();
                    } else {
                        JOptionPane.showMessageDialog(
                            framePrincipal,
                            "Error al agregar el producto al carrito",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    framePrincipal,
                    "Error al agregar el producto: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    @Override
    public void navegarAIndice(int nuevoIndice) {
        if (!esIndiceValido(nuevoIndice)) return;

        // Validar que no se puede avanzar más allá de categoría sin seleccionarla
        if (nuevoIndice > INDICE_CATEGORIA && !categoriaConfirmada) {
            mostrarMensaje(
                "Debe seleccionar una categoría antes de continuar.",
                "Categoría requerida",
                JOptionPane.WARNING_MESSAGE
            );
            navegarAIndice(INDICE_CATEGORIA);
            return;
        }

        // Validar que no se puede avanzar más allá de marca sin seleccionarla
        if (nuevoIndice > INDICE_MARCA_PROCESADOR && !marcaConfirmada) {
            mostrarMensaje(
                "Debe seleccionar una marca de procesador antes de continuar.",
                "Marca requerida",
                JOptionPane.WARNING_MESSAGE
            );
            navegarAIndice(INDICE_MARCA_PROCESADOR);
            return;
        }

        String[] pasos = armarEquipoPantalla.getListaPasosArmado();
        if (nuevoIndice < indiceActual && nuevoIndice >= INDICE_PROCESADOR) {
            FachadaControl fachada = FachadaControl.getInstance();
            String categoriaActual = pasos[nuevoIndice];

            // Remover componentes posteriores para evitar incompatibilidades
            fachada.removerComponentesPosteriores(categoriaActual);

            // Actualizar resumen después de limpiar
            armarEquipoPantalla.updateResumen(fachada.getEnsamblajeActual());
        }

        armarEquipoPantalla.mostrarMenusLaterales();

        indiceActual = nuevoIndice;
        armarEquipoPantalla.mostrarPaso(nuevoIndice);

        var contBtn = armarEquipoPantalla.getContinuarBtn();
        boolean contEnabled = false;

        controlpresentacion.ControlPresentacion cfg = controlpresentacion.ControlPresentacion.getInstance();
        String cfgCategoria = cfg.getCategoriaActual();
        String cfgMarca = cfg.getMarcaActual();
        FachadaControl fachada = FachadaControl.getInstance();

        if (nuevoIndice == 0) {
            armarEquipoPantalla.habilitarSoloCategorias();
            contEnabled = armarEquipoPantalla.getCategoriasPanel().getSeleccionActual() != null || cfgCategoria != null || seleccionCategoria != null || categoriaConfirmada;
        } else if (nuevoIndice == 1) {
            armarEquipoPantalla.habilitarCategoriasYMarca();
            contEnabled = armarEquipoPantalla.getMarcasPanel().getSeleccionActual() != null || cfgMarca != null || seleccionMarca != null || marcaConfirmada;
        } else {
            String pasoCat = pasos[nuevoIndice];
            boolean selCatPresent = armarEquipoPantalla.getCategoriasPanel().getSeleccionActual() != null || cfgCategoria != null || seleccionCategoria != null || categoriaConfirmada;
            boolean selMarcaPresent = armarEquipoPantalla.getMarcasPanel().getSeleccionActual() != null || cfgMarca != null || seleccionMarca != null || marcaConfirmada;

            if (selCatPresent && selMarcaPresent) {
                armarEquipoPantalla.habilitarTodo();
            } else {
                armarEquipoPantalla.habilitarCategoriasYMarca();
            }

            if (PASOS_OBLIGATORIOS.contains(nuevoIndice)) {
                contEnabled = (fachada.getComponenteSeleccionado(pasoCat) != null) || armarEquipoPantalla.haySeleccionEnCatalogo(pasoCat);
            } else {
                contEnabled = true;
            }
        }

        if (contBtn != null) contBtn.setEnabled(contEnabled);

        if (nuevoIndice >= 2 && nuevoIndice < pasos.length - 1) {
            boolean ok = fachada.tieneMinimoPorCategoria(pasos[nuevoIndice], MINIMO_POR_CATEGORIA);
            if (!ok) {
                JOptionPane.showMessageDialog(framePrincipal,
                        "No hay suficientes productos disponibles en la categoría '" + pasos[nuevoIndice] + "' para continuar.",
                        "Catálogo incompleto",
                        JOptionPane.WARNING_MESSAGE);
                int anterior = Math.max(indiceActual - 1, 0);
                armarEquipoPantalla.mostrarPaso(anterior);
                return;
            }

            // Validación especial para procesadores: verificar que haya productos de la marca seleccionada
            if (nuevoIndice == INDICE_PROCESADOR && marcaConfirmada && seleccionMarca != null) {
                if (!fachada.tieneMarcaEnCategoria("Procesador", seleccionMarca)) {
                    mostrarMensaje(
                        "No hay procesadores de la marca '" + seleccionMarca + "' disponibles en inventario.",
                        "Marca no disponible",
                        JOptionPane.WARNING_MESSAGE
                    );
                    navegarAIndice(INDICE_MARCA_PROCESADOR);
                    return;
                }
                // catálogo filtrado por marca
                armarEquipoPantalla.cargarCatalogoConMarca(pasos[nuevoIndice], seleccionMarca);
            } else {
                // cargar sin filtro de marca
                armarEquipoPantalla.cargarCatalogo(pasos[nuevoIndice]);
            }
        }

        if (nuevoIndice == INDICE_RESUMEN) {
            try {
                dto.EnsamblajeDTO ensamblaje = fachada.getEnsamblajeActual();
                armarEquipoPantalla.updateResumen(ensamblaje);
                armarEquipoPantalla.mostrarMenuOpcionesEnLateral();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            armarEquipoPantalla.mostrarResumenEnLateral();
        }

        SwingUtilities.invokeLater(() -> armarEquipoPantalla.actualizarEstadoContinuarDesdeUI());
     }

    private dto.ComponenteDTO convertirAComponenteDTO(String productoId) {
        try {
            ProductoDAO productoDAO = new ProductoDAO();
            entidades.ProductoEntidad producto = productoDAO.obtenerPorId(productoId);
            if (producto == null) return null;
            dto.ComponenteDTO dto = new dto.ComponenteDTO();
            dto.setId(producto.getId().toString());
            dto.setNombre(producto.getNombre());
            dto.setPrecio(producto.getPrecio());
            dto.setCategoria(producto.getCategoria());
            dto.setMarca(producto.getMarca());
            if (producto.getEspecificaciones() != null) {
                dto.setSocket(producto.getEspecificaciones().get("socket"));
                dto.setTipoRam(producto.getEspecificaciones().get("tipoRam"));
                dto.setFormFactor(producto.getEspecificaciones().get("formFactor"));
            }
            return dto;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private boolean esIndiceValido(int indice) {
        String[] pasos = armarEquipoPantalla.getListaPasosArmado();
        return pasos != null && indice >= 0 && indice < pasos.length;
    }

    @Override
    public void avanzarPaso() {
        // Validaciones antes de avanzar desde categoría
        if (indiceActual == INDICE_CATEGORIA) {
            if (!categoriaConfirmada) {
                mostrarMensaje(
                    "Debe seleccionar una categoría antes de continuar.",
                    "Categoría requerida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        }

        // Validaciones antes de avanzar desde marca
        if (indiceActual == INDICE_MARCA_PROCESADOR) {
            if (!marcaConfirmada) {
                mostrarMensaje(
                    "Debe seleccionar una marca de procesador antes de continuar.",
                    "Marca requerida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        }

        int siguiente = Math.min(indiceActual + 1, armarEquipoPantalla.getListaPasosArmado().length - 1);
        navegarAIndice(siguiente);
    }

    @Override
    public void retrocederPaso() {
        int anterior = Math.max(indiceActual - 1, 0);
        navegarAIndice(anterior);
    }

    private boolean validarEnsamblaje(dto.EnsamblajeDTO ensamblaje) {
        return ensamblaje != null && !ensamblaje.obtenerTodosComponentes().isEmpty();
    }

    private void limpiarConfiguracionActual() {
        FachadaControl fachada = FachadaControl.getInstance();
        fachada.limpiarEnsamblaje();
        armarEquipoPantalla.updateResumen(fachada.getEnsamblajeActual());

        controlpresentacion.ControlPresentacion.getInstance().seleccionarCategoria(null);
        controlpresentacion.ControlPresentacion.getInstance().seleccionarMarca(null);

        armarEquipoPantalla.getCategoriasPanel().limpiarSeleccion();
        armarEquipoPantalla.getMarcasPanel().limpiarSeleccion();

        this.seleccionCategoria = null;
        this.seleccionMarca = null;
        this.categoriaConfirmada = false;
        this.marcaConfirmada = false;

        carritoPantalla.actualizarCarrito();
    }

    private void mostrarMensaje(String mensaje, String titulo, int tipoMensaje) {
        JOptionPane.showMessageDialog(framePrincipal, mensaje, titulo, tipoMensaje);
    }

    private void manejarExcepcion(String contexto, Exception ex) {
        System.err.println(contexto + ": " + ex.getMessage());
        ex.printStackTrace();
        mostrarMensaje(contexto + ": " + ex.getMessage(), TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public JPanel getPanelActual() {
        return framePrincipal.getPanelContenido();
    }
}
