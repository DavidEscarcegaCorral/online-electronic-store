package compartido.cards;

import compartido.estilos.Estilos;
import compartido.estilos.FontUtil;
import javax.swing.*;
import java.awt.*;

public class TotalCard extends JPanel {
    private JLabel totalLbl;

    public TotalCard() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setPreferredSize(new Dimension(220, 40));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        totalLbl = new JLabel("Total: $0.00");
        totalLbl.setForeground(Color.white);
        totalLbl.setFont(FontUtil.loadFont(16, "Inter_SemiBold"));
        add(totalLbl);
    }

    public void setTotal(double total) {
        totalLbl.setText(String.format("Total: $%,.2f", total));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Estilos.COLOR_TITULO_LABEL);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        g2d.dispose();
    }
}
