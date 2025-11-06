package estilos;

import javax.swing.*;
import java.awt.*;

public class BotonHighSpecs extends JButton {
    private final String TEXT_HIGH = "HIGH";
    private final String TEXT_SPECS = "SPECS";

    private final Font FONT_HIGH = FontUtil.loadFont(54, "Iceland-Regular");
    private final Font FONT_SPECS = FontUtil.loadFont(42, "IBMPlexSans-MediumItalic");

    private final Color COLOR_HIGH = Estilos.COLOR_ENFASIS;
    private final Color COLOR_SPECS = Color.WHITE;

    private final int GAP = 8;

    public BotonHighSpecs() {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);

        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        FontMetrics fmHigh = g2.getFontMetrics(FONT_HIGH);
        FontMetrics fmSpecs = g2.getFontMetrics(FONT_SPECS);

        int highWidth = fmHigh.stringWidth(TEXT_HIGH);
        int specsWidth = fmSpecs.stringWidth(TEXT_SPECS);
        int totalWidth = highWidth + GAP + specsWidth;

        int x = (getWidth() - totalWidth) / 2;
        int y = (getHeight() - fmHigh.getHeight()) / 2 + fmHigh.getAscent();

        g2.setFont(FONT_HIGH);
        g2.setColor(COLOR_HIGH);
        g2.drawString(TEXT_HIGH, x, y);

        g2.setFont(FONT_SPECS);
        g2.setColor(COLOR_SPECS);
        g2.drawString(TEXT_SPECS, x + highWidth + GAP, y);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fmHigh = this.getFontMetrics(FONT_HIGH);
        FontMetrics fmSpecs = this.getFontMetrics(FONT_SPECS);

        int width = fmHigh.stringWidth(TEXT_HIGH) + GAP + fmSpecs.stringWidth(TEXT_SPECS);
        int height = Math.max(fmHigh.getHeight(), fmSpecs.getHeight());

        int paddingX = 20;
        int paddingY = 10;

        return new Dimension(width + paddingX, height + paddingY);
    }
}
