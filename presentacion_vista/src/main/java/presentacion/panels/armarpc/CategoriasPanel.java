package presentacion.panels.armarpc;

import estilos.Estilos;
import presentacion.panels.cards.CategoriaCard;

import javax.swing.*;
import java.awt.*;

public class CategoriasPanel extends JPanel {
    private static CategoriaCard categoriaCardGamer = new CategoriaCard("GAMER", "/img/categorias/gamer.png");
    private static CategoriaCard categoriaCardOffice = new CategoriaCard("OFFICE", "/img/categorias/oficina.png");
    private static CategoriaCard categoriaCardDesing = new CategoriaCard("DESING", "/img/categorias/desing.png");
    private static CategoriaCard categoriaCardCustom = new CategoriaCard("CUSTOM", "/img/categorias/custom.png");

    public CategoriasPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 25, 25));
        add(categoriaCardGamer);
        add(categoriaCardOffice);
        add(categoriaCardDesing);
        add(categoriaCardCustom);
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
