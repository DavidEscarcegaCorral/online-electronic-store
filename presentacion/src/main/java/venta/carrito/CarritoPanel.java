package venta.carrito;

import compartido.FramePrincipal;
import compartido.TotalPanel;
import compartido.estilos.FontUtil;
import compartido.PanelBase;
import controlpresentacion.ControlPresentacionVenta;
import dto.ConfiguracionDTO;
import dto.ItemCarritoDTO;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CarritoPanel extends PanelBase {
    private static String titulo = "Carrito";
    private JLabel tituloLbl;
    private OpcionEntregaPanel opcionEntregaPanel;
    private TotalPanel totalPanel;
    private OpcionPagoPanel opcionPagoPanel;
    private TablaPanel tablaPanel;
    private ControlPresentacionVenta controlVenta;

    private Runnable onRealizarPedido;

    public CarritoPanel(FramePrincipal framePadre) {
        super();
        this.controlVenta = ControlPresentacionVenta.getInstance();

        tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(FontUtil.loadFont(28, "Inter_SemiBold"));
        tituloLbl.setForeground(Color.white);

        opcionEntregaPanel = new OpcionEntregaPanel();
        totalPanel = new TotalPanel();
        opcionPagoPanel = new OpcionPagoPanel();

        tablaPanel = new TablaPanel(framePadre);

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
                        controlVenta.vaciarCarrito();
                        actualizarCarrito();
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

    /**
     * Actualiza el carrito obteniendo datos del controlador.
     */
    public void actualizarCarrito() {
        try {
            List<ItemCarritoDTO> items = new ArrayList<>();

            // Obtener productos individuales del carrito
            List<ItemCarritoDTO> productos = controlVenta.obtenerProductosDelCarrito();
            if (productos != null && !productos.isEmpty()) {
                items.addAll(productos);
            }

            // Obtener configuraciones del carrito
            List<ConfiguracionDTO> configuraciones = controlVenta.obtenerConfiguracionesEnCarrito();
            if (configuraciones != null) {
                for (ConfiguracionDTO config : configuraciones) {
                    ItemCarritoDTO item = new ItemCarritoDTO();
                    item.setNombre(config.getNombre() != null ? config.getNombre() : "Configuración PC");
                    item.setPrecioUnitario(config.getPrecioTotal());
                    item.setCantidad(1);
                    items.add(item);
                }
            }

            tablaPanel.actualizarCarrito(items);

            if (totalPanel != null) {
                double total = controlVenta.calcularTotalCarrito();
                totalPanel.actualizarTotal(total);
            }

            revalidate();
            repaint();

        } catch (Exception e) {
            System.err.println("Error al actualizar carrito: " + e.getMessage());
            e.printStackTrace();
            tablaPanel.actualizarCarrito(new ArrayList<>());
            if (totalPanel != null) {
                totalPanel.limpiar();
            }
        }
    }
}
