package venta.producto;

import compartido.estilos.Estilos;

import javax.swing.*;
import java.awt.*;

public class DetallesPanel extends JPanel {
    private JLabel detallesLabel;

    public DetallesPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        detallesLabel = new JLabel("<html><body style='width: 400px; padding: 10px;'>Aquí van los detalles del producto. Este texto es un ejemplo para mostrar cómo se verán los detalles dentro del panel.</body></html>");
        detallesLabel.setForeground(Color.WHITE);
        detallesLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        add(detallesLabel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Estilos.COLOR_NAV_SUP);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        g2d.dispose();
    }
}
