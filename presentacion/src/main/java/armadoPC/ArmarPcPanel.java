package armadoPC;

import compartido.estilos.Boton;
import compartido.estilos.Estilos;
import compartido.estilos.TituloLabel;
import compartido.PanelBase;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.function.Consumer;

public class ArmarPcPanel extends PanelBase {

    private static final String[] PASOS = {
        "Tipo de PC", "Marca Procesador", "Procesador", "Tarjeta Madre",
        "RAM", "Tarjeta de video", "Almacenamiento", "Fuente de poder",
        "Gabinete", "Disipador", "Ventilador", "Monitor",
        "Kit de teclado/raton", "Redes e internet", "Resumen"
    };

    private static final int PASO_RESUMEN = 14;
    private CategoriasPanel categoriasPanel;
    private MarcaProcesadorPanel marcasPanel;
    private EvaluarConfiguracionPanel evaluarConfiguracionPanel;
    private MenuComponentesPanel menuComponentesPanel;
    private SideMenuResumenPanel sideMenuResumenPanel;

    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private TituloLabel subTItuloLabel;
    private Boton continuarBtn;
    private Boton retrocederBtn;

    private Map<String, CatalagoPanel> catalogos;
    private Consumer<String> onProductoSelected;

    public ArmarPcPanel() {
        super();
        inicializarComponentes();
        ensamblarUI();
    }

    private void inicializarComponentes() {
        catalogos = new java.util.HashMap<>();

        categoriasPanel = new CategoriasPanel();
        marcasPanel = new MarcaProcesadorPanel();
        evaluarConfiguracionPanel = new EvaluarConfiguracionPanel();
        menuComponentesPanel = new MenuComponentesPanel();
        sideMenuResumenPanel = new SideMenuResumenPanel();

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

    public Boton getContinuarBtn() { return continuarBtn; }
    public Boton getRetrocederBtn() { return retrocederBtn; }
    public CardLayout getCardLayout() { return cardLayout; }
    public JPanel getCardsPanel() { return cardsPanel; }
    public String[] getListaPasosArmado() { return PASOS; }
    public TituloLabel getSubTItuloLabel() { return subTItuloLabel; }
    public MenuComponentesPanel getMenuComponentesPanel() { return menuComponentesPanel; }
    public CategoriasPanel getCategoriasPanel() { return categoriasPanel; }
    public MarcaProcesadorPanel getMarcasPanel() { return marcasPanel; }
}
