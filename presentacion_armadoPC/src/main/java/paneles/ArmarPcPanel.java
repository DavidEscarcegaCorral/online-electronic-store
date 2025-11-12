package paneles;

import estilos.Boton;
import estilos.Estilos;
import estilos.FontUtil;
import estilos.TituloLabel;

import javax.swing.*;
import java.awt.*;

public class ArmarPcPanel extends PanelBase {
    private static String titulo = "Armar PC";

    private CategoriasPanel categoriasPanel;
    private MarcaProcesadorPanel marcasPanel;
    private EvaluarConfiguracionPanel evaluarConfiguracionPanel;

    private MenuComponentesPanel menuComponentesPanel;
    private ResumenPanel resumenPanel;

    private JPanel cardsPanel;
    private CardLayout cardLayout;

    private JLabel tituloLbl;
    private TituloLabel subTItuloLabel;

    private Boton continuarBtn;
    private Boton retrocederBtn;

    private String[] listaPasosArmado;
    private int indiceActual = 0;

    public ArmarPcPanel() {
        super();
        iniciarComponentes();

        // Panel Norte
        panelNorte.add(retrocederBtn);
        panelNorte.add(subTItuloLabel);
        panelNorte.add(continuarBtn);

        // Panel Centro
        panelCentro.add(cardsPanel);

    }

    public void iniciarComponentes() {
        // Titulo label
        tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(FontUtil.loadFont(28, "Inter_SemiBold"));
        tituloLbl.setForeground(Color.white);
        listaPasosArmado = new String[15];


        // Card de categorias
        categoriasPanel = new CategoriasPanel();

        // Card de Marcas
        marcasPanel = new MarcaProcesadorPanel();

        //Card de Evaluar configuracion
        evaluarConfiguracionPanel = new EvaluarConfiguracionPanel();

        // Menu lateral de los componentes
        menuComponentesPanel = new MenuComponentesPanel();

        // Menu lateral de resumen de configuracion
        resumenPanel = new ResumenPanel();

        continuarBtn = new Boton("→", 55, 40, 22, 20, Color.white, Estilos.COLOR_BACKGROUND, Estilos.COLOR_ATRAS_BOTON_HOOVER);
        continuarBtn.setNewFont();
        retrocederBtn = new Boton("←", 55, 40, 22, 20, Color.white, Estilos.COLOR_BACKGROUND, Estilos.COLOR_ATRAS_BOTON_HOOVER);
        retrocederBtn.setNewFont();

        cargarMenu();
        cargarCards();

    }

    public void cargarCards() {
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setOpaque(false);
        cardsPanel.add(categoriasPanel, listaPasosArmado[0]);
        cardsPanel.add(marcasPanel, listaPasosArmado[1]);

        for (int i = 2; i < listaPasosArmado.length - 1; i++) {
            cardsPanel.add(new CatalagoPanel(listaPasosArmado[i]), listaPasosArmado[i]);
        }

        cardsPanel.add(evaluarConfiguracionPanel, listaPasosArmado[14]);

        subTItuloLabel = new TituloLabel(listaPasosArmado[indiceActual]);
        subTItuloLabel.setForeground(Color.white);
    }

    public void updateButtonState() {
        retrocederBtn.setEnabled(indiceActual > 0);
        continuarBtn.setEnabled(indiceActual < listaPasosArmado.length);
    }

    public void añadirMenusNavegacion() {
        panelOeste.add(menuComponentesPanel);
        panelEste.add(resumenPanel);
    }

    public void eliminarMenusNavegacion() {
        panelOeste.remove(menuComponentesPanel);
        panelEste.remove(resumenPanel);
    }

    public void cargarMenu() {
        listaPasosArmado[0] = "Tipo de PC";
        listaPasosArmado[1] = "Marca del procesador";
        listaPasosArmado[2] = "Procesador";
        listaPasosArmado[3] = "Tarjeta Madre";
        listaPasosArmado[4] = "Memoria RAM";
        listaPasosArmado[5] = "Almacenamiento";
        listaPasosArmado[6] = "Unidad SSD";
        listaPasosArmado[7] = "Tarjeta de video";
        listaPasosArmado[8] = "Fuente de poder";
        listaPasosArmado[9] = "Disipador";
        listaPasosArmado[10] = "Ventilador";
        listaPasosArmado[11] = "Monitor";
        listaPasosArmado[12] = "Kit de telcado/raton";
        listaPasosArmado[13] = "Redes e internet";
        listaPasosArmado[14] = "Resumen";

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.white);

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

    public String[] getListaPasosArmado() {
        return listaPasosArmado;
    }

    public int getIndiceActual() {
        return indiceActual;
    }

    public void setIndiceActual(int indiceActual) {
        this.indiceActual = indiceActual;
    }

    public TituloLabel getSubTItuloLabel() {
        return subTItuloLabel;
    }

    public MenuComponentesPanel getMenuComponentesPanel() {
        return menuComponentesPanel;
    }
}
