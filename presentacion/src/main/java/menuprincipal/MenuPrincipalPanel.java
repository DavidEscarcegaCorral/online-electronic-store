package menuprincipal;

import compartido.PanelBase;
import compartido.cards.ProductoCard;
import compartido.estilos.FontUtil;
import controlvista.ControlDeNavegacion;
import dao.ProductoDAO;
import entidades.ProductoEntidad;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalPanel extends PanelBase {
    private static final String titulo = "Menu Principal";
    private final JLabel tituloLbl;
    private final JPanel masVendidosPanel;
    private ControlDeNavegacion controlDeNavegacion;
    private final ProductoDAO productoDAO;

    public MenuPrincipalPanel() {
        super();
        this.productoDAO = new ProductoDAO();

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

    public void setControlDeNavegacion(ControlDeNavegacion controlDeNavegacion) {
        this.controlDeNavegacion = controlDeNavegacion;
    }

    private void crearTarjetasProducto() {
        try {
            ProductoEntidad producto = obtenerPrimerProducto();

            if (producto != null) {
                ProductoCard productoCard = new ProductoCard(
                    producto.getId().toString(),
                    producto.getNombre(),
                    producto.getPrecio(),
                    producto.getImagenUrl()
                );

                productoCard.setOnSelect(productId -> {
                    if (controlDeNavegacion != null) {
                        ProductoEntidad productoSeleccionado = productoDAO.obtenerPorId(productId);
                        if (productoSeleccionado != null) {
                            controlDeNavegacion.mostrarProducto(productoSeleccionado);
                        }
                    }
                });

                masVendidosPanel.add(productoCard);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private ProductoEntidad obtenerPrimerProducto() {
        try {
            java.util.List<ProductoEntidad> productos = productoDAO.obtenerTodos();
            if (productos != null && !productos.isEmpty()) {
                return productos.get(0);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener primer producto: " + e.getMessage());
        }
        return null;
    }
}




