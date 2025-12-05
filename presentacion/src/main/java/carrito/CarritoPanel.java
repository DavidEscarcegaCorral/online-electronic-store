package carrito;

import compartido.TotalPanel;
import compartido.estilos.FontUtil;
import compartido.estilos.scroll.ScrollPaneCustom;
import compartido.estilos.tabla.Tabla;
import compartido.FramePrincipal;
import compartido.PanelBase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CarritoPanel extends PanelBase {
    private static String titulo = "Carrito";
    private JLabel tituloLbl;
    private OpcionEntregaPanel opcionEntregaPanel;
    private TotalPanel totalPanel;
    private metodoPagoPanel metodoPagoPanel;

    private ScrollPaneCustom scroll;
    private DefaultTableModel model;
    private Tabla tablaResumen;

    public CarritoPanel() {
        super();
        tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(FontUtil.loadFont(28, "Inter_SemiBold"));
        tituloLbl.setForeground(Color.white);

        opcionEntregaPanel = new OpcionEntregaPanel();
        totalPanel = new TotalPanel();
        metodoPagoPanel = new metodoPagoPanel();

            String[] columns = {"Producto", "Precio unitario", "Cantidad", "Costo total", "Eliminar"};

        model =  new DefaultTableModel(columns, 0);
        tablaResumen = new Tabla(model, new FramePrincipal());

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

}
