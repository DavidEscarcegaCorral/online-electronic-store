package venta.carrito;

import compartido.TotalPanel;
import compartido.estilos.FontUtil;
import compartido.PanelBase;
import controlpresentacion.ControlPresentacionVenta;

import javax.swing.*;
import java.awt.*;

public class CarritoPanel extends PanelBase {
    private static String titulo = "Carrito";
    private JLabel tituloLbl;
    private OpcionEntregaPanel opcionEntregaPanel;
    private TotalPanel totalPanel;
    private OpcionPagoPanel opcionPagoPanel;
    private TablaPanel tablaPanel;

    private Runnable onRealizarPedido;

    public CarritoPanel() {
        super();
        tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(FontUtil.loadFont(28, "Inter_SemiBold"));
        tituloLbl.setForeground(Color.white);

        opcionEntregaPanel = new OpcionEntregaPanel();
        totalPanel = new TotalPanel();
        opcionPagoPanel = new OpcionPagoPanel();
        tablaPanel = new TablaPanel();

        // Panel Norte
        panelNorte.add(tituloLbl);

        // Panel Centro
        panelCentro.add(tablaPanel);

        // Panel Oeste
        panelOeste.add(opcionEntregaPanel);

        // Panel Este
        Box esteBox = Box.createVerticalBox();
        esteBox.setOpaque(false);
        esteBox.add(totalPanel);
        esteBox.add(Box.createVerticalStrut(15));
        esteBox.add(opcionPagoPanel);
        panelEste.add(esteBox);

        // Eventos botones
        try {
            var vaciarBtn = opcionPagoPanel.getVaciarCarritoBtn();
            if (vaciarBtn != null) {
                vaciarBtn.addActionListener(e -> {
                    try {
                        ControlPresentacionVenta controlVenta = ControlPresentacionVenta.getInstance();
                        controlVenta.vaciarCarrito();

                        tablaPanel.limpiar();
                        if (totalPanel != null) totalPanel.actualizarTotal();

                        revalidate();
                        repaint();

                        JOptionPane.showMessageDialog(this, "Carrito vaciado correctamente.", "Listo", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error al vaciar el carrito: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }

            var realizarPedidoBtn = opcionPagoPanel.getRealizarPedidoBtn();
            if (realizarPedidoBtn != null) {
                realizarPedidoBtn.addActionListener(e -> {
                    if (onRealizarPedido != null) {
                        onRealizarPedido.run();
                    }
                });
            }
        } catch (Exception ignored) {}

        actualizarCarrito();
    }

    public void setOnRealizarPedido(Runnable onRealizarPedido) {
        this.onRealizarPedido = onRealizarPedido;
    }

    public void actualizarCarrito() {
        tablaPanel.actualizarCarrito();

        try {
            if (totalPanel != null) {
                totalPanel.actualizarTotal();
            }
        } catch (Exception ignored) {}

        revalidate();
        repaint();
    }
}
