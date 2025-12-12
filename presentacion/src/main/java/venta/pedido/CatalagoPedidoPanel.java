package venta.pedido;

import compartido.cards.ProductoPedidoCard;
import compartido.estilos.Estilos;
import entidades.ConfiguracionEntidad;
import controlpresentacion.ControlPresentacionVenta;
import dao.CarritoDAO;
import dao.UsuarioDAO;
import entidades.UsuarioEntidad;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CatalagoPedidoPanel extends JPanel {
    private List<ProductoPedidoCard> productoPedidoCardList;
    private double totalGeneral;

    public CatalagoPedidoPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(900, 650));
        setLayout(new FlowLayout(FlowLayout.LEFT, 25, 25));

        productoPedidoCardList = new ArrayList<>();
        totalGeneral = 0.0;
    }

    private String obtenerClienteIdDefecto() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        UsuarioEntidad usuario = usuarioDAO.obtenerPorEmail("cliente_default@local");
        if (usuario != null && usuario.getId() != null) {
            return usuario.getId().toString();
        }
        throw new IllegalStateException("Usuario por defecto no encontrado");
    }

    public void cargarProductosDelCarrito() {
        removeAll();
        productoPedidoCardList.clear();
        totalGeneral = 0.0;

        try {
            ControlPresentacionVenta controlVenta = ControlPresentacionVenta.getInstance();
            List<ConfiguracionEntidad> configuraciones = controlVenta.obtenerConfiguracionesEnCarrito();

            String clienteId = obtenerClienteIdDefecto();
            entidades.CarritoEntidad carrito = new CarritoDAO().obtenerCarrito(clienteId);
            List<Map<String, Object>> productosIndividuales =
                carrito.getProductos() != null ? carrito.getProductos() : new ArrayList<>();

            boolean tieneConfiguraciones = configuraciones != null && !configuraciones.isEmpty();
            boolean tieneProductos = !productosIndividuales.isEmpty();

            if (!tieneConfiguraciones && !tieneProductos) {
                JLabel mensajeVacio = new JLabel("No hay productos en el carrito");
                mensajeVacio.setForeground(Color.WHITE);
                mensajeVacio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                add(mensajeVacio);
            } else {
                // Cargar configuraciones (componentes individuales de cada configuración)
                if (tieneConfiguraciones) {
                    for (ConfiguracionEntidad config : configuraciones) {
                        if (config.getComponentes() != null && !config.getComponentes().isEmpty()) {
                            for (Map<String, Object> componente : config.getComponentes()) {
                                ProductoPedidoCard card = crearProductoPedidoCard(componente);
                                productoPedidoCardList.add(card);
                                add(card);

                                // Sumar al total
                                Object precioObj = componente.get("precio");
                                if (precioObj instanceof Number) {
                                    totalGeneral += ((Number) precioObj).doubleValue();
                                }
                            }
                        }
                    }
                }

                // Cargar productos individuales
                if (tieneProductos) {
                    dao.ProductoDAO productoDAO = new dao.ProductoDAO();
                    for (Map<String, Object> prodMap : productosIndividuales) {
                        String productoId = (String) prodMap.get("productoId");
                        Integer cantidad = (Integer) prodMap.get("cantidad");

                        if (productoId != null && cantidad != null && cantidad > 0) {
                            entidades.ProductoEntidad producto = productoDAO.obtenerPorId(productoId);
                            if (producto != null) {
                                // Crear un Map compatible con crearProductoPedidoCard
                                Map<String, Object> productoMap = new java.util.HashMap<>();
                                productoMap.put("nombre", producto.getNombre() + " (x" + cantidad + ")");
                                productoMap.put("precio", producto.getPrecio() * cantidad);

                                ProductoPedidoCard card = crearProductoPedidoCard(productoMap);
                                productoPedidoCardList.add(card);
                                add(card);

                                totalGeneral += producto.getPrecio() * cantidad;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            JLabel mensajeError = new JLabel("Error: " + e.getMessage());
            mensajeError.setForeground(Color.RED);
            mensajeError.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            add(mensajeError);
            System.err.println("Error al cargar productos del carrito");
            e.printStackTrace();
        }

        revalidate();
        repaint();
    }

    private ProductoPedidoCard crearProductoPedidoCard(Map<String, Object> componente) {
        String nombre = (String) componente.get("nombre");
        Object precioObj = componente.get("precio");
        Double precio = precioObj instanceof Number
                ? ((Number) precioObj).doubleValue()
                : 0.0;

        ProductoPedidoCard card = new ProductoPedidoCard(
                nombre,
                precio,
                "/img/productos/default.png"
        );

        return card;
    }

    public double getTotalGeneral() {
        return totalGeneral;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Estilos.COLOR_NAV_INF);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        g2d.dispose();
    }
}
