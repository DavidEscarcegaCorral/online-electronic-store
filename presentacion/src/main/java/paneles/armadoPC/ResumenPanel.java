package paneles.armadoPC;


import cards.TotalCard;
import estilos.Estilos;
import estilos.FontUtil;
import estilos.ScrollPaneCustom;
import estilos.tabla.Tabla;
import frames.FramePrincipal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ResumenPanel extends JPanel {
    private JLabel tituloLbl;
    private TotalCard totalCard;
    private JPanel panelNorte;
    private JPanel panelCentro;
    private JPanel panelSur;
    private ScrollPaneCustom scroll;

    public ResumenPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(240, 610));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        tituloLbl = new JLabel("Resumen");
        tituloLbl.setForeground(Color.white);
        tituloLbl.setFont(FontUtil.loadFont(32, "Iceland-Regular"));

        totalCard = new TotalCard();

        // Panel Norte
        panelNorte = new JPanel();
        panelNorte.setOpaque(false);
        panelNorte.add(tituloLbl);

        // Panel Centro
        panelCentro = new JPanel();
        panelCentro.setPreferredSize(new Dimension(250, 560));
        panelCentro.setOpaque(false);

        // Panel Sur
        panelSur = new JPanel();
        panelSur.setOpaque(false);
        panelSur.add(totalCard);

        add(panelNorte);
        add(panelCentro);
        add(panelSur);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Estilos.COLOR_NAV_INF);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        g2d.dispose();
    }
}
