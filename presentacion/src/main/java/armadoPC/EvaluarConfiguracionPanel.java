package armadoPC;

import compartido.cards.ProductoCard;
import compartido.estilos.Estilos;
import compartido.estilos.FontUtil;
import compartido.estilos.scroll.ScrollPaneCustom;
import dto.EnsamblajeDTO;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class EvaluarConfiguracionPanel extends JPanel {
    private JPanel itemsPanel;
    private ScrollPaneCustom scroll;

    public EvaluarConfiguracionPanel() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());

        itemsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        itemsPanel.setOpaque(false);
        itemsPanel.setPreferredSize(new Dimension(680, 100));

        scroll = new ScrollPaneCustom(itemsPanel);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(720, 600));

        add(scroll, BorderLayout.CENTER);
    }

    public void updateFrom(EnsamblajeDTO ensamblaje) {
        itemsPanel.removeAll();

        if (ensamblaje == null || ensamblaje.getComponentes().isEmpty()) {
            JLabel vacio = new JLabel("No hay componentes seleccionados");
            vacio.setForeground(Color.WHITE);
            vacio.setFont(FontUtil.loadFont(16, "Inter_SemiBold"));
            itemsPanel.add(vacio);
            itemsPanel.setPreferredSize(new Dimension(680, 100));
            itemsPanel.revalidate();
            itemsPanel.repaint();
            return;
        }

        for (Map.Entry<String, dto.ComponenteDTO> entry : ensamblaje.getComponentes().entrySet()) {
            dto.ComponenteDTO comp = entry.getValue();
            ProductoCard card = new ProductoCard(
                    comp.getId(),
                    comp.getNombre(),
                    comp.getPrecio(),
                    "/img/productos/default.png"
            );
            itemsPanel.add(card);
        }

        SwingUtilities.invokeLater(() -> {
            int numProductos = ensamblaje.getComponentes().size();
            int cardsPerRow = 3;
            int numRows = (int) Math.ceil((double) numProductos / cardsPerRow);
            int cardHeight = 310;
            int verticalGap = 20;
            int totalHeight = (numRows * cardHeight) + ((numRows + 1) * verticalGap) + 40;

            itemsPanel.setPreferredSize(new Dimension(680, totalHeight));
            itemsPanel.revalidate();
            itemsPanel.repaint();
            scroll.revalidate();
            scroll.repaint();
        });
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
