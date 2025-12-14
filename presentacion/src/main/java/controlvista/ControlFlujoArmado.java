package controlvista;

import armadoPC.ArmarPcPanel;
import controlpresentacion.IControlPresentacionArmado;
import dto.ComponenteDTO;
import dto.EnsamblajeDTO;

import javax.swing.*;
import java.util.List;
import java.util.Set;

/**
 * Control especializado para gestionar el flujo de armado de PC (wizard de pasos).
 * <p>
 * PATRÓN: Mediador Jerárquico
 * - Se encarga exclusivamente de la lógica de navegación entre pasos del armado.
 * - Gestiona validaciones de categoría, marca y componentes obligatorios.
 * - Delega la lógica de negocio a ControlPresentacionArmado.
 */
public class ControlFlujoArmado {

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

    private final ArmarPcPanel armarPcPanel;
    private final IControlPresentacionArmado controlPresentacionArmado;
    private final JFrame frameParent;

    private int indiceActual = 0;
    private String seleccionCategoria = null;
    private String seleccionMarca = null;
    private boolean categoriaConfirmada = false;
    private boolean marcaConfirmada = false;

    public ControlFlujoArmado(ArmarPcPanel armarPcPanel, IControlPresentacionArmado controlPresentacionArmado, JFrame frameParent) {
        this.armarPcPanel = armarPcPanel;
        this.controlPresentacionArmado = controlPresentacionArmado;
        this.frameParent = frameParent;

        configurarEventos();
    }

    private void configurarEventos() {
        configurarBotonesComponentes();
        configurarNavegacionInterna();
        configurarCallbacksNegocio();
    }

    private void configurarBotonesComponentes() {
        var menuComponentes = armarPcPanel.getMenuComponentesPanel();
        if (menuComponentes == null) return;

        menuComponentes.getCategoriasBtn().addActionListener(e -> navegarAIndice(INDICE_CATEGORIA));
        menuComponentes.getMarcaProcesadorBtn().addActionListener(e -> navegarAIndice(INDICE_MARCA_PROCESADOR));
        menuComponentes.getProcesadorBtn().addActionListener(e -> navegarAIndice(INDICE_PROCESADOR));
        menuComponentes.getTarjetaMadreBtn().addActionListener(e -> navegarAIndice(INDICE_TARJETA_MADRE));
        menuComponentes.getMemoriaRAMBtn().addActionListener(e -> navegarAIndice(INDICE_MEMORIA_RAM));
        menuComponentes.getTarjetaDeVideoBtn().addActionListener(e -> navegarAIndice(INDICE_TARJETA_VIDEO));
        menuComponentes.getAlmacenamientoBtn().addActionListener(e -> navegarAIndice(INDICE_ALMACENAMIENTO));
        menuComponentes.getUnidadSSDBtn().addActionListener(e -> navegarAIndice(INDICE_ALMACENAMIENTO));
        menuComponentes.getFuenteDePoderBtn().addActionListener(e -> navegarAIndice(INDICE_FUENTE_PODER));
        menuComponentes.getGabineteBtn().addActionListener(e -> navegarAIndice(INDICE_GABINETE));
        menuComponentes.getDisipadorBtn().addActionListener(e -> navegarAIndice(INDICE_DISIPADOR));
        menuComponentes.getVentiladorBtn().addActionListener(e -> navegarAIndice(INDICE_VENTILADOR));
        menuComponentes.getMonitorBtn().addActionListener(e -> navegarAIndice(INDICE_MONITOR));
        menuComponentes.getKitTecladoRatonBtn().addActionListener(e -> navegarAIndice(INDICE_KIT_TECLADO_RATON));
        menuComponentes.getRedBtn().addActionListener(e -> navegarAIndice(INDICE_RED));
        menuComponentes.getResumenConfiguracionBtn().addActionListener(e -> navegarAIndice(INDICE_RESUMEN));
    }

    private void configurarNavegacionInterna() {
        var siguiente = armarPcPanel.getContinuarBtn();
        var retroceder = armarPcPanel.getRetrocederBtn();
        if (siguiente != null) {
            siguiente.addActionListener(e -> avanzarPaso());
        }
        if (retroceder != null) {
            retroceder.addActionListener(e -> retrocederPaso());
        }
    }

    private void configurarCallbacksNegocio() {
        armarPcPanel.setOnProductoSelected(this::procesarSeleccionProducto);
        armarPcPanel.setOnCategoriaSeleccionada(this::procesarSeleccionCategoria);
        armarPcPanel.setOnMarcaSeleccionada(this::procesarSeleccionMarca);
    }

    /**
     * Inicia el flujo de armado desde el principio.
     */
    public void iniciar() {
        indiceActual = 0;
        seleccionCategoria = null;
        seleccionMarca = null;
        categoriaConfirmada = false;
        marcaConfirmada = false;

        armarPcPanel.mostrarMenusLaterales();
        armarPcPanel.habilitarSoloCategorias();

        String[] pasos = armarPcPanel.getListaPasosArmado();
        if (pasos != null && pasos.length > 0) {
            armarPcPanel.mostrarPaso(0);
            var cont = armarPcPanel.getContinuarBtn();
            if (cont != null) cont.setEnabled(true);
        }
    }

    public void navegarAIndice(int nuevoIndice) {
        if (!esIndiceValido(nuevoIndice)) return;

        if (nuevoIndice > INDICE_CATEGORIA && !categoriaConfirmada) {
            mostrarMensaje("Debe seleccionar una categoría antes de continuar.", "Categoría requerida", JOptionPane.WARNING_MESSAGE);
            navegarAIndice(INDICE_CATEGORIA);
            return;
        }

        if (nuevoIndice > INDICE_MARCA_PROCESADOR && !marcaConfirmada) {
            mostrarMensaje("Debe seleccionar una marca de procesador antes de continuar.", "Marca requerida", JOptionPane.WARNING_MESSAGE);
            navegarAIndice(INDICE_MARCA_PROCESADOR);
            return;
        }

        String[] pasos = armarPcPanel.getListaPasosArmado();
        if (nuevoIndice < indiceActual && nuevoIndice >= INDICE_PROCESADOR) {
            String categoriaActual = pasos[nuevoIndice];
            controlPresentacionArmado.removerComponentesPosteriores(categoriaActual);
            armarPcPanel.updateResumen(controlPresentacionArmado.getEnsamblajeActual());
        }

        indiceActual = nuevoIndice;
        armarPcPanel.mostrarPaso(nuevoIndice);

        actualizarEstadoBotonContinuar(nuevoIndice);
        cargarCatalogoSiEsNecesario(nuevoIndice);
        actualizarPanelEste(nuevoIndice);

        SwingUtilities.invokeLater(armarPcPanel::actualizarEstadoContinuarDesdeUI);
    }

    /**
     * Actualiza el contenido del panel este (derecha) según el paso actual.
     * Panel Este en paso RESUMEN: muestra opciones (Guardar/Agregar al carrito)
     * Panel Este en otros pasos: muestra resumen de la configuración
     * Panel Oeste (izquierda): SIEMPRE visible con el menú de componentes
     */
    private void actualizarPanelEste(int nuevoIndice) {
        if (nuevoIndice == INDICE_RESUMEN) {
            EnsamblajeDTO ensamblaje = controlPresentacionArmado.getEnsamblajeActual();
            armarPcPanel.updateResumen(ensamblaje);
            armarPcPanel.mostrarMenuOpcionesEnLateral();
        } else {
            armarPcPanel.mostrarResumenEnLateral();
        }
    }

    private void actualizarEstadoBotonContinuar(int nuevoIndice) {
        var contBtn = armarPcPanel.getContinuarBtn();
        boolean contEnabled;

        String cfgCategoria = controlPresentacionArmado.getCategoriaActual();
        String cfgMarca = controlPresentacionArmado.getMarcaActual();
        String[] pasos = armarPcPanel.getListaPasosArmado();

        if (nuevoIndice == 0) {
            armarPcPanel.habilitarSoloCategorias();
            contEnabled = armarPcPanel.getCategoriasPanel().getSeleccionActual() != null || cfgCategoria != null || seleccionCategoria != null || categoriaConfirmada;
        } else if (nuevoIndice == 1) {
            armarPcPanel.habilitarCategoriasYMarca();
            contEnabled = armarPcPanel.getMarcasPanel().getSeleccionActual() != null || cfgMarca != null || seleccionMarca != null || marcaConfirmada;
        } else {
            String pasoCat = pasos[nuevoIndice];
            boolean selCatPresent = armarPcPanel.getCategoriasPanel().getSeleccionActual() != null || cfgCategoria != null || seleccionCategoria != null || categoriaConfirmada;
            boolean selMarcaPresent = armarPcPanel.getMarcasPanel().getSeleccionActual() != null || cfgMarca != null || seleccionMarca != null || marcaConfirmada;

            if (selCatPresent && selMarcaPresent) {
                armarPcPanel.habilitarTodo();
            } else {
                armarPcPanel.habilitarCategoriasYMarca();
            }

            if (PASOS_OBLIGATORIOS.contains(nuevoIndice)) {
                contEnabled = (controlPresentacionArmado.getComponenteSeleccionado(pasoCat) != null) || armarPcPanel.haySeleccionEnCatalogo(pasoCat);
            } else {
                contEnabled = true;
            }
        }

        if (contBtn != null) contBtn.setEnabled(contEnabled);
    }

    private void cargarCatalogoSiEsNecesario(int nuevoIndice) {
        String[] pasos = armarPcPanel.getListaPasosArmado();

        if (nuevoIndice >= 2 && nuevoIndice < pasos.length - 1) {
            boolean ok = controlPresentacionArmado.tieneMinimoPorCategoria(pasos[nuevoIndice], MINIMO_POR_CATEGORIA);

            if (!ok) {
                JOptionPane.showMessageDialog(frameParent,
                        "No hay suficientes productos disponibles en la categoría '" + pasos[nuevoIndice] + "' para continuar.",
                        "Catálogo incompleto",
                        JOptionPane.WARNING_MESSAGE);
                int anterior = Math.max(indiceActual - 1, 0);
                armarPcPanel.mostrarPaso(anterior);
                return;
            }

            if (nuevoIndice == INDICE_PROCESADOR && marcaConfirmada && seleccionMarca != null) {
                if (!controlPresentacionArmado.tieneMarcaEnCategoria("Procesador", seleccionMarca)) {
                    mostrarMensaje("No hay procesadores de la marca '" + seleccionMarca + "' disponibles en inventario.", "Marca no disponible", JOptionPane.WARNING_MESSAGE);
                    navegarAIndice(INDICE_MARCA_PROCESADOR);
                    return;
                }
                armarPcPanel.cargarCatalogoConMarca(pasos[nuevoIndice], seleccionMarca);
            } else {
                armarPcPanel.cargarCatalogo(pasos[nuevoIndice]);
            }
        }
    }


    private void procesarSeleccionProducto(String productoId) {
        try {
            ComponenteDTO componente = convertirAComponenteDTO(productoId);
            if (componente == null) {
                mostrarMensaje("Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            controlPresentacionArmado.seleccionarProducto(componente);

            List<String> errores = controlPresentacionArmado.avanzarConComponenteSeleccionado();

            // Los productos incompatibles ya se filtran antes de mostrarse,
            // pero si por alguna razón hay errores, los manejamos
            if (!errores.isEmpty()) {
                mostrarMensaje("Error de compatibilidad: " + String.join(", ", errores),
                              "Componente incompatible", JOptionPane.WARNING_MESSAGE);
                return;
            }

            armarPcPanel.updateResumen(controlPresentacionArmado.getEnsamblajeActual());
            var btn = armarPcPanel.getContinuarBtn();
            if (btn != null) btn.setEnabled(true);
        } catch (Exception ex) {
            System.err.println("Error procesando selección: " + ex.getMessage());
            mostrarMensaje("Error procesando selección: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarSeleccionCategoria(String categoria) {
        this.seleccionCategoria = categoria;
        this.categoriaConfirmada = false;

        if (categoria == null) {
            armarPcPanel.habilitarSoloCategorias();
            controlPresentacionArmado.seleccionarCategoria(null);
            SwingUtilities.invokeLater(armarPcPanel::actualizarEstadoContinuarDesdeUI);
            return;
        }

        if (!controlPresentacionArmado.tieneMinimoPorCategoria("Procesador", MINIMO_POR_CATEGORIA)) {
            mostrarMensaje("No hay suficientes procesadores disponibles para la categoría seleccionada.", "Inventario Insuficiente", JOptionPane.WARNING_MESSAGE);
            armarPcPanel.getCategoriasPanel().limpiarSeleccion();
            armarPcPanel.habilitarSoloCategorias();
            return;
        }

        String[] componentesObligatorios = {"Procesador", "Tarjeta Madre", "RAM", "Gabinete", "Tarjeta de video", "Fuente de poder", "Disipador"};
        for (String componente : componentesObligatorios) {
            if (!controlPresentacionArmado.tieneMinimoPorCategoria(componente, MINIMO_POR_CATEGORIA)) {
                mostrarMensaje("No hay suficientes productos de '" + componente + "' disponibles para armar una configuración básica.", "Inventario Insuficiente", JOptionPane.WARNING_MESSAGE);
                armarPcPanel.getCategoriasPanel().limpiarSeleccion();
                armarPcPanel.habilitarSoloCategorias();
                return;
            }
        }

        this.categoriaConfirmada = true;
        armarPcPanel.habilitarCategoriasYMarca();
        controlPresentacionArmado.seleccionarCategoria(categoria);
        SwingUtilities.invokeLater(armarPcPanel::actualizarEstadoContinuarDesdeUI);
    }

    private void procesarSeleccionMarca(String marca) {
        this.seleccionMarca = marca;
        this.marcaConfirmada = false;

        if (marca == null) {
            armarPcPanel.habilitarCategoriasYMarca();
            controlPresentacionArmado.seleccionarMarca(null);
            SwingUtilities.invokeLater(armarPcPanel::actualizarEstadoContinuarDesdeUI);
            return;
        }

        if (!categoriaConfirmada || seleccionCategoria == null) {
            mostrarMensaje("Debe seleccionar una categoría antes de elegir marca de procesador.", "Categoría no seleccionada", JOptionPane.WARNING_MESSAGE);
            armarPcPanel.getMarcasPanel().limpiarSeleccion();
            armarPcPanel.habilitarCategoriasYMarca();
            return;
        }

        if (!controlPresentacionArmado.tieneMarcaEnCategoria("Procesador", marca)) {
            mostrarMensaje("No hay procesadores de la marca '" + marca + "' disponibles en inventario.", "Marca no disponible", JOptionPane.WARNING_MESSAGE);
            armarPcPanel.getMarcasPanel().limpiarSeleccion();
            armarPcPanel.habilitarCategoriasYMarca();
            return;
        }

        this.marcaConfirmada = true;
        controlPresentacionArmado.seleccionarMarca(marca);
        armarPcPanel.habilitarTodo();
        SwingUtilities.invokeLater(armarPcPanel::actualizarEstadoContinuarDesdeUI);
    }

    public void avanzarPaso() {
        if (indiceActual == INDICE_CATEGORIA) {
            if (!categoriaConfirmada) {
                mostrarMensaje("Debe seleccionar una categoría antes de continuar.", "Categoría requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        if (indiceActual == INDICE_MARCA_PROCESADOR) {
            if (!marcaConfirmada) {
                mostrarMensaje("Debe seleccionar una marca de procesador antes de continuar.", "Marca requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        int siguiente = Math.min(indiceActual + 1, armarPcPanel.getListaPasosArmado().length - 1);
        navegarAIndice(siguiente);
    }

    public void retrocederPaso() {
        int anterior = Math.max(indiceActual - 1, 0);
        navegarAIndice(anterior);
    }

    public void limpiarEstado() {
        controlPresentacionArmado.limpiarEnsamblaje();
        armarPcPanel.updateResumen(controlPresentacionArmado.getEnsamblajeActual());

        controlPresentacionArmado.seleccionarCategoria(null);
        controlPresentacionArmado.seleccionarMarca(null);

        armarPcPanel.getCategoriasPanel().limpiarSeleccion();
        armarPcPanel.getMarcasPanel().limpiarSeleccion();

        this.seleccionCategoria = null;
        this.seleccionMarca = null;
        this.categoriaConfirmada = false;
        this.marcaConfirmada = false;
    }

    private ComponenteDTO convertirAComponenteDTO(String productoId) {
        return controlPresentacionArmado.convertirProductoADTO(productoId);
    }

    private boolean esIndiceValido(int indice) {
        String[] pasos = armarPcPanel.getListaPasosArmado();
        return pasos != null && indice >= 0 && indice < pasos.length;
    }

    private void mostrarMensaje(String mensaje, String titulo, int tipoMensaje) {
        JOptionPane.showMessageDialog(frameParent, mensaje, titulo, tipoMensaje);
    }
}

