package armadoPC;

import compartido.cards.ProductoCard;
import compartido.estilos.Estilos;
import compartido.estilos.scroll.ScrollPaneCustom;
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

    private JPanel itemsPanel;
    private ScrollPaneCustom scroll;

    public CatalagoPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(740, 650));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());

        productoCardList = new ArrayList<>();
        productoDAO = new ProductoDAO();

        itemsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 25));
        itemsPanel.setOpaque(false);
        itemsPanel.setPreferredSize(new Dimension(680, 650));

        scroll = new ScrollPaneCustom(itemsPanel);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(720, 600));

        add(scroll, BorderLayout.CENTER);
    }

    public void setOnProductoSelected(Consumer<String> callback) {
        this.onProductoSelected = callback;
    }

    public void cargarLista(String nombreProducto) {
        itemsPanel.removeAll();
        productoCardList.clear();
        productoSeleccionado = null;

        try {
            List<ProductoEntidad> productos = productoDAO.obtenerPorCategoria(nombreProducto);

            if (productos == null || productos.isEmpty()) {
                JLabel mensajeVacio = new JLabel("No hay productos disponibles en esta categoría");
                mensajeVacio.setForeground(Color.WHITE);
                mensajeVacio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                itemsPanel.add(mensajeVacio);
                itemsPanel.setPreferredSize(new Dimension(680, 650));
                itemsPanel.revalidate();
                itemsPanel.repaint();
                return;
            }

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
                itemsPanel.add(card);
            }

            SwingUtilities.invokeLater(() -> {
                int numProductos = productos.size();
                int cardsPerRow = 3;
                int numRows = (int) Math.ceil((double) numProductos / cardsPerRow);
                int cardHeight = 310;
                int verticalGap = 25;
                int totalHeight = (numRows * cardHeight) + ((numRows + 1) * verticalGap) + 40;

                itemsPanel.setPreferredSize(new Dimension(680, totalHeight));
                itemsPanel.revalidate();
                itemsPanel.repaint();
                scroll.revalidate();
                scroll.repaint();
            });
        } catch (Exception e) {
            JLabel mensajeError = new JLabel("Error: " + e.getMessage());
            mensajeError.setForeground(Color.RED);
            mensajeError.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            itemsPanel.add(mensajeError);
            System.err.println("Error al cargar productos de categoría: " + nombreProducto);
            e.printStackTrace();
        }
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
