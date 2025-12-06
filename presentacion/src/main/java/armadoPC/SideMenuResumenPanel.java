package armadoPC;


import compartido.cards.TotalCard;
import compartido.estilos.Estilos;
import compartido.estilos.FontUtil;
import compartido.estilos.scroll.ScrollPaneCustom;

import javax.swing.*;
import java.awt.*;

public class SideMenuResumenPanel extends JPanel {
    private JLabel tituloLbl;
    private TotalCard totalCard;
    private JPanel panelNorte;
    private JPanel panelCentro;
    private JPanel panelSur;
    private ScrollPaneCustom scroll;

    public SideMenuResumenPanel() {
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
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        scroll = new ScrollPaneCustom(panelCentro);
        scroll.setPreferredSize(new Dimension(220, 480));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Panel Sur
        panelSur = new JPanel();
        panelSur.setOpaque(false);
        panelSur.add(totalCard);

        add(panelNorte);
        add(scroll);
        add(panelSur);

    }

    public void updateFrom(dto.EnsamblajeDTO ensamblaje) {
        panelCentro.removeAll();
        if (ensamblaje == null) {
            revalidate();
            repaint();
            return;
        }

        ensamblaje.getComponentes().forEach((cat, comp) -> {
            JPanel itemPanel = new JPanel();
            itemPanel.setOpaque(false);
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            itemPanel.setMaximumSize(new Dimension(200, 60));

            JLabel categoryLabel = new JLabel(cat);
            categoryLabel.setForeground(Color.WHITE);
            categoryLabel.setFont(FontUtil.loadFont(15, "Inter_SemiBold"));
            categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel nameLabel = new JLabel(comp.getNombre());
            nameLabel.setForeground(new Color(200, 200, 200));
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel priceLabel = new JLabel(String.format("$%,.2f", comp.getPrecio()));
            priceLabel.setForeground(Estilos.COLOR_ENFASIS);
            priceLabel.setFont(FontUtil.loadFont(14, "Inter_SemiBold"));
            priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            itemPanel.add(categoryLabel);
            itemPanel.add(Box.createVerticalStrut(2));
            itemPanel.add(nameLabel);
            itemPanel.add(Box.createVerticalStrut(2));
            itemPanel.add(priceLabel);

            panelCentro.add(itemPanel);
            panelCentro.add(Box.createVerticalStrut(10));
        });

        double total = ensamblaje.getPrecioTotal();
        totalCard.setTotal(total);

        revalidate();
        repaint();
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
