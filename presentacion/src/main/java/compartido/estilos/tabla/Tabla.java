package compartido.estilos.tabla;

import compartido.estilos.Estilos;
import compartido.estilos.FontUtil;
import compartido.FramePrincipal;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class Tabla extends JTable {
    private FramePrincipal owner;

    public Tabla(TableModel model, FramePrincipal owner) {
        super(model);
        this.owner = owner;
        configStyle();

    }

    public void setModel(TableModel model) {
        super.setModel(model);
        alingColumn();
    }

    private void configStyle() {
        //Background, foreground, font, row height
        setOpaque(true);
        this.setForeground(Color.white);
        this.setBackground(Estilos.COLOR_BACKGROUND);
        this.setFont(FontUtil.loadFont(16, "Inter_Regular"));
        this.setRowHeight(60);

        this.setShowGrid(false);
        this.setShowHorizontalLines(false);
        this.setShowVerticalLines(false);
        this.setGridColor(Estilos.COLOR_BACKGROUND);
        setIntercellSpacing(new Dimension(0, 0));
        setBorder(null);

        // Hacer que la selección use el mismo color que el fondo para que no destaque
        this.setSelectionBackground(Estilos.COLOR_BACKGROUND);
        this.setSelectionForeground(this.getForeground());
        this.setFillsViewportHeight(true);

        //Header
        JTableHeader header = this.getTableHeader();
        header.setOpaque(false);
        alingColumn();

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setOpaque(true);
                label.setBackground(Estilos.COLOR_BACKGROUND);
                label.setForeground(Color.WHITE);
                label.setFont(FontUtil.loadFont(18, "Inter_Regular"));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return label;
            }
        });

        header.setReorderingAllowed(false);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        // Forzar mismo fondo/foreground si está seleccionado (evita color distinto al seleccionar)
        if (isCellSelected(row, column)) {
            c.setBackground(getBackground());
            c.setForeground(getForeground());
        } else {
            c.setBackground(getBackground());
            c.setForeground(getForeground());
        }
        return c;
    }

    public void alingColumn() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < this.getColumnCount(); i++) {
            this.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return getColumnName(column).equals("Ver");
    }


    public void addColumnButton() {
        try {
            getColumn("Ver").setCellRenderer(new ButtonRenderer());
            getColumn("Ver").setCellEditor(new ButtonEditor(new JCheckBox(), owner));
        } catch (IllegalArgumentException e) {
            System.out.println("La tabla no tiene columna 'Ver'.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

}
