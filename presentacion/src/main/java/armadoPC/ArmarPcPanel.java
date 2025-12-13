package armadoPC;

import compartido.estilos.Boton;
import compartido.estilos.Estilos;
import compartido.estilos.TituloLabel;
import compartido.PanelBase;
import dto.EnsamblajeDTO;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ArmarPcPanel extends PanelBase {
    private static final String[] PASOS = {
            "Tipo de PC", "Marca Procesador", "Procesador", "Tarjeta Madre",
            "RAM", "Tarjeta de video", "Almacenamiento", "Fuente de poder",
            "Gabinete", "Disipador", "Ventilador", "Monitor",
            "Kit de teclado/raton", "Redes e internet", "Evaluar Configuración"
    };

    private static final int PASO_RESUMEN = 14;
    private CategoriasPanel categoriasPanel;
    private MarcaProcesadorPanel marcasPanel;
    private EvaluarConfiguracionPanel evaluarConfiguracionPanel;
    private MenuComponentesPanel menuComponentesPanel;
    private SideMenuResumenPanel sideMenuResumenPanel;
    private MenuOpcionesPanel menuOpcionesPanel;

    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private TituloLabel tituloLbl;
    private Boton continuarBtn;
    private Boton retrocederBtn;

    private Map<String, CatalagoPanel> catalogos;
    private Consumer<String> onProductoSelected;

    private int pasoActual = 0;

    public ArmarPcPanel() {
        super();
        inicializarComponentes();
        navegacionSup();
    }

    public void setOnCategoriaSeleccionada(Consumer<String> callback) {
        if (categoriasPanel != null) categoriasPanel.setOnCategoriaSeleccionada(callback);
    }

    public void setOnMarcaSeleccionada(Consumer<String> callback) {
        if (marcasPanel != null) marcasPanel.setOnMarcaSelected(callback);
    }

    public void bloquearTodo() {
        establecerEstadoBotones(false);
    }

    public void habilitarTodo() {
        if (menuComponentesPanel == null) return;
        establecerEstadoBotones(true);
    }

    private void establecerEstadoBotones(boolean enabled) {
        if (menuComponentesPanel != null) {
            menuComponentesPanel.getCategoriasBtn().setEnabled(enabled);
            menuComponentesPanel.getMarcaProcesadorBtn().setEnabled(enabled);
            menuComponentesPanel.getProcesadorBtn().setEnabled(enabled);
            menuComponentesPanel.getTarjetaMadreBtn().setEnabled(enabled);
            menuComponentesPanel.getMemoriaRAMBtn().setEnabled(enabled);
            menuComponentesPanel.getAlmacenamientoBtn().setEnabled(enabled);
            menuComponentesPanel.getUnidadSSDBtn().setEnabled(enabled);
            menuComponentesPanel.getTarjetaDeVideoBtn().setEnabled(enabled);
            menuComponentesPanel.getFuenteDePoderBtn().setEnabled(enabled);
            menuComponentesPanel.getGabineteBtn().setEnabled(enabled);
            menuComponentesPanel.getDisipadorBtn().setEnabled(enabled);
            menuComponentesPanel.getVentiladorBtn().setEnabled(enabled);
            menuComponentesPanel.getMonitorBtn().setEnabled(enabled);
            menuComponentesPanel.getKitTecladoRatonBtn().setEnabled(enabled);
            menuComponentesPanel.getRedBtn().setEnabled(enabled);
            menuComponentesPanel.getResumenConfiguracionBtn().setEnabled(enabled);
        }
        if (continuarBtn != null) continuarBtn.setEnabled(enabled);
    }

    public void habilitarSoloCategorias() {
        if (menuComponentesPanel == null) return;
        bloquearTodo();
        menuComponentesPanel.getCategoriasBtn().setEnabled(true);
    }

    public void habilitarCategoriasYMarca() {
        if (menuComponentesPanel == null) return;
        bloquearTodo();
        menuComponentesPanel.getCategoriasBtn().setEnabled(true);
        menuComponentesPanel.getMarcaProcesadorBtn().setEnabled(true);
    }

    private void inicializarComponentes() {
        catalogos = new HashMap<>();
        categoriasPanel = new CategoriasPanel();
        marcasPanel = new MarcaProcesadorPanel();
        evaluarConfiguracionPanel = new EvaluarConfiguracionPanel();
        menuComponentesPanel = new MenuComponentesPanel();
        sideMenuResumenPanel = new SideMenuResumenPanel();
        menuOpcionesPanel = new MenuOpcionesPanel();

        continuarBtn = new Boton("→", 55, 40, 22, 20, Color.white,
                Estilos.COLOR_BACKGROUND, Estilos.COLOR_ATRAS_BOTON_HOOVER);

        retrocederBtn = new Boton("←", 55, 40, 22, 20, Color.white,
                Estilos.COLOR_BACKGROUND, Estilos.COLOR_ATRAS_BOTON_HOOVER);

        inicializarCards();
        inicializarPanalesLaterales();
    }

    private void inicializarPanalesLaterales() {
        panelOeste.add(menuComponentesPanel);
        panelEste.add(sideMenuResumenPanel);
        menuOpcionesPanel.setVisible(false);
    }

    private void inicializarCards() {
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setOpaque(false);

        cardsPanel.add(categoriasPanel, PASOS[0]);
        cardsPanel.add(marcasPanel, PASOS[1]);

        for (int i = 2; i < PASOS.length - 1; i++) {
            CatalagoPanel catalogo = new CatalagoPanel();
            catalogos.put(PASOS[i], catalogo);
            cardsPanel.add(catalogo, PASOS[i]);
        }

        cardsPanel.add(evaluarConfiguracionPanel, PASOS[PASO_RESUMEN]);

        tituloLbl = new TituloLabel(PASOS[0]);
        tituloLbl.setForeground(Color.white);
    }

    private void navegacionSup() {
        panelNorte.add(retrocederBtn);
        panelNorte.add(tituloLbl);
        panelNorte.add(continuarBtn);

        panelCentro.add(cardsPanel);
    }

    public void cargarCatalogo(String categoria) {
        CatalagoPanel catalogo = catalogos.get(categoria);
        if (catalogo == null) return;

        catalogo.setOnProductoSelected(id -> {
            if (onProductoSelected != null) onProductoSelected.accept(id);
        });

        catalogo.cargarLista(categoria);
    }

    public void cargarCatalogoConMarca(String categoria, String marca) {
        CatalagoPanel catalogo = catalogos.get(categoria);
        if (catalogo == null) return;

        catalogo.setOnProductoSelected(id -> {
            if (onProductoSelected != null) onProductoSelected.accept(id);
        });

        catalogo.cargarListaConMarca(categoria, marca);
    }

    public void setOnProductoSelected(Consumer<String> callback) {
        this.onProductoSelected = callback;
    }

    public void updateResumen(EnsamblajeDTO ensamblaje) {
        sideMenuResumenPanel.updateFrom(ensamblaje);
        menuOpcionesPanel.updateFrom(ensamblaje);
        if (evaluarConfiguracionPanel != null) {
            evaluarConfiguracionPanel.updateFrom(ensamblaje);
        }
    }

    public void mostrarPaso(int index) {
        if (index < 0) index = 0;
        if (index >= PASOS.length) index = PASOS.length - 1;
        pasoActual = index;
        cardLayout.show(cardsPanel, PASOS[pasoActual]);
        tituloLbl.setText(PASOS[pasoActual]);

    }

    public boolean haySeleccionEnCatalogo(String categoria) {
        if (categoria == null) return false;
        CatalagoPanel catalogo = catalogos.get(categoria);
        return catalogo != null && catalogo.getProductoSeleccionado() != null;
    }

    public void actualizarEstadoContinuarDesdeUI() {
        SwingUtilities.invokeLater(this::validarEstadoContinuar);
    }

    private void validarEstadoContinuar() {
        if (continuarBtn == null) return;
        continuarBtn.setEnabled(validarSeleccionEnPasoActual());
    }

    private boolean validarSeleccionEnPasoActual() {
        return switch (getPasoActual()) {
            case 0 -> categoriasPanel.getSeleccionActual() != null;
            case 1 -> marcasPanel.getSeleccionActual() != null;
            default -> false;
        };
    }

    /**
     * Muestra los menús laterales por defecto durante el proceso de armado.
     * Panel Oeste: Menú de componentes (siempre visible)
     * Panel Este: Resumen de la configuración
     */
    public void mostrarMenusLaterales() {
        menuComponentesPanel.setVisible(true);
        sideMenuResumenPanel.setVisible(true);
        menuOpcionesPanel.setVisible(false);
    }

    /**
     * Muestra las opciones finales (Guardar/Agregar al carrito) en el panel este.
     * Panel Oeste: Menú de componentes (permanece visible)
     * Panel Este: Opciones de acción
     */
    public void mostrarMenuOpcionesEnLateral() {
        sideMenuResumenPanel.setVisible(false);

        if (menuOpcionesPanel.getParent() != panelEste) {
            panelEste.add(menuOpcionesPanel);
        }
        menuOpcionesPanel.setVisible(true);

        panelEste.revalidate();
        panelEste.repaint();
    }

    /**
     * Muestra solo el resumen en el panel este.
     * Panel Oeste: Menú de componentes (permanece visible)
     * Panel Este: Resumen de la configuración
     */
    public void mostrarResumenEnLateral() {
        sideMenuResumenPanel.setVisible(true);
        menuOpcionesPanel.setVisible(false);
    }

    public int getPasoActual() {
        return pasoActual;
    }

    public Boton getContinuarBtn() {
        return continuarBtn;
    }

    public Boton getRetrocederBtn() {
        return retrocederBtn;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getCardsPanel() {
        return cardsPanel;
    }

    public String[] getListaPasosArmado() {
        return PASOS;
    }

    public TituloLabel getTituloLbl() {
        return tituloLbl;
    }

    public MenuComponentesPanel getMenuComponentesPanel() {
        return menuComponentesPanel;
    }

    public CategoriasPanel getCategoriasPanel() {
        return categoriasPanel;
    }

    public MarcaProcesadorPanel getMarcasPanel() {
        return marcasPanel;
    }

    public MenuOpcionesPanel getMenuOpcionesPanel() {
        return menuOpcionesPanel;
    }
}
