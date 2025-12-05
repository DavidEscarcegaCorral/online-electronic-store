package compartido.cards;

import compartido.estilos.Boton;
import compartido.estilos.Estilos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class ProductoCard extends JPanel {
    private JLabel imagenProductoLbl;
    private JLabel nombreProductoLbl;
    private JLabel precioProductoLbl;
    private JLabel productoLinkLbl;
    private Boton productoLinkBtn;

    private String id;
    private String nombreProducto;
    private double precioProducto;
    private String imagenUrl;

    // callback para notificar selección
    private Consumer<String> onSelect;

    public ProductoCard(String id, String nombreProducto, double productoPrecio, String imagenUrl){
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.precioProducto = productoPrecio;
        this.imagenUrl = imagenUrl;

        initComponents();
        añadirComponentes();
        setupListeners();

    }

    public void setOnSelect(Consumer<String> onSelect) {
        this.onSelect = onSelect;
    }

    public String getId() {
        return id;
    }

    private void initComponents(){
        setOpaque(false);
        setPreferredSize(new Dimension(200, 310));
        setMaximumSize(new Dimension(200, 310));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        imagenProductoLbl = new JLabel();
        imagenProductoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        cargarImagen(this.imagenUrl);

        // Label del nombre
        nombreProductoLbl = new JLabel("<html><div style='text-align: center;'>" + nombreProducto + "</div></html>");
        nombreProductoLbl.setFont(new Font("Arial", Font.PLAIN, 14));
        nombreProductoLbl.setForeground(Color.BLACK);
        nombreProductoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        nombreProductoLbl.setHorizontalAlignment(SwingConstants.CENTER);

        // Label del precio
        precioProductoLbl = new JLabel(String.format("$%,.2f", precioProducto));
        precioProductoLbl.setFont(new Font("Arial", Font.BOLD, 18));
        precioProductoLbl.setForeground(Color.BLACK);
        precioProductoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        //  Label del enlace al producto
        productoLinkLbl = new JLabel("<html><u>ir al producto</u></html>");
        productoLinkLbl.setFont(new Font("Arial", Font.PLAIN, 12));
        productoLinkLbl.setForeground(new Color(138, 43, 226));
        productoLinkLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        productoLinkLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        productoLinkBtn = new Boton("Ir al producto", 85, 30, 14, 10, Color.white, Estilos.COLOR_ENFASIS,  Estilos.COLOR_ENFASIS_HOOVER);
        productoLinkBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void añadirComponentes() {
        add(Box.createVerticalStrut(10));
        add(imagenProductoLbl);
        add(Box.createVerticalStrut(20));
        add(nombreProductoLbl);
        add(Box.createVerticalStrut(15));
        add(precioProductoLbl);
        add(Box.createVerticalStrut(15));
        add(productoLinkBtn);
        add(Box.createVerticalGlue());
    }

    private void setupListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Tarjeta de producto clickeada: " + nombreProducto);
                if (onSelect != null) {
                    onSelect.accept(id);
                }

            }
        });

        // también permitir seleccionar con el botón "Ir al producto"
        productoLinkBtn.addActionListener(e -> {
            if (onSelect != null) onSelect.accept(id);
        });
    }

    private void cargarImagen(String path){
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
        }catch (Exception e) {
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
