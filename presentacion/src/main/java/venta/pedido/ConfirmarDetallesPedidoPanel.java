package venta.pedido;

import compartido.PanelBase;
import compartido.TotalPanel;
import compartido.estilos.Boton;
import compartido.estilos.Estilos;
import compartido.estilos.TituloLabel;
import dto.MetodoPagoDTO;
import controlpresentacion.ControlPresentacionVenta;

import javax.swing.*;
import java.awt.*;

public class ConfirmarDetallesPedidoPanel extends PanelBase {
    private CatalagoPedidoPanel catalagoPedidoPanel;
    private MetodoPagoPanel metodoPagoPanel;
    private TotalPanel totalPanel;
    private TituloLabel tituloLabel;
    private Boton confirmarPedidoBtn;
    private Runnable onPedidoConfirmado;

    public ConfirmarDetallesPedidoPanel() {
        super();
        catalagoPedidoPanel = new CatalagoPedidoPanel();
        metodoPagoPanel = new MetodoPagoPanel();
        tituloLabel = new TituloLabel("Confirmar y pagar");
        totalPanel = new TotalPanel();

        confirmarPedidoBtn = new Boton(
                "Confirmar Pedido",
                180,
                30,
                14,
                8,
                Color.WHITE,
                Estilos.COLOR_BOTON_MORADO,
                Estilos.COLOR_BOTON_MORADO_HOVER
        );

        inicializarPanelCentral();
        configurarEventos();

        panelNorte.add(tituloLabel);

        panelCentro.setLayout(new FlowLayout());
        panelCentro.add(catalagoPedidoPanel);

        panelOeste.setLayout(new BoxLayout(panelOeste, BoxLayout.Y_AXIS));
        panelOeste.add(metodoPagoPanel);
        panelOeste.add(Box.createVerticalStrut(20));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        btnPanel.add(confirmarPedidoBtn);

        panelEste.setLayout(new BoxLayout(panelEste, BoxLayout.Y_AXIS));
        panelEste.add(Box.createVerticalGlue());
        panelEste.add(totalPanel);
        panelEste.add(Box.createVerticalStrut(20));
        panelEste.add(btnPanel);
        panelEste.add(Box.createVerticalGlue());
    }

    private void configurarEventos() {
        confirmarPedidoBtn.addActionListener(e -> {
            try {
                ControlPresentacionVenta controlVenta = ControlPresentacionVenta.getInstance();

                double total = controlVenta.calcularTotalCarrito();
                boolean tieneItems = total > 0;

                if (!tieneItems) {
                    JOptionPane.showMessageDialog(
                        this,
                        "No hay productos en el carrito para realizar el pedido.",
                        "Carrito Vacío",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // Confirmar acción con el usuario
                int confirmacion = JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de confirmar este pedido?",
                        "Confirmar Pedido",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    // Crear metodo de pago (mock)
                    MetodoPagoDTO metodoPago = new MetodoPagoDTO();
                    metodoPago.setTipo(MetodoPagoDTO.Tipo.TARJETA);

                    // Confirmar el pedido
                    String pedidoId = controlVenta.confirmarPedido(metodoPago);

                    if (pedidoId != null) {
                        JOptionPane.showMessageDialog(
                                this,
                                "¡Pedido confirmado exitosamente!\nID: " + pedidoId,
                                "Pedido Confirmado",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        // Ejecutar callback si existe
                        if (onPedidoConfirmado != null) {
                            onPedidoConfirmado.run();
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "Error al procesar el pedido. Intente nuevamente.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "Error al confirmar el pedido: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    public void setOnPedidoConfirmado(Runnable callback) {
        this.onPedidoConfirmado = callback;
    }

    private void inicializarPanelCentral() {
        panelCentro.setLayout(new BorderLayout());
    }

    public void actualizarContenido() {
        if (catalagoPedidoPanel != null) {
            catalagoPedidoPanel.cargarProductosDelCarrito();
        }
        if (totalPanel != null) {
            double total = getTotalCarrito();
            totalPanel.actualizarTotal(total);
        }
    }

    public double getTotalCarrito() {
        return catalagoPedidoPanel != null ? catalagoPedidoPanel.getTotalGeneral() : 0.0;
    }
}
