package venta.carrito;

import compartido.FramePrincipal;
import compartido.estilos.Estilos;
import compartido.estilos.scroll.ScrollPaneCustom;
import compartido.estilos.tabla.Tabla;
import dto.ItemCarritoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vista Pasiva (Dumb View) para mostrar items del carrito en una tabla.
 *
 * ✅ ARQUITECTURA CORRECTA:
 * - NO tiene acceso directo a DAOs (violación de capas eliminada)
 * - NO tiene lógica de negocio (cálculos, validaciones)
 * - Recibe datos como DTOs desde el panel padre
 * - Solo se encarga de pintar y formatear visualmente
 * - Gestión de errores con logging adecuado
 */
public class TablaPanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(TablaPanel.class);

    private DefaultTableModel model;
    private Tabla tablaResumen;
    private ScrollPaneCustom scroll;
    private JPanel panelCentro;

    /**
     * Constructor que inicializa la tabla sin lógica de negocio.
     */
    public TablaPanel(FramePrincipal framePadre) {
        setOpaque(false);

        String[] columns = {"Producto", "Precio unitario", "Cantidad", "Costo total", ""};
        model = new DefaultTableModel(columns, 0);

        tablaResumen = new Tabla(model, framePadre);
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

    /**
     * Actualiza la tabla con items del carrito.
     *
     * FLUJO CORRECTO: Panel Padre → obtiene DTOs → pasa a Vista → Vista pinta
     *
     * @param items Lista de items del carrito como DTOs (productos + configuraciones)
     */
    public void actualizarCarrito(List<ItemCarritoDTO> items) {
        try {
            limpiar();

            if (items == null || items.isEmpty()) {
                logger.info("Carrito vacío, tabla limpia");
                return;
            }

            for (ItemCarritoDTO item : items) {
                agregarItemATabla(item);
            }

            logger.info("Tabla actualizada con {} items", items.size());

            revalidate();
            repaint();

        } catch (Exception e) {
            logger.error("Error al actualizar tabla del carrito", e);
            mostrarMensajeError();
        }
    }

    /**
     * Agrega un item individual a la tabla.
     * Solo formatea y pinta, no calcula ni busca datos.
     */
    private void agregarItemATabla(ItemCarritoDTO item) {
        if (item == null) {
            logger.warn("Intento de agregar item null a la tabla");
            return;
        }

        String nombre = item.getNombre() != null ? item.getNombre() : "Sin nombre";
        double precioUnitario = item.getPrecioUnitario();
        int cantidad = item.getCantidad();
        double precioTotal = item.getSubtotal();

        model.addRow(new Object[]{
            nombre,
            String.format("$%,.2f", precioUnitario),
            String.valueOf(cantidad),
            String.format("$%,.2f", precioTotal),
            "Eliminar"
        });
    }

    /**
     * Muestra un mensaje de error en la tabla cuando falla la carga.
     */
    private void mostrarMensajeError() {
        limpiar();
        model.addRow(new Object[]{
            "Error al cargar items del carrito",
            "",
            "",
            "",
            ""
        });
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
