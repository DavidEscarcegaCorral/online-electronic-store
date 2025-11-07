package presentacion.panels;

import estilos.Boton;
import estilos.Estilos;
import estilos.FontUtil;
import estilos.TituloLabel;

import javax.swing.*;
import java.awt.*;

public class ArmarEquipoPanel extends PanelBase {
    private static String titulo = "Armar PC";
    private JLabel tituloLbl;
    private TituloLabel subTItuloLabel;
    private JPanel categoriasPanel;
    private CategoriaPanel categoriaPanelGamer;
    private CategoriaPanel categoriaPanelOffice;
    private CategoriaPanel categoriaPanelDesing;
    private CategoriaPanel categoriaPanelCustom;
    private Boton continuarBtn;

    public ArmarEquipoPanel() {
        super();
        tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(FontUtil.loadFont(28, "Inter_SemiBold"));
        tituloLbl.setForeground(Color.white);
        subTItuloLabel = new TituloLabel("CATEGORIAS");
        subTItuloLabel.setForeground(Color.white);
        categoriaPanelGamer = new CategoriaPanel("GAMER", "/img/categorias/gamer.png");
        categoriaPanelOffice = new CategoriaPanel("OFFICE", "/img/categorias/oficina.png");
        categoriaPanelDesing = new CategoriaPanel("DESING", "/img/categorias/desing.png");
        categoriaPanelCustom  = new CategoriaPanel("CUSTOM", "/img/categorias/custom.png");

        categoriasPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Estilos.COLOR_NAV_INF);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2d.dispose();
            }
        };

        // Boton
        continuarBtn = new Boton("Continuar", 130, 30, 16, 25, Color.white, Estilos.COLOR_ENFASIS, Estilos.COLOR_ENFASIS_HOOVER);

        // Panel Norte
        panelNorte.add(tituloLbl);

        // panel Categoria
        categoriasPanel.add(categoriaPanelGamer);
        categoriasPanel.add(categoriaPanelOffice);
        categoriasPanel.add(categoriaPanelDesing);
        categoriasPanel.add(categoriaPanelCustom);
        categoriasPanel.setOpaque(false);

        // Panel Centro
        panelCentro.add(subTItuloLabel);
        panelCentro.add(categoriasPanel);

        // Panel Sur
        panelSur.add(continuarBtn);

    }
}
