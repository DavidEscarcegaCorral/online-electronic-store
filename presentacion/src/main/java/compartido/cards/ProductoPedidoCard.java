package compartido.cards;

import compartido.estilos.FontUtil;
import dto.ItemCarritoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tarjeta optimizada para mostrar productos en pedidos.
 *
 */
public class ProductoPedidoCard extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(ProductoPedidoCard.class);

    private static final ConcurrentHashMap<String, ImageIcon> IMAGE_CACHE = new ConcurrentHashMap<>();
    private static final int IMAGE_WIDTH = 100;
    private static final int IMAGE_HEIGHT = 100;

    private JLabel imagenProductoLbl;
    private JLabel nombreProductoLbl;
    private JLabel precioProductoLbl;
    private JLabel cantidadLbl;

    private String imagenUrl;
    private String nombreProducto;
    private double precioProducto;
    private int cantidad;

    /**
     * Constructor con DTO (RECOMENDADO).
     *
     * @param item DTO con toda la información del item
     */
    public ProductoPedidoCard(ItemCarritoDTO item) {
        this(item.getNombre(), item.getPrecioUnitario(), null, item.getCantidad());
    }

    /**
     * Constructor legacy (mantiene compatibilidad).
     */
    public ProductoPedidoCard(String nombreProducto, double productoPrecio, String imagenUrl) {
        this(nombreProducto, productoPrecio, imagenUrl, 1);
    }

    /**
     * Constructor completo.
     */
    private ProductoPedidoCard(String nombreProducto, double productoPrecio, String imagenUrl, int cantidad) {
        this.nombreProducto = nombreProducto;
        this.precioProducto = productoPrecio;
        this.imagenUrl = imagenUrl;
        this.cantidad = cantidad;

        initComponents();
        añadirComponentes();
        cargarImagenOptimizado();
    }

    private void initComponents() {
        setOpaque(false);
        setPreferredSize(new Dimension(400, 130));
        setMaximumSize(new Dimension(430, 150));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout(15, 0));

        imagenProductoLbl = new JLabel();
        imagenProductoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagenProductoLbl.setAlignmentY(Component.CENTER_ALIGNMENT);
        imagenProductoLbl.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));

        String nombreHTML = String.format("<html><body style='width: 220px'>%s</body></html>",
                                         nombreProducto != null ? nombreProducto : "Sin nombre");
        nombreProductoLbl = new JLabel(nombreHTML);
        nombreProductoLbl.setFont(FontUtil.loadFont(12, "Inter_Light"));
        nombreProductoLbl.setForeground(Color.BLACK);
        nombreProductoLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        precioProductoLbl = new JLabel(String.format("$%,.2f", precioProducto));
        precioProductoLbl.setFont(new Font("Arial", Font.BOLD, 18));
        precioProductoLbl.setForeground(Color.BLACK);
        precioProductoLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (cantidad > 1) {
            cantidadLbl = new JLabel(String.format("Cantidad: %d", cantidad));
            cantidadLbl.setFont(FontUtil.loadFont(10, "Inter_Regular"));
            cantidadLbl.setForeground(Color.GRAY);
            cantidadLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
    }

    private void añadirComponentes() {
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setPreferredSize(new Dimension(120, 150));
        panelIzquierdo.add(Box.createVerticalGlue());
        panelIzquierdo.add(imagenProductoLbl);
        panelIzquierdo.add(Box.createVerticalGlue());

        JPanel panelDerecho = new JPanel();
        panelDerecho.setOpaque(false);
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.add(Box.createVerticalStrut(10));
        panelDerecho.add(nombreProductoLbl);
        panelDerecho.add(Box.createVerticalStrut(5));
        if (cantidadLbl != null) {
            panelDerecho.add(cantidadLbl);
            panelDerecho.add(Box.createVerticalStrut(5));
        }
        panelDerecho.add(Box.createVerticalStrut(10));
        panelDerecho.add(precioProductoLbl);

        add(panelIzquierdo, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);
    }

    /**
     * Carga la imagen usando caché.
     */
    private void cargarImagenOptimizado() {
        String cacheKey = nombreProducto != null ? nombreProducto : "default";

        ImageIcon cachedIcon = IMAGE_CACHE.get(cacheKey);
        if (cachedIcon != null) {
            imagenProductoLbl.setIcon(cachedIcon);
            return;
        }

        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() {
                try {
                    String rutaImagen = buscarRutaImagen();
                    java.net.URL location = getClass().getResource(rutaImagen);

                    if (location != null) {
                        ImageIcon icon = new ImageIcon(location);
                        if (icon.getIconWidth() != -1) {
                            Image originalImage = icon.getImage();
                            Image scaledImage = originalImage.getScaledInstance(
                                IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH
                            );
                            return new ImageIcon(scaledImage);
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error al cargar imagen para: {}", nombreProducto, e);
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        IMAGE_CACHE.put(cacheKey, icon);
                        imagenProductoLbl.setIcon(icon);
                    }
                } catch (Exception e) {
                    logger.error("Error al aplicar imagen cargada", e);
                }
            }
        };

        worker.execute();
    }

    /**
     * Busca la ruta de la imagen en múltiples ubicaciones.
     */
    private String buscarRutaImagen() {
        if (imagenUrl != null && !imagenUrl.trim().isEmpty()) {
            java.net.URL location = getClass().getResource(imagenUrl);
            if (location != null) {
                return imagenUrl;
            }
        }

        String rutaPorNombre = obtenerRutaImagenPorNombre(nombreProducto);
        java.net.URL location = getClass().getResource(rutaPorNombre);
        if (location != null) {
            return rutaPorNombre;
        }

        return "/img/productos/default.png";
    }

    /**
     * Genera la ruta de imagen basándose en el nombre del producto.
     */
    private String obtenerRutaImagenPorNombre(String nombreProducto) {
        if (nombreProducto == null || nombreProducto.trim().isEmpty()) {
            return "/img/productos/default.png";
        }

        String nombreNormalizado = nombreProducto.toLowerCase()
            .replaceAll("\\s+", "-")
            .replaceAll("[^a-z0-9-]", "");

        return "/img/productos/" + nombreNormalizado + ".png";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        g2d.dispose();
    }
}
