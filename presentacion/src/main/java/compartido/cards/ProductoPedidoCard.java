package compartido.cards;

import compartido.estilos.FontUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class ProductoPedidoCard extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(ProductoPedidoCard.class);

    private JLabel imagenProductoLbl;
    private JLabel nombreProductoLbl;
    private JLabel precioProductoLbl;

    private String imagenUrl;
    private String nombreProducto;
    private double precioProducto;

    public ProductoPedidoCard(String nombreProducto, double productoPrecio, String imagenUrl) {
        this.nombreProducto = nombreProducto;
        this.precioProducto = productoPrecio;
        this.imagenUrl = imagenUrl;

        initComponents();
        añadirComponentes();
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
        cargarImagen(this.imagenUrl);

        nombreProductoLbl = new JLabel(nombreProducto);
        nombreProductoLbl.setFont(FontUtil.loadFont(12, "Inter_Light"));
        nombreProductoLbl.setForeground(Color.BLACK);
        nombreProductoLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        precioProductoLbl = new JLabel(String.format("$%,.2f", precioProducto));
        precioProductoLbl.setFont(new Font("Arial", Font.BOLD, 18));
        precioProductoLbl.setForeground(Color.BLACK);
        precioProductoLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
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
        panelDerecho.add(Box.createVerticalStrut(20));
        panelDerecho.add(precioProductoLbl);

        add(panelIzquierdo, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);
    }

    private void cargarImagen(String path) {
        try {
            ImageIcon icon = null;
            java.net.URL location = null;

            // Primero intentar cargar desde la ruta proporcionada
            if (path != null && !path.trim().isEmpty()) {
                location = getClass().getResource(path);
                if (location != null) {
                    icon = new ImageIcon(location);
                }
            }

            // Si no se encontró en la ruta proporcionada, buscar por nombre del producto
            if (icon == null || icon.getIconWidth() == -1) {
                String rutaPorNombre = obtenerRutaImagenPorNombre(nombreProducto);
                location = getClass().getResource(rutaPorNombre);
                if (location != null) {
                    icon = new ImageIcon(location);
                }
            }

            // Si aún no se encontró, usar imagen por defecto
            if (icon == null || icon.getIconWidth() == -1) {
                location = getClass().getResource("/img/productos/default.png");
                if (location != null) {
                    icon = new ImageIcon(location);
                }
            }

            // Si tenemos un icono, escalarlo y mostrarlo
            if (icon != null && icon.getIconWidth() != -1) {
                Image originalImage = icon.getImage();
                Image scaledImage = originalImage.getScaledInstance(100, -1, Image.SCALE_SMOOTH);
                imagenProductoLbl.setIcon(new ImageIcon(scaledImage));
                imagenProductoLbl.setText("");
            } else {
                imagenProductoLbl.setIcon(null);
            }

        } catch (Exception e) {
            logger.error("Error al cargar la imagen para producto: {}", nombreProducto, e);
            imagenProductoLbl.setIcon(null);
        }
    }

    private String obtenerRutaImagenPorNombre(String nombreProducto) {
        if (nombreProducto == null || nombreProducto.trim().isEmpty()) {
            return "/img/productos/default.png";
        }

        String nombreNormalizado = nombreProducto.toLowerCase()
            .replaceAll("\\s+", "-")
            .replaceAll("[^a-z0-9-]", "");

        String[] variaciones = {
            "/img/productos/" + nombreNormalizado + ".png",
            "/img/productos/" + nombreNormalizado + ".jpg",
            "/img/productos/" + nombreNormalizado.substring(0, Math.min(20, nombreNormalizado.length())) + ".png",
            "/img/productos/procesadores/" + nombreNormalizado + ".png"
        };

        for (String ruta : variaciones) {
            java.net.URL location = getClass().getResource(ruta);
            if (location != null) {
                return ruta;
            }
        }

        return "/img/productos/default.png";
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
