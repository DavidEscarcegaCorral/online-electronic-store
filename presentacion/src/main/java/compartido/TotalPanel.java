package compartido;

import compartido.estilos.Estilos;
import compartido.estilos.FontUtil;
import dto.CarritoDTO;
import javax.swing.*;
import java.awt.*;

public class TotalPanel extends JPanel {
    private JPanel panelNorte;
    private JPanel panelCentro;
    private JPanel panelSur;

    private JLabel subtotalLabel;
    private JLabel envioLabel;
    private JLabel totalLabel;

    public TotalPanel() {
        setLayout(new  GridLayout(0, 1, 0, 0));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(220,350));

        totalLabel = new JLabel();
        totalLabel.setText("Total");
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(FontUtil.loadFont(30, "Iceland-Regular"));

        panelNorte=new JPanel();
        panelNorte.add(totalLabel);
        panelNorte.setOpaque(false);

        subtotalLabel = new JLabel();
        subtotalLabel.setText("Subtotal: $"+ 0);
        subtotalLabel.setForeground(Color.WHITE);
        subtotalLabel.setFont(FontUtil.loadFont(18, "Inter_Light"));

        envioLabel = new  JLabel();
        envioLabel.setText("Envio: $"+ 0);
        envioLabel.setForeground(Color.WHITE);
        envioLabel.setFont(FontUtil.loadFont(18, "Inter_Light"));

        panelCentro=new JPanel();
        panelCentro.setLayout(new GridLayout(0,1));
        panelCentro.add(subtotalLabel);
        panelCentro.add(envioLabel);
        panelCentro.setOpaque(false);

        panelSur = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Estilos.COLOR_TITULO_LABEL);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2d.dispose();
            }
        };
        panelSur.setOpaque(false);
        panelSur.setPreferredSize(new Dimension(190,40));

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
