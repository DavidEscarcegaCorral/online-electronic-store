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
        panelCentro.setPreferredSize(new Dimension(250, 560));
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        // Panel Sur
        panelSur = new JPanel();
        panelSur.setOpaque(false);
        panelSur.add(totalCard);

        add(panelNorte);
        add(new JScrollPane(panelCentro));
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
            JPanel row = new JPanel();
            row.setOpaque(false);
            row.setLayout(new BorderLayout());
            JLabel name = new JLabel(cat + ": " + comp.getNombre());
            name.setForeground(Color.white);
            row.add(name, BorderLayout.CENTER);
            JLabel price = new JLabel(String.format("$%,.2f", comp.getPrecio()));
            price.setForeground(Color.white);
            row.add(price, BorderLayout.EAST);
            panelCentro.add(row);
            panelCentro.add(Box.createVerticalStrut(8));
        });

        // actualizar total
        double total = ensamblaje.getPrecioTotal();
        // reemplazamos el label dentro de TotalCard mediante reflexión simple o reemplazo del componente
        panelSur.removeAll();
        totalCard = new TotalCard();
        // Actualizar texto del totalCard: es sencillo recrearlo con un label adicional debajo
        JLabel totalLbl = new JLabel(String.format("Total: $%,.2f", total));
        totalLbl.setForeground(Color.white);
        totalLbl.setFont(FontUtil.loadFont(14, "Inter_SemiBold"));
        panelSur.add(totalCard);
        panelSur.add(totalLbl);

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
