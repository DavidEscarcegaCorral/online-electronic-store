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

        opcionEntregaPanel = new OpcionEntregaPanel();
        totalPanel = new TotalPanel();
        metodoPagoPanel = new metodoPagoPanel();

        IventaFacade = VentaFacade.getInstance();
        sesion = SesionManager.getInstance();
        rellenarTabla(sesion.getCarritoActual().getItems());

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


    }

    void rellenarTabla(List<ItemCarritoDTO> items){
        String[] columns = {"Producto", "Precio unitario", "Cantidad", "Costo total", "Eliminar"};

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for(ItemCarritoDTO item : items){
            Object[] fila = {
                    item.getNombre(),
                    item.getPrecioUnitario(),
                    item.getCantidad(),
                    item.getSubtotal(),
                    new JLabel("X")
            };
            model.addRow(fila);
        }

        tablaResumen=new Tabla(model, new FramePrincipal());
    }

}
