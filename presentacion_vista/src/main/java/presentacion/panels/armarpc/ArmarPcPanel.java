package presentacion.panels.armarpc;

import estilos.Boton;
import estilos.Estilos;
import estilos.FontUtil;
import estilos.TituloLabel;
import presentacion.panels.PanelBase;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ArmarPcPanel extends PanelBase {
    private static String titulo = "Armar PC";
    private CategoriasPanel categoriasPanel;
    private MarcaProcesadorPanel marcasPanel;
    private CatalagoPanel catalagoPanel;
    private MenuComponentesPanel menuComponentesPanel;
    private ResumenPanel resumenPanel;
    private JPanel cardsPanel;

    private CardLayout cardLayout;

    private JLabel tituloLbl;
    private TituloLabel subTItuloLabel;

    private Boton continuarBtn;
    private Boton retrocederBtn;

    private ArrayList<String> cardNames = new ArrayList<>();
    private int currentIndex = 0;

    public ArmarPcPanel() {
        super();
        tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(FontUtil.loadFont(28, "Inter_SemiBold"));
        tituloLbl.setForeground(Color.white);
        subTItuloLabel = new TituloLabel("CATEGORIAS");
        subTItuloLabel.setForeground(Color.white);

        categoriasPanel = new CategoriasPanel();
        marcasPanel = new MarcaProcesadorPanel();
        catalagoPanel = new CatalagoPanel("Procesador");
        menuComponentesPanel = new MenuComponentesPanel();
        resumenPanel = new ResumenPanel();
        cardLayout = new CardLayout();

        // Cards Panel
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setOpaque(false);
        cardsPanel.add(categoriasPanel, "Categorias");
        cardNames.add("Categorias");
        cardsPanel.add(marcasPanel, "Marcas");
        cardNames.add("Marcas");
        cardsPanel.add(catalagoPanel, "Procesadores");
        cardNames.add("Procesadores");

        // Boton
        continuarBtn = new Boton("→", 55, 35, 18, 20, Color.white, Estilos.COLOR_BACKGROUND, Estilos.COLOR_ATRAS_BOTON_HOOVER);
        retrocederBtn = new Boton("←", 55, 35, 18, 20, Color.white, Estilos.COLOR_BACKGROUND, Estilos.COLOR_ATRAS_BOTON_HOOVER);

        // Panel Norte
        panelNorte.add(retrocederBtn);
        panelNorte.add(subTItuloLabel);
        panelNorte.add(continuarBtn);

        // Panel Centro
        panelCentro.add(cardsPanel);

    }

    public void updateButtonState() {
        retrocederBtn.setEnabled(currentIndex > 0);
        continuarBtn.setEnabled(currentIndex < cardNames.size() - 1);
    }

    public void añadirMenusNavegacion(){
        panelOeste.add(menuComponentesPanel);
        panelEste.add(resumenPanel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.white);
        Font tittleFont = FontUtil.loadFont(30, "Inter_SemiBold");
        g2d.setFont(tittleFont);
        g2d.drawString("Armar PC", 500, 45);

        g2d.dispose();
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

    public ArrayList<String> getCardNames() {
        return cardNames;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }
}
