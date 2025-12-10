package venta.carrito;

import compartido.estilos.Estilos;
import compartido.estilos.scroll.ScrollPaneCustom;
import compartido.estilos.tabla.Tabla;
import compartido.FramePrincipal;
import fachada.IVentaFacade;
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
            IVentaFacade ventaFacade = fachada.VentaFacade.getInstance();
            List<entidades.ConfiguracionEntidad> configuraciones = ventaFacade.obtenerConfiguracionesEnCarrito();

            for (entidades.ConfiguracionEntidad config : configuraciones) {
                String nombreConfig = config.getNombre() != null ? config.getNombre() : "Configuración PC";
                Double precio = config.getPrecioTotal() != null ? config.getPrecioTotal() : 0.0;

                model.addRow(new Object[]{
                    nombreConfig,
                    String.format("$%,.2f", precio),
                    "1",
                    String.format("$%,.2f", precio * 1),
                    "Eliminar"
                });
            }
        } catch (Exception e) {
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
