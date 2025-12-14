package compartido.cards;

import compartido.estilos.Boton;
import compartido.estilos.Estilos;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.function.Consumer;

/**
 * Tarjeta visual de producto con dos modos de operación:
 * - ARMADO: Usado en el catálogo de armado de PC (botón selecciona componente)
 * - CATALOGO: Usado en otras secciones (botón abre página de producto)
 */
public class ProductoCard extends JPanel {

    public enum Modo {
        ARMADO,
        CATALOGO
    }

    private JLabel imagenProductoLbl;
    private JLabel nombreProductoLbl;
    private JLabel precioProductoLbl;
    private JLabel productoLinkLbl;
    private Boton productoLinkBtn;
    private String id;
    private String imagenUrl;
    private String nombreProducto;
    private double precioProducto;

    private Consumer<String> onSelect;
    private Consumer<String> onVerProducto;
    private boolean seleccionado = false;
    private Modo modo;

    /**
     * Constructor con modo por defecto.
     */
    public ProductoCard(String id, String nombreProducto, double productoPrecio, String imagenUrl){
        this(id, nombreProducto, productoPrecio, imagenUrl, Modo.CATALOGO);
    }

    /**
     * Constructor con modo específico.
     *
     * @param modo ARMADO para catálogo de armado PC, CATALOGO para otras secciones
     */
    public ProductoCard(String id, String nombreProducto, double productoPrecio, String imagenUrl, Modo modo){
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.precioProducto = productoPrecio;
        this.imagenUrl = imagenUrl;
        this.modo = modo;

        initComponents();
        añadirComponentes();
        setupListeners();
    }

    /**
     * Configura el callback cuando se selecciona el producto.
     * Se usa para agregar el componente al resumen del armado.
     */
    public void setOnSelect(Consumer<String> onSelect) {
        this.onSelect = onSelect;
    }

    /**
     * Configura el callback cuando se quiere ver el producto.
     * Se usa para navegar a la página de detalles del producto.
     */
    public void setOnVerProducto(Consumer<String> onVerProducto) {
        this.onVerProducto = onVerProducto;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
        repaint();
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

        nombreProductoLbl = new JLabel("<html><div style='text-align: center;'>" + nombreProducto + "</div></html>");
        nombreProductoLbl.setFont(new Font("Arial", Font.PLAIN, 14));
        nombreProductoLbl.setForeground(Color.BLACK);
        nombreProductoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        nombreProductoLbl.setHorizontalAlignment(SwingConstants.CENTER);

        precioProductoLbl = new JLabel(String.format("$%,.2f", precioProducto));
        precioProductoLbl.setFont(new Font("Arial", Font.BOLD, 18));
        precioProductoLbl.setForeground(Color.BLACK);
        precioProductoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        productoLinkLbl = new JLabel("<html><u>ir al producto</u></html>");
        productoLinkLbl.setFont(new Font("Arial", Font.PLAIN, 12));
        productoLinkLbl.setForeground(new Color(138, 43, 226));
        productoLinkLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        productoLinkLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        String textoBoton = (modo == Modo.ARMADO) ? "Seleccionar" : "Ver Producto";
        int anchoBoton = (modo == Modo.ARMADO) ? 100 : 120;

        productoLinkBtn = new Boton(textoBoton, anchoBoton, 30, 14, 10, Color.white, Estilos.COLOR_ENFASIS, Estilos.COLOR_ENFASIS_HOOVER);
        productoLinkBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void añadirComponentes() {
        add(Box.createVerticalStrut(10));
        add(imagenProductoLbl);
        add(Box.createVerticalStrut(10));
        add(nombreProductoLbl);
        add(Box.createVerticalStrut(5));
        add(precioProductoLbl);

        add(Box.createVerticalGlue());

        add(productoLinkBtn);

        add(Box.createVerticalStrut(15));
    }

    private void setupListeners() {
        productoLinkBtn.addActionListener(e -> {
            if (modo == Modo.ARMADO) {
                if (onSelect != null) {
                    onSelect.accept(id);
                }
            } else {
                if (onVerProducto != null) {
                    onVerProducto.accept(id);
                }
            }
        });

        productoLinkLbl.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (onVerProducto != null) {
                    onVerProducto.accept(id);
                }
            }
        });
    }

    private void cargarImagen(String path){
        try {
            URL location = getClass().getResource(path);
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

        if (seleccionado) {
            g2d.setColor(Estilos.COLOR_ENFASIS);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 25, 25);
        }

        g2d.dispose();
    }
}
