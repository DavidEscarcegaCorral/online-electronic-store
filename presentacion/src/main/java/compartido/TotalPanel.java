package compartido;

import compartido.estilos.Estilos;
import compartido.estilos.FontUtil;

import javax.swing.*;
import java.awt.*;

public class TotalPanel extends JPanel {
    private JPanel panelNorte;
    private JPanel panelCentro;
    private JPanel panelSur;

    private JLabel subtotalLabel;
    private JLabel envioLabel;
    private JLabel totalLabel;
    private JLabel totalValueLabel;

    public TotalPanel() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(220,350));

        panelNorte = new JPanel();
        panelNorte.setOpaque(false);
        panelNorte.setPreferredSize(new Dimension(220, 40));
        panelNorte.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 8));
        JLabel titulo = new JLabel("Total");
        titulo.setFont(FontUtil.loadFont(18, "Inter_SemiBold"));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelNorte.add(titulo);

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
        panelSur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 8));

        totalLabel = new JLabel("Total:");
        totalLabel.setFont(FontUtil.loadFont(14, "Inter_SemiBold"));
        totalLabel.setForeground(Color.WHITE);

        totalValueLabel = new JLabel("$0.00");
        totalValueLabel.setFont(FontUtil.loadFont(16, "Inter_SemiBold"));
        totalValueLabel.setForeground(Color.WHITE);

        panelSur.add(totalLabel);
        panelSur.add(totalValueLabel);

        add(panelNorte);
        add(panelSur);
    }

    public void actualizarTotal() {
        try {
            fachada.IVentaFacade ventaFacade = fachada.VentaFacade.getInstance();
            double total = ventaFacade.calcularTotalCarrito();
            totalValueLabel.setText(String.format("$%,.2f", total));
        } catch (Exception e) {
            totalValueLabel.setText("$0.00");
        }
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
