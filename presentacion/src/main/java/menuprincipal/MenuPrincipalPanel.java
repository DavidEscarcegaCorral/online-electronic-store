package menuprincipal;

import compartido.PanelBase;
import compartido.cards.ProductoCard;
import compartido.cards.ProductoPedidoCard;
import compartido.estilos.FontUtil;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalPanel extends PanelBase {
    private static String titulo = "Menu Principal";
    private JLabel tituloLbl;
    private JPanel masVendidosPanel;
    private JPanel ofertasPanel;

    public MenuPrincipalPanel() {
        super();
        tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(FontUtil.loadFont(28, "Inter_SemiBold"));
        tituloLbl.setForeground(Color.white);

        masVendidosPanel = new JPanel();
        masVendidosPanel.setOpaque(false);

        panelNorte.add(tituloLbl);

        panelCentro.add(masVendidosPanel);

    }


}

