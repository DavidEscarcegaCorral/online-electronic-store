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

        tablaPanel.setOnCambioCantidad((productoId, nuevaCantidad) -> {
            try {
                boolean ok = controlVenta.actualizarCantidadItem(productoId, nuevaCantidad);
                if (!ok) {
                    JOptionPane.showMessageDialog(this, "No hay stock suficiente", "Sin stock", JOptionPane.WARNING_MESSAGE);
                    actualizarCarrito();
                } else {
                    actualizarCarrito();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al actualizar la cantidad: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                actualizarCarrito();
            }
        });

        tablaPanel.setOnEliminarProducto(productoId -> {
            try {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Eliminar este producto del carrito?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controlVenta.removerProductoDelCarrito(productoId);
                    actualizarCarrito();
                    if (totalPanel != null) {
                        double total = controlVenta.calcularTotalCarrito();
                        totalPanel.actualizarTotal(total);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al eliminar el producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

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

        // botones
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
                    // verificar stock de todas las cantidades
                    boolean tieneStock = controlVenta.verificarStockCarrito();
                    if (!tieneStock) {
                        JOptionPane.showMessageDialog(this, "No hay stock suficiente para los items del carrito.", "Sin stock", JOptionPane.WARNING_MESSAGE);
                        actualizarCarrito();
                        return;
                    }

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

    public void cancelarEdicionTabla() {
        try {
            if (tablaPanel != null) {
                tablaPanel.cancelarEdicion();
            }
        } catch (Exception ignored) {}
    }

    /**
     * Actualiza la cantidad mostrada de un producto en la tabla.
     * @param productoId id del producto cuyo valor se esta modificando
     */
    public boolean actualizarCantidadProductoEnVista(String productoId) {
        try {
            if (productoId == null) return false;
            List<ItemCarritoDTO> productos = controlVenta.obtenerProductosDelCarrito();
            if (productos == null) return false;

            for (ItemCarritoDTO item : productos) {
                if (productoId.equals(item.getProductoId())) {
                    if (tablaPanel != null) {
                        boolean updated = tablaPanel.actualizarCantidadEnFila(productoId, item.getCantidad());
                        if (updated && totalPanel != null) {
                            double total = controlVenta.calcularTotalCarrito();
                            totalPanel.actualizarTotal(total);
                        }
                        return updated;
                    }
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error al sincronizar cantidad de producto en vista: " + e.getMessage());
            return false;
        }
    }

    public void actualizarCarrito() {
        try {
            List<ItemCarritoDTO> items = new ArrayList<>();

            List<ItemCarritoDTO> productos = controlVenta.obtenerProductosDelCarrito();
            if (productos != null && !productos.isEmpty()) {
                items.addAll(productos);
            }

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
