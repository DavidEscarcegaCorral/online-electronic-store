package estilos.tabla;

import frames.FramePrincipal;

import javax.swing.*;
import java.awt.*;

public class ButtonEditor extends DefaultCellEditor {
    private FramePrincipal owner;
    private JButton button;
    private boolean isPushed;
    private int currentRow;
    private int id;

    public ButtonEditor(JCheckBox checkBox, FramePrincipal owner) {
        super(checkBox);
        this.owner = owner;
        button = new JButton("Delete");
        button.setFont(new Font("SansSerif", Font.PLAIN, 12));
        button.setBackground(new Color(60, 63, 83));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());

        button.addActionListener(e -> {
            if (isPushed) {

            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.currentRow = row;
        this.isPushed = true;
        int id = (int) table.getValueAt(row, table.getColumnModel().getColumnIndex("Id"));
        this.id = id;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        isPushed = false;
        return null;
    }
}
