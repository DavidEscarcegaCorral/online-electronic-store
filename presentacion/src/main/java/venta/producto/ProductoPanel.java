package venta.producto;

import compartido.PanelBase;
import compartido.cards.ProductoFotosCard;
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
    private DetallesProductoPanel detallesProductoPanel;
    private TituloLabel tituloLabel;
    private ProductoFotosCard productoFotosCard;

    private JPanel p1;
    private JPanel p2;
    private JPanel p3;
    private JPanel p4;

    private JScrollPane scroll;

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
        inicializarPanelOeste();

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
        detallesProductoPanel = new DetallesProductoPanel();

        p1.add(nombreProductoLbl);
        p2.add(precioProductoLbl);
        p3.add(lblCantidad);
        p3.add(spinnerCantidad);
        p3.add(btnAgregarCarrito);
        p4.add(detallesProductoPanel);

        panelCentro.add(p1);
        panelCentro.add(p2);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(p3);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(p4);

    }

    private void inicializarPanelOeste() {
        panelOeste.setPreferredSize(new Dimension(800, 800));
        panelOeste.setBackground(new Color(70, 70, 70));
        panelOeste.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void cargarFoto(String productoId) {
        if (productoFotosCard != null) {
            panelOeste.remove(productoFotosCard);
        }

        productoFotosCard = new ProductoFotosCard(productoId);
        panelOeste.add(productoFotosCard, BorderLayout.CENTER);
        panelOeste.revalidate();
        panelOeste.repaint();
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
            String productoId = obtenerValorProducto(producto, "getId").toString();

            String titulo = categoria + " - " + marca;
            tituloLabel.setText(titulo);

            cargarFoto(productoId);

            nombreProductoLbl.setText(nombre);

            precioProductoLbl.setText("$" + String.format("%.2f", precio));

            spinnerCantidad.setValue(1);

            productoActual = producto;

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
        if (productoFotosCard != null) {
            panelOeste.remove(productoFotosCard);
            productoFotosCard = null;
        }
        nombreProductoLbl.setText("Nombre del Producto");
        precioProductoLbl.setText("$0.00");
        spinnerCantidad.setValue(1);
        productoActual = null;
        panelOeste.revalidate();
        panelOeste.repaint();
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
