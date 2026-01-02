package venta.carrito;

import compartido.FramePrincipal;
import compartido.estilos.Estilos;
import compartido.estilos.scroll.ScrollPaneCustom;
import compartido.estilos.tabla.Tabla;
import compartido.estilos.Boton;
import dto.ItemCarritoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TablaPanel extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(TablaPanel.class);
    private static final String COL_PRODUCTO = "Producto";
    private static final String COL_PRECIO_UNITARIO = "Precio unitario";
    private static final String COL_CANTIDAD = "Cantidad";
    private static final String COL_COSTO_TOTAL = "Costo total";
    private static final String COL_ELIMINAR = "Eliminar";
    private static final String COL_ID = "Id";

    private DefaultTableModel model;
    private Tabla tablaResumen;
    private ScrollPaneCustom scroll;
    private JPanel panelCentro;

    private BiConsumer<String, Integer> onCambioCantidad;
    private Consumer<String> onEliminarProducto;

    private class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {
        private final JSpinner editorSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        private boolean listenerAttached = false;

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            int current = 1;
            try {
                current = Integer.parseInt(String.valueOf(value));
            } catch (Exception ignored) {
            }

            editorSpinner.setValue(current);

            if (!listenerAttached) {
                editorSpinner.addChangeListener(ev -> {
                    if (onCambioCantidad != null) {
                        int editingRow = tablaResumen.getEditingRow();
                        if (editingRow >= 0) {
                            int modelRow = tablaResumen.convertRowIndexToModel(editingRow);
                            int modelColId = model.getColumnCount() - 1;
                            Object val = model.getValueAt(modelRow, modelColId);
                            if (val != null) {
                                int nuevaCantidad = ((Number) editorSpinner.getValue()).intValue();
                                onCambioCantidad.accept(val.toString(), nuevaCantidad);
                            }
                        }
                    }
                });
                listenerAttached = true;
            }

            return editorSpinner;
        }

        @Override
        public Object getCellEditorValue() {
            return String.valueOf(((Number) editorSpinner.getValue()).intValue());
        }
    }

    private class SpinnerRenderer implements TableCellRenderer {
        private final JSpinner rendererSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            try {
                int v = 1;
                if (value != null) v = Integer.parseInt(String.valueOf(value));
                rendererSpinner.setValue(v);
            } catch (Exception ignored) {
                rendererSpinner.setValue(1);
            }

            // Setear como no editable
            rendererSpinner.setEnabled(false);
            if (isSelected) {
                rendererSpinner.setBackground(table.getSelectionBackground());
            } else {
                rendererSpinner.setBackground(table.getBackground());
            }
            return rendererSpinner;
        }
    }

    private class DeleteRenderer implements TableCellRenderer {
        private final Boton btn;

        public DeleteRenderer() {
            btn = new Boton("Eliminar", 110, 30, 12, 8, Color.WHITE, Estilos.COLOR_VACIAR_CARITO, Estilos.COLOR_VACIAR_CARRITO_HOOVER);
            btn.setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return btn;
        }
    }

    /**
     * Constructor que inicializa la tabla
     */
    public TablaPanel(FramePrincipal framePadre) {
        setOpaque(false);
        String[] columns = {COL_PRODUCTO, COL_PRECIO_UNITARIO, COL_CANTIDAD, COL_COSTO_TOTAL, COL_ELIMINAR, COL_ID};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                String name = getColumnName(column);
                if (COL_CANTIDAD.equals(name)) {
                    int modelColId = getColumnCount() - 1;
                    Object val = getValueAt(row, modelColId);
                    return val != null && !String.valueOf(val).isBlank();
                }
                return "Ver".equals(name);
            }
        };

        tablaResumen = new Tabla(model, framePadre);
        panelCentro = new JPanel();

        panelCentro.setOpaque(false);
        tablaResumen.setOpaque(false);
        tablaResumen.setBackground(new Color(0, 0, 0, 0));

        Dimension tablaSize = new Dimension(900, 510);
        tablaResumen.setPreferredScrollableViewportSize(tablaSize);

        scroll = new ScrollPaneCustom(tablaResumen);

        try {
            var idColumn = tablaResumen.getColumn("Id");
            idColumn.setMinWidth(0);
            idColumn.setMaxWidth(0);
            idColumn.setPreferredWidth(0);
        } catch (IllegalArgumentException ignored) {
        }

        TableCellEditor spinnerEditor = new SpinnerEditor();

        try {
            int col = tablaResumen.getColumnModel().getColumnIndex(COL_CANTIDAD);
            tablaResumen.getColumnModel().getColumn(col).setCellEditor(spinnerEditor);
            tablaResumen.getColumnModel().getColumn(col).setCellRenderer(new SpinnerRenderer());
            int spinnerHeight = new JSpinner().getPreferredSize().height + 6;
            tablaResumen.setRowHeight(Math.max(tablaResumen.getRowHeight(), spinnerHeight));

            int colEliminar = tablaResumen.getColumnModel().getColumnIndex(COL_ELIMINAR);
            tablaResumen.getColumnModel().getColumn(colEliminar).setCellRenderer(new DeleteRenderer());
            tablaResumen.getColumnModel().getColumn(colEliminar).setMinWidth(100);
            tablaResumen.getColumnModel().getColumn(colEliminar).setMaxWidth(140);
        } catch (IllegalArgumentException ignored) {
        }

        tablaResumen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tablaResumen.rowAtPoint(e.getPoint());
                int col = tablaResumen.columnAtPoint(e.getPoint());
                if (row >= 0 && col >= 0) {
                    String colName = tablaResumen.getColumnName(col);
                    if (COL_ELIMINAR.equals(colName)) {
                        int modelColId = model.getColumnCount() - 1;
                        Object val = model.getValueAt(tablaResumen.convertRowIndexToModel(row), modelColId);
                        if (val != null && onEliminarProducto != null) {
                            onEliminarProducto.accept(val.toString());
                        }
                    }
                }
            }
        });

        panelCentro.add(scroll);
        add(panelCentro);
    }

    /**
     * Actualiza la tabla con items del carrito.
     * @param items Lista de items del carrito como dto's productos y configuraciones
     */
    public void actualizarCarrito(List<ItemCarritoDTO> items) {
        try {
            if (tablaResumen != null && tablaResumen.isEditing()) {
                try {
                    TableCellEditor ed = tablaResumen.getCellEditor();
                    if (ed != null) ed.cancelCellEditing();
                } catch (Exception ignored) {
                }
            }

            limpiar();

            if (items == null || items.isEmpty()) {
                logger.info("Carrito vacío, tabla limpia");
                return;
            }

            for (ItemCarritoDTO item : items) {
                agregarItemATabla(item);
            }

            logger.info("Tabla actualizada con {} items", items.size());

            tablaResumen.revalidate();
            tablaResumen.repaint();

        } catch (Exception e) {
            logger.error("Error al actualizar tabla del carrito", e);
            mostrarMensajeError();
        }
    }

    /**
     * Agrega un item individual a la tabla.
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
                COL_ELIMINAR,
                item.getProductoId()
        });
    }

    private void mostrarMensajeError() {
        limpiar();
        model.addRow(new Object[]{
                "Error al cargar items del carrito",
                "",
                "",
                "",
                "",
                ""
        });
    }

    public void limpiar() {
        model.setRowCount(0);
    }

    public void cancelarEdicion() {
        if (tablaResumen != null && tablaResumen.isEditing()) {
            try {
                TableCellEditor ed = tablaResumen.getCellEditor();
                if (ed != null) ed.cancelCellEditing();
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Actualiza la cantidad y subtotal en la fila correspondiente al productId.
     */
    public boolean actualizarCantidadEnFila(String productoId, int nuevaCantidad) {
        if (productoId == null) return false;
        int rows = model.getRowCount();
        int colId = model.findColumn(COL_ID);
        int colCantidad = model.findColumn(COL_CANTIDAD);
        int colPrecio = model.findColumn(COL_PRECIO_UNITARIO);
        int colTotal = model.findColumn(COL_COSTO_TOTAL);

        if (colId == -1 || colCantidad == -1) return false;

        for (int r = 0; r < rows; r++) {
            Object pid = model.getValueAt(r, colId);
            if (pid != null && productoId.equals(String.valueOf(pid))) {
                // Actualizar cantidad
                model.setValueAt(String.valueOf(nuevaCantidad), r, colCantidad);

                // Actualizar subtotal
                try {
                    Object priceObj = model.getValueAt(r, colPrecio);
                    double price = 0.0;
                    if (priceObj != null) {
                        String s = String.valueOf(priceObj).replaceAll("[^0-9\\.,]", "").replace(',', '.');
                        price = Double.parseDouble(s);
                    }
                    double subtotal = price * nuevaCantidad;
                    model.setValueAt(String.format("$%,.2f", subtotal), r, colTotal);
                } catch (Exception ignored) {
                }

                tablaResumen.revalidate();
                tablaResumen.repaint();
                return true;
            }
        }
        return false;
    }

    public void setOnEliminarProducto(Consumer<String> onEliminar) {
        this.onEliminarProducto = onEliminar;
    }

    public void setOnCambioCantidad(BiConsumer<String, Integer> callback) {
        this.onCambioCantidad = callback;
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
