package venta.carrito;

import compartido.estilos.Estilos;
import compartido.estilos.scroll.ScrollPaneCustom;
import compartido.estilos.tabla.Tabla;
import compartido.FramePrincipal;
import controlconfig.IVentaFacade;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TablaPanel extends JPanel {
    private DefaultTableModel model;
    private Tabla tablaResumen;
    private ScrollPaneCustom scroll;

    private JPanel panelCentro;

    public TablaPanel() {
        setOpaque(false);

        String[] columns = {"Producto", "Precio unitario", "Cantidad", "Costo total", ""};
        model = new DefaultTableModel(columns, 0);
        tablaResumen = new Tabla(model, new FramePrincipal());
        panelCentro = new JPanel();

        panelCentro.setOpaque(false);

        tablaResumen.setOpaque(false);
        tablaResumen.setBackground(new Color(0, 0, 0, 0));

        Dimension tablaSize = new Dimension(900, 510);
        tablaResumen.setPreferredScrollableViewportSize(tablaSize);

        scroll = new ScrollPaneCustom(tablaResumen);

        panelCentro.add(scroll);
        add(panelCentro);
    }

    public void actualizarCarrito() {
        model.setRowCount(0);

        try {
            IVentaFacade ventaFacade = controlconfig.VentaFacade.getInstance();

            List<entidades.ConfiguracionEntidad> configuraciones = ventaFacade.obtenerConfiguracionesEnCarrito();
            if (configuraciones != null && !configuraciones.isEmpty()) {
                for (entidades.ConfiguracionEntidad config : configuraciones) {
                    String nombreConfig = config.getNombre() != null ? config.getNombre() : "Configuración PC";
                    Double precio = config.getPrecioTotal() != null ? config.getPrecioTotal() : 0.0;
                    int cantidad = 1;

                    model.addRow(new Object[]{
                        nombreConfig,
                        String.format("$%,.2f", precio),
                        String.valueOf(cantidad),
                        String.format("$%,.2f", precio * cantidad),
                        "Eliminar"
                    });
                }
                System.out.println("Tabla actualizada con " + configuraciones.size() + " configuración(es)");
            }

            entidades.CarritoEntidad carrito = dao.CarritoDAO.getCarritoActual();
            if (carrito.getProductos() != null && !carrito.getProductos().isEmpty()) {
                dao.ProductoDAO productoDAO = new dao.ProductoDAO();
                for (java.util.Map<String, Object> prod : carrito.getProductos()) {
                    String productoId = (String) prod.get("productoId");
                    Integer cantidad = (Integer) prod.get("cantidad");

                    entidades.ProductoEntidad producto = productoDAO.obtenerPorId(productoId);
                    if (producto != null) {
                        String nombreProducto = producto.getNombre();
                        Double precioUnitario = producto.getPrecio();
                        Double precioTotal = precioUnitario * cantidad;

                        model.addRow(new Object[]{
                            nombreProducto,
                            String.format("$%,.2f", precioUnitario),
                            String.valueOf(cantidad),
                            String.format("$%,.2f", precioTotal),
                            "Eliminar"
                        });
                    }
                }
                System.out.println("Tabla actualizada con " + carrito.getProductos().size() + " producto(s)");
            }

            if ((configuraciones == null || configuraciones.isEmpty()) &&
                (carrito.getProductos() == null || carrito.getProductos().isEmpty())) {
                System.out.println("No hay items en el carrito");
            }

        } catch (Exception e) {
            System.err.println("Error al actualizar carrito: " + e.getMessage());
            e.printStackTrace();
        }

        revalidate();
        repaint();
    }

    public void limpiar() {
        model.setRowCount(0);
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public Tabla getTabla() {
        return tablaResumen;
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
