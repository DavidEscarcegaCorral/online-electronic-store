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
        continuarBtn = new Boton("Continuar", 130, 30, 16, 25, Color.white, Estilos.COLOR_ENFASIS, Estilos.COLOR_ENFASIS_HOOVER);
        retrocederBtn = new Boton("Volver", 130, 30, 16, 25, Color.white, Estilos.COLOR_ENFASIS, Estilos.COLOR_ENFASIS_HOOVER);

        // Panel Norte
        panelNorte.add(tituloLbl);
        panelNorte.add(subTItuloLabel);

        // Panel Centro
        panelCentro.add(cardsPanel);

        // Panel Sur
        panelSur.add(retrocederBtn);
        panelSur.add(continuarBtn);

    }

    public void updateButtonState() {
        retrocederBtn.setEnabled(currentIndex > 0);
        continuarBtn.setEnabled(currentIndex < cardNames.size() - 1);
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
