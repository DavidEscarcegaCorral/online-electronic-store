package compartido.cards;

import compartido.estilos.FontUtil;

import javax.swing.*;
import java.awt.*;

public class ProductoPedidoCard extends JPanel {
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
            java.net.URL location = getClass().getResource(path);
            if (location == null) {
                imagenProductoLbl.setText("No Image");
                imagenProductoLbl.setIcon(null);
                return;
            }
            ImageIcon originalIcon = new ImageIcon(location);
            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(100, -1, Image.SCALE_SMOOTH);
            imagenProductoLbl.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen para " + nombreProducto + ": " + path);
            imagenProductoLbl.setText("No Image");
            imagenProductoLbl.setIcon(null);
            e.printStackTrace();
        }
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
