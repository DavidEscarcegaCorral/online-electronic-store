package venta.pedido;

import compartido.PanelBase;
import compartido.TotalPanel;
import compartido.estilos.TituloLabel;

import javax.swing.*;
import java.awt.*;

public class ConfirmarDetallesPedidoPanel extends PanelBase {
    private CatalagoPedidoPanel catalagoPedidoPanel;
    private MetodoPagoPanel metodoPagoPanel;
    private TotalPanel totalPanel;
    private TituloLabel tituloLabel;

    public ConfirmarDetallesPedidoPanel() {
        super();

        catalagoPedidoPanel = new CatalagoPedidoPanel();
        metodoPagoPanel = new MetodoPagoPanel();
        tituloLabel = new TituloLabel("Confirmar detalles");
        totalPanel = new TotalPanel();

        inicializarPanelCentral();

        // Panel Norte
        panelNorte.add(tituloLabel);

        // Panel Centro
        panelCentro.setLayout(new FlowLayout());
        panelCentro.add(catalagoPedidoPanel);

        // Panel Oeste
        panelOeste.add(metodoPagoPanel);

        // Panel Este
        panelEste.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelEste.add(totalPanel);
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
}
