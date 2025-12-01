package compartido.estilos.tabla;

import compartido.estilos.Estilos;
import compartido.estilos.FontUtil;
import compartido.FramePrincipal;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
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
        this.setForeground(Color.BLACK);
        this.setBackground(Estilos.COLOR_BACKGROUND);
        this.setFont(FontUtil.loadFont(16, "IBMPlexSans-Light"));
        this.setRowHeight(60);

        //Grid
        this.setShowHorizontalLines(false);
        this.setShowVerticalLines(false);
        this.setGridColor(Estilos.COLOR_BACKGROUND);
        setIntercellSpacing(new Dimension(10, 10));

        //Selection colors
        this.setSelectionBackground(Color.white);
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
//                label.setFont(FontUtil.loadFont(15, "Inter_18pt-ExtraLight"));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return label;
            }
        });

        header.setReorderingAllowed(false);
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
