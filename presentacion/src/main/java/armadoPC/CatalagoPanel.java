package armadoPC;

import compartido.cards.ProductoCard;
import compartido.estilos.Estilos;
import dao.ProductoDAO;
import entidades.ProductoEntidad;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CatalagoPanel extends JPanel {
    public List<ProductoCard> productoCardList;
    private Consumer<String> onProductoSelected;
    private ProductoCard productoSeleccionado;
    private ProductoDAO productoDAO;

    public CatalagoPanel(String Producto) {
        setOpaque(false);
        setPreferredSize(new Dimension(740, 650));
        setLayout(new FlowLayout(FlowLayout.LEFT, 25, 25));
        productoCardList = new ArrayList<>();
        productoDAO = new ProductoDAO();
    }

    public void setOnProductoSelected(Consumer<String> callback) {
        this.onProductoSelected = callback;
    }

    public void cargarLista(String nombreProducto) {
        removeAll();
        productoCardList.clear();
        productoSeleccionado = null;

        try {
            List<ProductoEntidad> productos = productoDAO.obtenerPorCategoria(nombreProducto);

            if (productos == null || productos.isEmpty()) {
                JLabel mensajeVacio = new JLabel("No hay productos disponibles en esta categoría");
                mensajeVacio.setForeground(Color.WHITE);
                mensajeVacio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                add(mensajeVacio);
            } else {
                for (ProductoEntidad producto : productos) {
                    ProductoCard card = new ProductoCard(
                            producto.getId().toString(),
                            producto.getNombre(),
                            producto.getPrecio(),
                            "/img/productos/default.png"
                    );

                    card.setOnSelect(id -> {
                        if (productoSeleccionado != null) {
                            productoSeleccionado.setSeleccionado(false);
                        }
                        card.setSeleccionado(true);
                        productoSeleccionado = card;

                        if (onProductoSelected != null) {
                            onProductoSelected.accept(id);
                        }
                    });

                    productoCardList.add(card);
                    add(card);
                }
            }
        } catch (Exception e) {
            JLabel mensajeError = new JLabel("Error: " + e.getMessage());
            mensajeError.setForeground(Color.RED);
            mensajeError.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            add(mensajeError);
            System.err.println("Error al cargar productos de categoría: " + nombreProducto);
            e.printStackTrace();
        }

        revalidate();
        repaint();

    }

    public void cargarLista(java.util.List<dto.ComponenteDTO> componentes) {
        removeAll();
        productoCardList.clear();
        productoSeleccionado = null;

        if (componentes == null || componentes.isEmpty()) {
            JLabel mensajeVacio = new JLabel("No hay productos disponibles");
            mensajeVacio.setForeground(Color.WHITE);
            mensajeVacio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            add(mensajeVacio);
            revalidate();
            repaint();
            return;
        }

        for (dto.ComponenteDTO dto : componentes) {
            ProductoCard card = new ProductoCard(
                    dto.getId(),
                    dto.getNombre(),
                    dto.getPrecio(),
                    "/img/productos/default.png");

            card.setOnSelect(id -> {
                if (productoSeleccionado != null) {
                    productoSeleccionado.setSeleccionado(false);
                }
                card.setSeleccionado(true);
                productoSeleccionado = card;

                if (onProductoSelected != null) {
                    onProductoSelected.accept(id);
                }
            });

            productoCardList.add(card);
            add(card);
        }

        revalidate();
        repaint();
    }

    public ProductoCard getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void limpiarSeleccion() {
        if (productoSeleccionado != null) {
            productoSeleccionado.setSeleccionado(false);
            productoSeleccionado = null;
        }
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
