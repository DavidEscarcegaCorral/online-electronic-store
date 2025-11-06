package presentacion.panels;

import estilos.Boton;
import estilos.Estilos;
import estilos.TituloLabel;

import javax.swing.*;
import java.awt.*;

public class ArmarEquipoPanel extends PanelBase {
    private static String titulo = "Aramr Equipo";
    private JLabel subTItuloLabel;
    private JPanel categoriaPanel;
    private Boton continuarBtn;

    public ArmarEquipoPanel() {
        super();
        tituloLabel = new TituloLabel(titulo);
        subTItuloLabel = new JLabel("Categoria");
        subTItuloLabel.setForeground(Color.white);
        subTItuloLabel.setFont(new Font("Arial", Font.BOLD, 20));
        categoriaPanel = new JPanel() {
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
        continuarBtn = new Boton("Continuar", 130, 30, 16, 25, Color.white, Estilos.COLOR_ENFASIS, Estilos.COLOR_ENFASIS_HOOVER);

        // Panel Norte
        panelNorte.add(tituloLabel);

        // panel Categoria
        categoriaPanel.setOpaque(false);
        categoriaPanel.add(subTItuloLabel);

        // Panel Centro
        panelCentro.add(categoriaPanel);

        // Panel Sur
        panelSur.add(continuarBtn);

    }
}
