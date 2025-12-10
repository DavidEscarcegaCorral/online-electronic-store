package venta.pedido;

import compartido.cards.ProductoPedidoCard;
import compartido.estilos.Estilos;
import entidades.ConfiguracionEntidad;
import fachada.IVentaFacade;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CatalagoPedidoPanel extends JPanel {
    private List<ProductoPedidoCard> productoPedidoCardList;

    public CatalagoPedidoPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(900, 650));
        setLayout(new FlowLayout(FlowLayout.LEFT, 25, 25));

        productoPedidoCardList = new ArrayList<>();
    }

    public void cargarProductosDelCarrito() {
        removeAll();
        productoPedidoCardList.clear();

        try {
            IVentaFacade ventaFacade = fachada.VentaFacade.getInstance();
            List<ConfiguracionEntidad> configuraciones = ventaFacade.obtenerConfiguracionesEnCarrito();

            if (configuraciones == null || configuraciones.isEmpty()) {
                JLabel mensajeVacio = new JLabel("No hay productos en el carrito");
                mensajeVacio.setForeground(Color.WHITE);
                mensajeVacio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                add(mensajeVacio);
            } else {
                for (ConfiguracionEntidad config : configuraciones) {
                    if (config.getComponentes() != null && !config.getComponentes().isEmpty()) {
                        for (Map<String, Object> componente : config.getComponentes()) {
                            ProductoPedidoCard card = crearProductoPedidoCard(componente);
                            productoPedidoCardList.add(card);
                            add(card);
                        }
                    }
                }
            }
        } catch (Exception e) {
            JLabel mensajeError = new JLabel("Error: " + e.getMessage());
            mensajeError.setForeground(Color.RED);
            mensajeError.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            add(mensajeError);
            System.err.println("Error al cargar productos del carrito");
            e.printStackTrace();
        }

        revalidate();
        repaint();
    }

    private ProductoPedidoCard crearProductoPedidoCard(Map<String, Object> componente) {
        String nombre = (String) componente.get("nombre");
        Object precioObj = componente.get("precio");
        Double precio = precioObj instanceof Number
                ? ((Number) precioObj).doubleValue()
                : 0.0;

        ProductoPedidoCard card = new ProductoPedidoCard(
                nombre,
                precio,
                "/img/productos/default.png"
        );

        return card;
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
