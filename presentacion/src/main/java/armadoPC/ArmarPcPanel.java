package armadoPC;

import compartido.estilos.Boton;
import compartido.estilos.Estilos;
import compartido.estilos.TituloLabel;
import compartido.PanelBase;

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
    private TituloLabel subTItuloLabel;
    private Boton continuarBtn;
    private Boton retrocederBtn;

    private Map<String, CatalagoPanel> catalogos;
    private Consumer<String> onProductoSelected;

    private int pasoActual = 0;

    public ArmarPcPanel() {
        super();
        inicializarComponentes();
        ensamblarUI();
    }

    public void setOnCategoriaSeleccionada(java.util.function.Consumer<String> callback) {
        if (categoriasPanel != null) categoriasPanel.setOnCategoriaSeleccionada(callback);
    }

    public void setOnMarcaSeleccionada(java.util.function.Consumer<String> callback) {
        if (marcasPanel != null) marcasPanel.setOnMarcaSelected(callback);
    }

    public void habilitarNavegacionBasica(boolean enabled) {
        if (continuarBtn != null) continuarBtn.setEnabled(enabled);
        if (menuComponentesPanel != null) {
            // Habilitar/Deshabilitar todos los botones del menú lateral
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
    }

    public void bloquearTodo() {
        if (menuComponentesPanel != null) {
            menuComponentesPanel.getCategoriasBtn().setEnabled(false);
            menuComponentesPanel.getMarcaProcesadorBtn().setEnabled(false);
            menuComponentesPanel.getProcesadorBtn().setEnabled(false);
            menuComponentesPanel.getTarjetaMadreBtn().setEnabled(false);
            menuComponentesPanel.getMemoriaRAMBtn().setEnabled(false);
            menuComponentesPanel.getAlmacenamientoBtn().setEnabled(false);
            menuComponentesPanel.getUnidadSSDBtn().setEnabled(false);
            menuComponentesPanel.getTarjetaDeVideoBtn().setEnabled(false);
            menuComponentesPanel.getFuenteDePoderBtn().setEnabled(false);
            menuComponentesPanel.getGabineteBtn().setEnabled(false);
            menuComponentesPanel.getDisipadorBtn().setEnabled(false);
            menuComponentesPanel.getVentiladorBtn().setEnabled(false);
            menuComponentesPanel.getMonitorBtn().setEnabled(false);
            menuComponentesPanel.getKitTecladoRatonBtn().setEnabled(false);
            menuComponentesPanel.getRedBtn().setEnabled(false);
            menuComponentesPanel.getResumenConfiguracionBtn().setEnabled(false);
        }
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

    public void habilitarTodo() {
        if (menuComponentesPanel == null) return;
        menuComponentesPanel.getCategoriasBtn().setEnabled(true);
        menuComponentesPanel.getMarcaProcesadorBtn().setEnabled(true);
        menuComponentesPanel.getProcesadorBtn().setEnabled(true);
        menuComponentesPanel.getTarjetaMadreBtn().setEnabled(true);
        menuComponentesPanel.getMemoriaRAMBtn().setEnabled(true);
        menuComponentesPanel.getAlmacenamientoBtn().setEnabled(true);
        menuComponentesPanel.getUnidadSSDBtn().setEnabled(true);
        menuComponentesPanel.getTarjetaDeVideoBtn().setEnabled(true);
        menuComponentesPanel.getFuenteDePoderBtn().setEnabled(true);
        menuComponentesPanel.getGabineteBtn().setEnabled(true);
        menuComponentesPanel.getDisipadorBtn().setEnabled(true);
        menuComponentesPanel.getVentiladorBtn().setEnabled(true);
        menuComponentesPanel.getMonitorBtn().setEnabled(true);
        menuComponentesPanel.getKitTecladoRatonBtn().setEnabled(true);
        menuComponentesPanel.getRedBtn().setEnabled(true);
        menuComponentesPanel.getResumenConfiguracionBtn().setEnabled(true);
        if (continuarBtn != null) continuarBtn.setEnabled(true);
    }

    private void inicializarComponentes() {
        catalogos = new HashMap<>();

        categoriasPanel = new CategoriasPanel();
        marcasPanel = new MarcaProcesadorPanel();
        evaluarConfiguracionPanel = new EvaluarConfiguracionPanel();
        menuComponentesPanel = new MenuComponentesPanel();
        sideMenuResumenPanel = new SideMenuResumenPanel();
        menuOpcionesPanel = new MenuOpcionesPanel();

        inicializarBotones();
        inicializarCards();
    }

    private void inicializarBotones() {
        continuarBtn = new Boton("→", 55, 40, 22, 20, Color.white,
                Estilos.COLOR_BACKGROUND, Estilos.COLOR_ATRAS_BOTON_HOOVER);

        retrocederBtn = new Boton("←", 55, 40, 22, 20, Color.white,
                Estilos.COLOR_BACKGROUND, Estilos.COLOR_ATRAS_BOTON_HOOVER);

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

        subTItuloLabel = new TituloLabel(PASOS[0]);
        subTItuloLabel.setForeground(Color.white);
    }

    private void ensamblarUI() {
        panelNorte.add(retrocederBtn);
        panelNorte.add(subTItuloLabel);
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

    public void setOnProductoSelected(Consumer<String> callback) {
        this.onProductoSelected = callback;
    }

    public void updateResumen(dto.EnsamblajeDTO ensamblaje) {
        sideMenuResumenPanel.updateFrom(ensamblaje);
        menuOpcionesPanel.updateFrom(ensamblaje);
        if (evaluarConfiguracionPanel != null) {
            evaluarConfiguracionPanel.updateFrom(ensamblaje);
        }
    }

    public void mostrarMenusLaterales() {
        panelOeste.removeAll();
        panelEste.removeAll();
        panelOeste.add(menuComponentesPanel);
        panelEste.add(sideMenuResumenPanel);
        panelOeste.revalidate();
        panelOeste.repaint();
        panelEste.revalidate();
        panelEste.repaint();

    }

    public void mostrarPaso(int index) {
        if (index < 0) index = 0;
        if (index >= PASOS.length) index = PASOS.length - 1;
        pasoActual = index;
        cardLayout.show(cardsPanel, PASOS[pasoActual]);
        subTItuloLabel.setText(PASOS[pasoActual]);

        SwingUtilities.invokeLater(() -> {
            int centralHeight = panelCentro.getHeight();
            if (centralHeight <= 0 && cardsPanel != null) {
                Dimension pref = cardsPanel.getPreferredSize();
                centralHeight = pref != null ? pref.height : 0;
            }
            if (centralHeight <= 0) centralHeight = 600;

            Dimension westPref = menuComponentesPanel != null ? menuComponentesPanel.getPreferredSize() : new Dimension(240, centralHeight);
            Dimension eastPref = sideMenuResumenPanel != null ? sideMenuResumenPanel.getPreferredSize() : new Dimension(240, centralHeight);

            panelOeste.setPreferredSize(new Dimension(westPref.width, centralHeight));
            panelEste.setPreferredSize(new Dimension(eastPref.width, centralHeight));

            panelOeste.revalidate();
            panelEste.revalidate();
        });
    }

    public int getPasoActual() { return pasoActual; }

    public Boton getContinuarBtn() { return continuarBtn; }
    public Boton getRetrocederBtn() { return retrocederBtn; }
    public CardLayout getCardLayout() { return cardLayout; }
    public JPanel getCardsPanel() { return cardsPanel; }
    public String[] getListaPasosArmado() { return PASOS; }
    public TituloLabel getSubTItuloLabel() { return subTItuloLabel; }
    public MenuComponentesPanel getMenuComponentesPanel() { return menuComponentesPanel; }
    public CategoriasPanel getCategoriasPanel() { return categoriasPanel; }
    public MarcaProcesadorPanel getMarcasPanel() { return marcasPanel; }
    public MenuOpcionesPanel getMenuOpcionesPanel() { return menuOpcionesPanel; }

    public boolean haySeleccionEnCatalogo(String categoria) {
        if (categoria == null) return false;
        CatalagoPanel catalogo = catalogos.get(categoria);
        if (catalogo == null) return false;
        return catalogo.getProductoSeleccionado() != null;
    }

    public void actualizarEstadoContinuarDesdeUI() {
        SwingUtilities.invokeLater(() -> {
            if (continuarBtn == null) return;
            int paso = getPasoActual();
            if (paso == 0) {
                continuarBtn.setEnabled(categoriasPanel.getSeleccionActual() != null);
            } else if (paso == 1) {
                continuarBtn.setEnabled(marcasPanel.getSeleccionActual() != null);
            }
        });
    }

    public void mostrarMenuOpcionesEnLateral() {
        panelEste.removeAll();
        panelEste.add(menuOpcionesPanel);
        panelEste.revalidate();
        panelEste.repaint();
    }

    public void mostrarResumenEnLateral() {
        panelEste.removeAll();
        panelEste.add(sideMenuResumenPanel);
        panelEste.revalidate();
        panelEste.repaint();
    }
}
