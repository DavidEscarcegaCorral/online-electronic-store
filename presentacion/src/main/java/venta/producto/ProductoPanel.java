package venta.producto;

import compartido.PanelBase;
import compartido.estilos.Boton;
import compartido.estilos.Estilos;
import compartido.estilos.FontUtil;
import compartido.estilos.TituloLabel;
import javax.swing.*;
import java.awt.*;

/**
 * Panel para mostrar y comprar un producto individual.
 * Estructura:
 * - Norte: Nombre de categoría del componente
 * - Este: Imagen grande del producto
 * - Centro: BoxLayout con 4 paneles (nombre, precio, cantidad/botón, detalles)
 */
public class ProductoPanel extends PanelBase {
    private DetallesPanel detallesPanel;
    private TituloLabel tituloLabel;

    private JPanel p1;
    private JPanel p2;
    private JPanel p3;
    private JPanel p4;

    private JScrollPane scroll;

    private JLabel fotoPorductoLbl;
    private JLabel nombreProductoLbl;
    private JLabel precioProductoLbl;
    private JSpinner spinnerCantidad;
    private Boton btnAgregarCarrito;
    private Object productoActual;

    public ProductoPanel() {
        super();
        inicializarComponentes();

    }

    private void inicializarComponentes() {
        tituloLabel = new TituloLabel("Categoría - Componente");

        p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p1.setOpaque(false);
        p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p2.setOpaque(false);
        p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        p3.setOpaque(false);
        p4 = new JPanel();
        p4.setOpaque(false);

        panelNorte.add(tituloLabel);
        inicializarPanelCentro();
        inicializarPanelEste();

    }

    private void inicializarPanelEste() {
        JPanel panelFoto = new JPanel(new BorderLayout());
        panelFoto.setBackground(new Color(70, 70, 70));
        panelFoto.setPreferredSize(new Dimension(350, 450));
        panelFoto.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        fotoPorductoLbl = new JLabel();
        fotoPorductoLbl.setHorizontalAlignment(JLabel.CENTER);
        fotoPorductoLbl.setVerticalAlignment(JLabel.CENTER);
        panelFoto.add(fotoPorductoLbl, BorderLayout.CENTER);

        panelEste = panelFoto;
    }

    private void inicializarPanelCentro() {
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        // contenido p1
        nombreProductoLbl = new JLabel("Nombre del Producto");
        nombreProductoLbl.setFont(FontUtil.loadFont(32, "IBMPlexSans-Light"));
        nombreProductoLbl.setForeground(Color.WHITE);

        // contenido p2
        precioProductoLbl = new JLabel("$0.00");
        precioProductoLbl.setFont(FontUtil.loadFont(36, "Inter_SemiBold"));
        precioProductoLbl.setForeground(Color.white);

        // contenido p3
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(FontUtil.loadFont(14, "Inter_SemiBold"));
        lblCantidad.setForeground(Color.WHITE);

        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinnerCantidad.setPreferredSize(new Dimension(70, 35));

        btnAgregarCarrito = new Boton(
                "Agregar al Carrito",
                180,
                40,
                14,
                15,
                Color.WHITE,
                Estilos.COLOR_ENFASIS,
                Estilos.COLOR_ENFASIS_HOOVER
        );

        // contenido p4
        detallesPanel = new DetallesPanel();

        p1.add(nombreProductoLbl);
        p2.add(precioProductoLbl);
        p3.add(lblCantidad);
        p3.add(spinnerCantidad);
        p3.add(btnAgregarCarrito);
        p4.add(detallesPanel);

        panelCentro.add(p1);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(p2);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(p3);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(p4);

    }

    private void cargarFoto(String imagenUrl) {
        try {
            if (imagenUrl != null && !imagenUrl.isEmpty()) {
                java.net.URL url = getClass().getResource(imagenUrl);
                if (url != null) {
                    ImageIcon icon = new ImageIcon(url);
                    Image img = icon.getImage();
                    Image imgEscalada = img.getScaledInstance(330, 330, Image.SCALE_SMOOTH);
                    fotoPorductoLbl.setIcon(new ImageIcon(imgEscalada));
                } else {
                    fotoPorductoLbl.setText("Imagen no disponible");
                    fotoPorductoLbl.setForeground(Color.GRAY);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            fotoPorductoLbl.setText("Error al cargar imagen");
            fotoPorductoLbl.setForeground(Color.RED);
        }
    }

    public void cargarProducto(Object producto) {
        if (producto == null) {
            limpiarUI();
            return;
        }

        this.productoActual = producto;

        try {
            String categoria = (String) obtenerValorProducto(producto, "getCategoria");
            String marca = (String) obtenerValorProducto(producto, "getMarca");
            String nombre = (String) obtenerValorProducto(producto, "getNombre");
            Double precio = (Double) obtenerValorProducto(producto, "getPrecio");
            String imagenUrl = (String) obtenerValorProducto(producto, "getImagenUrl");

            String titulo = categoria + " - " + marca;
            tituloLabel.setText(titulo);

            cargarFoto(imagenUrl);

            nombreProductoLbl.setText(nombre);

            precioProductoLbl.setText("$" + String.format("%.2f", precio));

            spinnerCantidad.setValue(1);

        } catch (Exception e) {
            System.err.println("Error al cargar producto: " + e.getMessage());
            limpiarUI();
        }
    }

    private Object obtenerValorProducto(Object producto, String metodo) throws Exception {
        return producto.getClass().getMethod(metodo).invoke(producto);
    }

    private void limpiarUI() {
        tituloLabel.setText("Categoría - Componente");
        fotoPorductoLbl.setIcon(null);
        fotoPorductoLbl.setText("");
        nombreProductoLbl.setText("Nombre del Producto");
        precioProductoLbl.setText("$0.00");
        spinnerCantidad.setValue(1);
        productoActual = null;
    }

    public Object getProductoActual() {
        return productoActual;
    }

    public int getCantidadSeleccionada() {
        return (Integer) spinnerCantidad.getValue();
    }

    public void limpiarListeners() {
        java.awt.event.ActionListener[] listeners = btnAgregarCarrito.getActionListeners();
        for (java.awt.event.ActionListener listener : listeners) {
            btnAgregarCarrito.removeActionListener(listener);
        }
    }

    public void setOnAgregarAlCarrito(java.awt.event.ActionListener listener) {
        btnAgregarCarrito.addActionListener(listener);
    }
}
