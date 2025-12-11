package venta.pedido;

import compartido.PanelBase;
import compartido.TotalPanel;
import compartido.estilos.Boton;
import compartido.estilos.Estilos;
import compartido.estilos.TituloLabel;
import dto.MetodoPagoDTO;
import fachada.IVentaFacade;

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
        tituloLabel = new TituloLabel("Confirmar detalles");
        totalPanel = new TotalPanel();

        // Crear botón de confirmar pedido
        confirmarPedidoBtn = new Boton(
            "Confirmar Pedido",
            200,
            45,
            16,
            15,
            Color.WHITE,
            Estilos.COLOR_BOTON_MORADO,
            Estilos.COLOR_BOTON_MORADO_HOVER
        );

        inicializarPanelCentral();
        configurarEventos();

        // Panel Norte
        panelNorte.add(tituloLabel);

        // Panel Centro
        panelCentro.setLayout(new FlowLayout());
        panelCentro.add(catalagoPedidoPanel);

        // Panel Oeste
        panelOeste.setLayout(new BoxLayout(panelOeste, BoxLayout.Y_AXIS));
        panelOeste.add(metodoPagoPanel);
        panelOeste.add(Box.createVerticalStrut(20));

        // Centrar el botón
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        btnPanel.add(confirmarPedidoBtn);
        btnPanel.setMaximumSize(new Dimension(300, 60));
        panelOeste.add(btnPanel);

        // Panel Este
        panelEste.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelEste.add(totalPanel);
    }

    private void configurarEventos() {
        confirmarPedidoBtn.addActionListener(e -> {
            try {
                // Validar que hay productos en el carrito
                IVentaFacade ventaFacade = fachada.VentaFacade.getInstance();
                var configuraciones = ventaFacade.obtenerConfiguracionesEnCarrito();

                if (configuraciones == null || configuraciones.isEmpty()) {
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
                    // Crear método de pago (mock - por ahora tarjeta por defecto)
                    MetodoPagoDTO metodoPago = new MetodoPagoDTO();
                    metodoPago.setTipo(MetodoPagoDTO.Tipo.TARJETA);

                    // Confirmar el pedido
                    String pedidoId = ventaFacade.confirmarPedidoConConfiguraciones(metodoPago);

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
            totalPanel.actualizarTotal();
        }
    }

    public double getTotalCarrito() {
        return catalagoPedidoPanel != null ? catalagoPedidoPanel.getTotalGeneral() : 0.0;
    }
}
