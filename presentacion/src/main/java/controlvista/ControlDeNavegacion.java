package controlvista;

import compartido.FramePrincipal;
import armadoPC.ArmarPcPanel;
import carrito.CarritoPanel;
import compartido.BarraNavegacion;
import menuprincipal.MenuPrincipalPanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlDeNavegacion {
    // Constantes para índices de navegación
    private static final int INDICE_INICIO_MENUS = 2;
    private static final int DIRECCION_ADELANTE = 1;
    private static final int DIRECCION_ATRAS = -1;

    // Índices de componentes
    private static final int INDICE_PROCESADOR = 2;
    private static final int INDICE_TARJETA_MADRE = 3;
    private static final int INDICE_MEMORIA_RAM = 4;
    private static final int INDICE_ALMACENAMIENTO = 5;
    private static final int INDICE_UNIDAD_SSD = 6;
    private static final int INDICE_TARJETA_VIDEO = 7;
    private static final int INDICE_FUENTE_PODER = 8;
    private static final int INDICE_DISIPADOR = 9;
    private static final int INDICE_VENTILADOR = 10;
    private static final int INDICE_MONITOR = 11;
    private static final int INDICE_KIT_TECLADO_RATON = 12;
    private static final int INDICE_RED = 13;

    private final FramePrincipal framePrincipal;
    private final MenuPrincipalPanel menuPrincipalPanel;
    private final ArmarPcPanel armarEquipoPantalla;
    private final CarritoPanel carritoPantalla;

    public ControlDeNavegacion(FramePrincipal framePrincipal) {
        this.framePrincipal = framePrincipal;
        this.menuPrincipalPanel = new MenuPrincipalPanel();
        this.armarEquipoPantalla = new ArmarPcPanel();
        this.carritoPantalla = new CarritoPanel();

        inicializarVista();
        configurarBarraNavegacion();
        configurarNavegacionArmarPC();
        configurarBotonesComponentes();
    }

    /**
     * Inicializa la vista mostrando el panel principal.
     */
    private void inicializarVista() {
        framePrincipal.setPanelContenido(menuPrincipalPanel);
        framePrincipal.setVisible(true);
    }

    /**
     * Configura los listeners de la barra de navegación principal.
     */
    private void configurarBarraNavegacion() {
        BarraNavegacion barra = framePrincipal.getBarraNavegacion();

        // Botón de inicio/home
        barra.getBoton().addActionListener(e -> mostrarNuevaPantalla(menuPrincipalPanel));

        // Label de "Armar PC"
        barra.getArmarPcLbl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarNuevaPantalla(armarEquipoPantalla);
            }
        });

        // Label de "Carrito"
        barra.getCarritoLbl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarNuevaPantalla(carritoPantalla);
            }
        });
    }

    /**
     * Configura los botones de navegación (continuar/retroceder) del panel de armado.
     */
    private void configurarNavegacionArmarPC() {
        armarEquipoPantalla.getContinuarBtn()
            .addActionListener(e -> navegarDireccion(DIRECCION_ADELANTE));

        armarEquipoPantalla.getRetrocederBtn()
            .addActionListener(e -> navegarDireccion(DIRECCION_ATRAS));
    }

    /**
     * Configura los listeners para los botones de selección directa de componentes.
     */
    private void configurarBotonesComponentes() {
        var menuComponentes = armarEquipoPantalla.getMenuComponentesPanel();

        menuComponentes.getProcesadorBtn()
            .addActionListener(e -> navegarComponente(INDICE_PROCESADOR));

        menuComponentes.getTarjetaMadreBtn()
            .addActionListener(e -> navegarComponente(INDICE_TARJETA_MADRE));

        menuComponentes.getMemoriaRAMBtn()
            .addActionListener(e -> navegarComponente(INDICE_MEMORIA_RAM));

        menuComponentes.getAlmacenamientoBtn()
            .addActionListener(e -> navegarComponente(INDICE_ALMACENAMIENTO));

        menuComponentes.getUnidadSSDBtn()
            .addActionListener(e -> navegarComponente(INDICE_UNIDAD_SSD));

        menuComponentes.getTarjetaDeVideoBtn()
            .addActionListener(e -> navegarComponente(INDICE_TARJETA_VIDEO));

        menuComponentes.getFuenteDePoderBtn()
            .addActionListener(e -> navegarComponente(INDICE_FUENTE_PODER));

        menuComponentes.getDisipadorBtn()
            .addActionListener(e -> navegarComponente(INDICE_DISIPADOR));

        menuComponentes.getVentiladorBtn()
            .addActionListener(e -> navegarComponente(INDICE_VENTILADOR));

        menuComponentes.getMonitorBtn()
            .addActionListener(e -> navegarComponente(INDICE_MONITOR));

        menuComponentes.getKitTecladoRatonBtn()
            .addActionListener(e -> navegarComponente(INDICE_KIT_TECLADO_RATON));

        menuComponentes.getRedBtn()
            .addActionListener(e -> navegarComponente(INDICE_RED));
    }

    /**
     * Muestra una nueva pantalla en el frame principal.
     * @param nuevoPanel El panel a mostrar
     */
    private void mostrarNuevaPantalla(JPanel nuevoPanel) {
        framePrincipal.setPanelContenido(nuevoPanel);
    }

    /**
     * Navega hacia adelante o atrás en el flujo de armado de PC.
     * @param direction 1 para adelante, -1 para atrás
     */
    private void navegarDireccion(int direction) {
        int indiceActual = armarEquipoPantalla.getIndiceActual();
        int nuevoIndice = indiceActual + direction;
        navegarAIndice(nuevoIndice);
    }

    /**
     * Navega directamente a un componente específico por su índice.
     * @param indiceComponente El índice del componente al que navegar
     */
    private void navegarComponente(int indiceComponente) {
        navegarAIndice(indiceComponente);
    }

    /**
     * Método centralizado para navegar a cualquier índice del flujo de armado.
     * Gestiona la validación, actualización de menús y cambio de pantalla.
     * @param nuevoIndice El índice al que se quiere navegar
     */
    private void navegarAIndice(int nuevoIndice) {
        // Validar que el índice esté dentro del rango válido
        if (!esIndiceValido(nuevoIndice)) {
            return;
        }

        // Gestionar visibilidad de menús de navegación
        actualizarVisibilidadMenus(nuevoIndice);

        // Actualizar la vista del panel de armado
        actualizarVistaPanelArmado(nuevoIndice);
    }

    /**
     * Valida si un índice está dentro del rango válido de pasos de armado.
     * @param indice El índice a validar
     * @return true si el índice es válido, false en caso contrario
     */
    private boolean esIndiceValido(int indice) {
        return indice >= 0 && indice < armarEquipoPantalla.getListaPasosArmado().length;
    }

    /**
     * Actualiza la visibilidad de los menús de navegación según el índice.
     * Los menús se muestran a partir del índice 2 (después de las pantallas iniciales).
     * @param indice El índice actual de navegación
     */
    private void actualizarVisibilidadMenus(int indice) {
        if (indice >= INDICE_INICIO_MENUS) {
            armarEquipoPantalla.añadirMenusNavegacion();
        } else {
            armarEquipoPantalla.eliminarMenusNavegacion();
        }
    }

    /**
     * Actualiza la vista del panel de armado mostrando el paso correspondiente.
     * @param indice El índice del paso a mostrar
     */
    private void actualizarVistaPanelArmado(int indice) {
        String[] pasos = armarEquipoPantalla.getListaPasosArmado();

        // Cambiar a la tarjeta correspondiente
        armarEquipoPantalla.getCardLayout().show(
            armarEquipoPantalla.getCardsPanel(),
            pasos[indice]
        );

        // Actualizar el índice actual
        armarEquipoPantalla.setIndiceActual(indice);

        // Actualizar el título del paso
        armarEquipoPantalla.getSubTItuloLabel().setTitulo(pasos[indice]);

        // Actualizar estado de botones (habilitar/deshabilitar)
        armarEquipoPantalla.updateButtonState();
    }
}
