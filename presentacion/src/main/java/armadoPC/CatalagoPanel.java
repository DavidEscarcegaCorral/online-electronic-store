package armadoPC;

import compartido.cards.ProductoCard;
import compartido.estilos.Estilos;
import dao.ProductoDAO;
import entidades.ProductoEntidad;
import fachada.ArmadoFacade;
import fachada.IArmadoFacade;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CatalagoPanel extends JPanel {
    private final ProductoDAO productoDAO;
    private Consumer<String> onProductoSelected;

    private ProductoCard productoSeleccionado;
    public List<ProductoCard> productoCardList;

    public CatalagoPanel() {
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
                IArmadoFacade armadoFacade = ArmadoFacade.getInstance();
                dto.ComponenteDTO seleccionadoDTO = armadoFacade.getComponenteSeleccionado(nombreProducto);
                String seleccionadoId = seleccionadoDTO != null ? seleccionadoDTO.getId() : null;

                for (ProductoEntidad producto : productos) {
                    ProductoCard card = crearProductoCard(producto);

                    if (seleccionadoId != null && seleccionadoId.equals(producto.getId().toString())) {
                        card.setSeleccionado(true);
                        productoSeleccionado = card;
                    }

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

    private ProductoCard crearProductoCard(ProductoEntidad producto) {
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

        return card;
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
