package paneles.armadoPC;

import estilos.Estilos;
import estilos.ScrollPaneCustom;
import estilos.tabla.Tabla;
import frames.FramePrincipal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EvaluarConfiguracionPanel extends JPanel {


    public EvaluarConfiguracionPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(240, 700));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


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
