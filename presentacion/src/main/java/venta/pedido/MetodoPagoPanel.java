package venta.pedido;

import compartido.estilos.Estilos;

import javax.swing.*;
import java.awt.*;

public class MetodoPagoPanel extends JPanel {
    private JPanel panelDireccionPanel;
    private JPanel tarjetaInfoPanel;

    public MetodoPagoPanel() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        panelDireccionPanel = new JPanel() {
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
        panelDireccionPanel.setOpaque(false);
        panelDireccionPanel.setPreferredSize(new Dimension(300, 240));
        panelDireccionPanel.setAlignmentX(LEFT_ALIGNMENT);


        tarjetaInfoPanel = new JPanel() {
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
        tarjetaInfoPanel.setOpaque(false);
        tarjetaInfoPanel.setPreferredSize(new Dimension(300, 80));
        tarjetaInfoPanel.setAlignmentX(LEFT_ALIGNMENT);

        add(panelDireccionPanel);
        add(Box.createVerticalStrut(15));
        add(tarjetaInfoPanel);
    }

}
