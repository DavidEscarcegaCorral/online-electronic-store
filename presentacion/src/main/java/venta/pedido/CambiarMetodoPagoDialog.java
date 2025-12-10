package venta.pedido;

import compartido.FramePrincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CambiarMetodoPagoDialog extends JDialog {

    public CambiarMetodoPagoDialog(FramePrincipal owner) {

        super(owner, "Metodo de pago", true);

        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton okButton = new JButton("Aceptar");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setVisible(false);
                dispose();
            }
        });

        buttonPanel.add(okButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

}
