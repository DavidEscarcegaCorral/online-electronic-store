package compartido.estilos.textfields;

import compartido.estilos.FontUtil;
import compartido.estilos.RoundBorder;

import javax.swing.*;
import java.awt.*;

public class PsswrdFieldPh extends JPasswordField {
    private String txtPlaceholder;
    private int width;
    private int height;
    private int fontSize;
    private int cornerRadius;

    public PsswrdFieldPh(String txtPlaceholder, int width, int height, int fontSize, int cornerRadius) {
        this.txtPlaceholder = txtPlaceholder;
        this.width = width;
        this.height = height;
        this.fontSize = fontSize;
        this.cornerRadius = cornerRadius;
        setFont(FontUtil.loadFont(fontSize, "Inter_Regular"));
        setPreferredSize(new Dimension(width, height));
        setBorder(new RoundBorder(this.cornerRadius, Color.white));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2));

        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        if ((getPassword().length == 0)) {
            g2d.setColor(new Color(172, 172, 172));
            g2d.setFont(FontUtil.loadFont(this.fontSize, "Inter_Regular"));
            FontMetrics fm = g2d.getFontMetrics();
            int padding = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(this.txtPlaceholder, 15, padding);
            g2d.dispose();
        }

        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }
}

