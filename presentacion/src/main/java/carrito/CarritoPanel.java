package carrito;

import compartido.TotalPanel;
import compartido.estilos.FontUtil;
import compartido.estilos.scroll.ScrollPaneCustom;
import compartido.estilos.tabla.Tabla;
import compartido.FramePrincipal;
import compartido.PanelBase;
import dto.CarritoDTO;
import dto.ItemCarritoDTO;
import fachada.IVentaFacade;
import fachada.VentaFacade;
import Sesion.SesionManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CarritoPanel extends PanelBase {
    private static String titulo = "Carrito";
    private JLabel tituloLbl;
    private OpcionEntregaPanel opcionEntregaPanel;
    private TotalPanel totalPanel;
    private metodoPagoPanel metodoPagoPanel;

    private CarritoDTO carritoDTO;

    private final IVentaFacade IventaFacade;

    private ScrollPaneCustom scroll;
    private DefaultTableModel model;
    private Tabla tablaResumen;

    private SesionManager sesion;
    public CarritoPanel() {
        super();
        sesion = SesionManager.getInstance();
        tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(FontUtil.loadFont(28, "Inter_SemiBold"));
        tituloLbl.setForeground(Color.white);

        totalPanel = new TotalPanel();
        opcionEntregaPanel = new OpcionEntregaPanel(totalPanel);
        metodoPagoPanel = new metodoPagoPanel();

        CarritoDTO carritoDTO = new CarritoDTO();
        IventaFacade = VentaFacade.getInstance();
        sesion = SesionManager.getInstance();
        sesion.setCarritoActual(carritoDTO);

        rellenarTabla(sesion.getCarritoActual().getItems());
        tablaResumen= new Tabla(model,new FramePrincipal());
        scroll = new ScrollPaneCustom(tablaResumen);
        scroll.setOpaque(false);

        // Panel Norte
        panelNorte.add(tituloLbl);

        // Panel Centro
        panelCentro.add(scroll);

        // Panel Oeste
        panelOeste.add(opcionEntregaPanel);

        // Panel Este
        panelEste.add(totalPanel);

        panelEste.add(metodoPagoPanel);

        actualizarCarrito();
        }


    public void rellenarTabla(List<ItemCarritoDTO> items) {
        String[] columns = {"Producto", "Precio unitario", "Cantidad", "Costo total", "Eliminar"};

        model = new DefaultTableModel(columns, 0);

        for (ItemCarritoDTO item : items) {
            Object[] fila = {
                    item.getNombre(),
                    item.getPrecioUnitario(),
                    item.getCantidad(),
                    item.getSubtotal(),
                    new JLabel("X")
            };
            model.addRow(fila);
        }
    }

    public void actualizarCarrito() {
        model.setRowCount(0);

        try {
            fachada.IVentaFacade ventaFacade = fachada.VentaFacade.getInstance();
            java.util.List<entidades.ConfiguracionEntidad> configuraciones = ventaFacade.obtenerConfiguracionesEnCarrito();

            for (entidades.ConfiguracionEntidad config : configuraciones) {
                String nombreConfig = config.getNombre() != null ? config.getNombre() : "Configuración PC";
                Double precio = config.getPrecioTotal() != null ? config.getPrecioTotal() : 0.0;

                model.addRow(new Object[]{
                    nombreConfig,
                    String.format("$%,.2f", precio),
                    "1",
                    String.format("$%,.2f", precio * 1),
                    "Eliminar"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error al cargar el carrito: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }

        revalidate();
        repaint();
    }

}
