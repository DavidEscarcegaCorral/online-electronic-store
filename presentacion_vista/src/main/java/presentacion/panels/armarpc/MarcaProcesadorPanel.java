package presentacion.panels.armarpc;

import estilos.Estilos;
import presentacion.panels.cards.MarcaProcesadorCard;

import javax.swing.*;
import java.awt.*;

public class MarcaProcesadorPanel extends JPanel {
    private static MarcaProcesadorCard intel = new MarcaProcesadorCard("Intel", "/img/marcas/intelLogo.png");
    private static MarcaProcesadorCard amd = new MarcaProcesadorCard("AMD", "/img/marcas/AMDLogo.png");

    public MarcaProcesadorPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));
        add(intel);
        add(amd);
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
