package menuprincipal;

import compartido.PanelBase;
import compartido.cards.ProductoCard;
import compartido.estilos.FontUtil;
import dao.ProductoDAO;
import entidades.ProductoEntidad;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MenuPrincipalPanel extends PanelBase {
    private static final String titulo = "Menu Principal";
    private final JLabel tituloLbl;
    private final JPanel masVendidosPanel;
    private final ProductoDAO productoDAO;
    private final List<ProductoCard> tarjetas;

    public MenuPrincipalPanel() {
        super();
        this.productoDAO = new ProductoDAO();
        this.tarjetas = new ArrayList<>();

        tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(FontUtil.loadFont(28, "Inter_SemiBold"));
        tituloLbl.setForeground(Color.white);

        masVendidosPanel = new JPanel();
        masVendidosPanel.setOpaque(false);
        masVendidosPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        panelNorte.add(tituloLbl);

        crearTarjetasProducto();

        panelCentro.add(new JScrollPane(masVendidosPanel));
    }

    private void crearTarjetasProducto() {
        try {
            List<ProductoEntidad> productos = productoDAO.obtenerTodos();

            if (productos != null && !productos.isEmpty()) {
                ProductoEntidad producto = productos.get(0);

                ProductoCard productoCard = new ProductoCard(
                    producto.getId().toString(),
                    producto.getNombre(),
                    producto.getPrecio(),
                    producto.getImagenUrl()
                );

                tarjetas.add(productoCard);
                masVendidosPanel.add(productoCard);
            }
        } catch (Exception e) {
        }
    }

    public void setOnProductoSeleccionado(Consumer<String> callback) {
        for (ProductoCard tarjeta : tarjetas) {
            tarjeta.setOnSelect(callback);
        }
    }

    public List<ProductoCard> getTarjetas() {
        return tarjetas;
    }
}




