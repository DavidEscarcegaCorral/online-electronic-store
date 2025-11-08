package presentacion.panels.armarpc;

import estilos.Estilos;
import presentacion.panels.cards.ProductoCard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CatalagoPanel extends JPanel {
    public List<ProductoCard> productoCardList;

    public CatalagoPanel(String Producto) {
        setOpaque(false);
        setPreferredSize(new Dimension(780, 700));
        setLayout(new FlowLayout(FlowLayout.LEFT, 25, 25));
        productoCardList = new ArrayList<>();
        cargarLista();

    }

    public void cargarLista(){
        ProductoCard card1 = new ProductoCard(
                "P001",
                "Procesador AMD Ryzen 7 5700G Octa Core 3.8GHz 20MB Socket AM4 100-100000263BOX",
                1419.00,
                "/img/productos/procesadores/Ryzen5.jpg"
        );
        ProductoCard card2 = new ProductoCard(
                "P001",
                "Procesador AMD Ryzen 7 5700G Octa Core 3.8GHz 20MB Socket AM4 100-100000263BOX",
                1419.00,
                "/img/productos/procesadores/Ryzen5.jpg"
        );
        ProductoCard card3 = new ProductoCard(
                "P001",
                "Procesador AMD Ryzen 7 5700G Octa Core 3.8GHz 20MB Socket AM4 100-100000263BOX",
                1419.00,
                "/img/productos/procesadores/Ryzen5.jpg"
        );
        ProductoCard card4 = new ProductoCard(
                "P001",
                "Procesador AMD Ryzen 7 5700G Octa Core 3.8GHz 20MB Socket AM4 100-100000263BOX",
                1419.00,
                "/img/productos/procesadores/Ryzen5.jpg"
        );
        ProductoCard card5 = new ProductoCard(
                "P001",
                "Procesador AMD Ryzen 7 5700G Octa Core 3.8GHz 20MB Socket AM4 100-100000263BOX",
                1419.00,
                "/img/productos/procesadores/Ryzen5.jpg"
        );

        add(card1);
        add(card2);
        add(card3);
        add(card4);
        add(card5);
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
