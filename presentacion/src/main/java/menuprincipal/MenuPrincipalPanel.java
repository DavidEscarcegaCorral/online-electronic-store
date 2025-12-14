package menuprincipal;

import compartido.PanelBase;
import compartido.cards.ProductoCard;
import compartido.estilos.FontUtil;
import compartido.estilos.scroll.ScrollPaneCustom;
import dto.ComponenteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Panel principal del menú.
 */
public class MenuPrincipalPanel extends PanelBase {
    private static final Logger logger = LoggerFactory.getLogger(MenuPrincipalPanel.class);
    private static final String titulo = "Menu Principal";

    private final JLabel tituloLbl;
    private final JPanel masVendidosPanel;
    private final List<ProductoCard> tarjetas;
    private Consumer<String> onProductoSeleccionado;

    public MenuPrincipalPanel() {
        super();
        this.tarjetas = new ArrayList<>();

        tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(FontUtil.loadFont(28, "Inter_SemiBold"));
        tituloLbl.setForeground(Color.white);

        masVendidosPanel = new JPanel();
        masVendidosPanel.setOpaque(false);
        masVendidosPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        panelNorte.add(tituloLbl);
        panelCentro.add(new ScrollPaneCustom(masVendidosPanel));
    }

    /**
     * Carga los productos destacados desde el controlador.
     * @param productos Lista de DTOs con los productos a mostrar (la lógica de cuáles
     *                  mostrar está en el controlador, no en la vista)
     */
    public void cargarProductosDestacados(List<ComponenteDTO> productos) {
        try {
            limpiarProductos();

            if (productos == null || productos.isEmpty()) {
                logger.warn("No hay productos destacados para mostrar");
                mostrarMensajeVacio();
                return;
            }

            for (ComponenteDTO producto : productos) {
                try {
                    ProductoCard productoCard = new ProductoCard(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getPrecio(),
                        producto.getImagenUrl()
                    );

                    if (onProductoSeleccionado != null) {
                        productoCard.setOnVerProducto(onProductoSeleccionado);
                    }

                    tarjetas.add(productoCard);
                    masVendidosPanel.add(productoCard);
                } catch (Exception e) {
                    logger.error("Error al crear tarjeta para producto: {}", producto.getNombre(), e);
                }
            }

            masVendidosPanel.revalidate();
            masVendidosPanel.repaint();

            logger.info("Cargados {} productos destacados en el menú principal", tarjetas.size());
        } catch (Exception e) {
            logger.error("Error al cargar productos destacados", e);
            mostrarMensajeError();
        }
    }

    /**
     * Limpia todos los productos del panel.
     */
    public void limpiarProductos() {
        tarjetas.clear();
        masVendidosPanel.removeAll();
        masVendidosPanel.revalidate();
        masVendidosPanel.repaint();
    }

    /**
     * Muestra un mensaje cuando no hay productos disponibles.
     */
    private void mostrarMensajeVacio() {
        JLabel mensajeVacio = new JLabel("No hay productos destacados disponibles");
        mensajeVacio.setFont(FontUtil.loadFont(16, "Inter_Regular"));
        mensajeVacio.setForeground(Color.LIGHT_GRAY);
        masVendidosPanel.add(mensajeVacio);
        masVendidosPanel.revalidate();
        masVendidosPanel.repaint();
    }

    /**
     * Muestra un mensaje de error cuando falla la carga.
     */
    private void mostrarMensajeError() {
        JLabel mensajeError = new JLabel("Error al cargar los productos. Intente nuevamente más tarde.");
        mensajeError.setFont(FontUtil.loadFont(16, "Inter_Regular"));
        mensajeError.setForeground(Color.RED);
        masVendidosPanel.add(mensajeError);
        masVendidosPanel.revalidate();
        masVendidosPanel.repaint();
    }

    /**
     * Configura el callback que se ejecuta cuando se selecciona un producto.
     * Este callback abre ProductoPanel con los detalles del producto.
     *
     * @param onProductoSeleccionado Callback que recibe el ID del producto seleccionado
     */
    public void setOnProductoSeleccionado(Consumer<String> onProductoSeleccionado) {
        this.onProductoSeleccionado = onProductoSeleccionado;
    }

    public List<ProductoCard> getTarjetas() {
        return tarjetas;
    }
}




