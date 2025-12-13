package armadoPC;

import compartido.cards.TotalCard;
import compartido.estilos.Boton;
import compartido.estilos.FontUtil;
import compartido.estilos.Estilos;
import compartido.estilos.scroll.ScrollPaneCustom;
import dao.ConfiguracionDAO;
import dao.UsuarioDAO;
import entidades.ConfiguracionEntidad;
import entidades.UsuarioEntidad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MenuOpcionesPanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(MenuOpcionesPanel.class);

    private JLabel tituloLbl;
    private TotalCard totalCard;
    private JPanel panelNorte;
    private JPanel panelCentro;
    private JPanel panelSur;
    private ScrollPaneCustom scroll;

    private Boton guardarConfigBtn;
    private Boton agregarAlCarritoBtn;
    private Consumer<Void> onGuardarConfiguracion;
    private Consumer<Void> onAgregarAlCarrito;
    private dto.EnsamblajeDTO ensamblajActual;

    public MenuOpcionesPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(240, 270));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        tituloLbl = new JLabel("Opciones");
        tituloLbl.setForeground(Color.white);
        tituloLbl.setFont(FontUtil.loadFont(32, "Iceland-Regular"));

        totalCard = new TotalCard();

        guardarConfigBtn = new Boton("Guardar", 150, 40, 14, 10, Color.white, Estilos.COLOR_ENFASIS, Estilos.COLOR_ENFASIS_HOOVER);
        guardarConfigBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        agregarAlCarritoBtn = new Boton("Añadir al Carrito", 150, 40, 14, 10, Color.white, Estilos.COLOR_ENFASIS, Estilos.COLOR_ENFASIS_HOOVER);
        agregarAlCarritoBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelNorte = new JPanel();
        panelNorte.setOpaque(false);
        panelNorte.add(tituloLbl);

        panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        scroll = new ScrollPaneCustom(panelCentro);
        scroll.setPreferredSize(new Dimension(220, 480));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panelSur = new JPanel();
        panelSur.setOpaque(false);
        panelSur.setLayout(new BoxLayout(panelSur, BoxLayout.Y_AXIS));
        panelSur.add(Box.createVerticalStrut(10));
        panelSur.add(guardarConfigBtn);
        panelSur.add(Box.createVerticalStrut(10));
        panelSur.add(agregarAlCarritoBtn);
        panelSur.add(Box.createVerticalStrut(10));
        panelSur.add(totalCard);

        guardarConfigBtn.addActionListener(e -> {
            if (!validarRequisitosMinimos(ensamblajActual)) {
                JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor(MenuOpcionesPanel.this),
                    "No es posible guardar la configuración debido a que no se cumplen los requisitos mínimos.\n\n" +
                    "Debe seleccionar los siguientes componentes obligatorios:\n" +
                    "• Procesador\n" +
                    "• Tarjeta Madre\n" +
                    "• Memoria RAM\n" +
                    "• Gabinete\n" +
                    "• Tarjeta de Video\n" +
                    "• Fuente de Poder\n" +
                    "• Disipador",
                    "Configuración Incompleta",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            abrirDialogoGuardarConfiguracion();
        });

        agregarAlCarritoBtn.addActionListener(e -> {
            if (!validarRequisitosMinimos(ensamblajActual)) {
                JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor(MenuOpcionesPanel.this),
                    "No es posible añadir al carrito debido a que no se cumplen los requisitos mínimos.\n\n" +
                    "Debe seleccionar los siguientes componentes obligatorios:\n" +
                    "• Procesador\n" +
                    "• Tarjeta Madre\n" +
                    "• Memoria RAM\n" +
                    "• Gabinete\n" +
                    "• Tarjeta de Video\n" +
                    "• Fuente de Poder\n" +
                    "• Disipador",
                    "Configuración Incompleta",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (onAgregarAlCarrito != null) {
                onAgregarAlCarrito.accept(null);
            }
        });

        add(panelNorte);
        add(scroll);
        add(panelSur);
    }

    public void setOnGuardarConfiguracion(Consumer<Void> callback) {
        this.onGuardarConfiguracion = callback;
    }

    public void setOnAgregarAlCarrito(Consumer<Void> callback) {
        this.onAgregarAlCarrito = callback;
    }

    public Boton getGuardarConfigBtn() {
        return guardarConfigBtn;
    }

    public void updateFrom(dto.EnsamblajeDTO ensamblaje) {
        this.ensamblajActual = ensamblaje;
        if (ensamblaje == null) {
            totalCard.setTotal(0.0);
            guardarConfigBtn.setEnabled(true);
            revalidate();
            repaint();
            return;
        }

        double total = ensamblaje.getPrecioTotal();
        totalCard.setTotal(total);
        guardarConfigBtn.setEnabled(true);

        revalidate();
        repaint();
    }

    private boolean validarRequisitosMinimos(dto.EnsamblajeDTO ensamblaje) {
        if (ensamblaje == null) return false;

        java.util.List<dto.ComponenteDTO> componentes = ensamblaje.obtenerTodosComponentes();
        if (componentes == null || componentes.isEmpty()) return false;

        java.util.Set<String> categoriasPresentes = new java.util.HashSet<>();
        for (dto.ComponenteDTO comp : componentes) {
            categoriasPresentes.add(comp.getCategoria());
        }

        java.util.Set<String> categoriasObligatorias = java.util.Set.of(
            "Procesador",
            "Tarjeta Madre",
            "RAM",
            "Gabinete",
            "Tarjeta de video",
            "Fuente de poder",
            "Disipador"
        );

        return categoriasPresentes.containsAll(categoriasObligatorias);
    }

    public dto.EnsamblajeDTO getEnsamblajActual() {
        return ensamblajActual;
    }

    private void abrirDialogoGuardarConfiguracion() {
        if (ensamblajActual == null) {
            JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                "No hay configuración para guardar.",
                "Configuración vacía",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        NombrearConfiguracionDialog dialog = new NombrearConfiguracionDialog(parentFrame);
        dialog.setEnsamblaje(ensamblajActual);
        dialog.setOnGuardarConfiguracion(nombre -> guardarConfiguracionEnBD(nombre));
        dialog.setVisible(true);
    }

    private void guardarConfiguracionEnBD(String nombre) {
        try {
            String clienteId = obtenerClienteIdDefecto();
            ConfiguracionDAO configuracionDAO = new ConfiguracionDAO();

            List<ConfiguracionEntidad> configsExistentes = configuracionDAO.obtenerPorUsuarioId(clienteId);

            boolean existeConEsteNombre = configsExistentes.stream()
                .anyMatch(c -> c.getNombre() != null && c.getNombre().equalsIgnoreCase(nombre));

            if (existeConEsteNombre) {
                JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor(this),
                    "Ya existe una configuración con el nombre: " + nombre,
                    "Nombre duplicado",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            ConfiguracionEntidad nuevaConfig = new ConfiguracionEntidad();
            nuevaConfig.setNombre(nombre);
            nuevaConfig.setUsuarioId(clienteId);
            nuevaConfig.setPrecioTotal(ensamblajActual.getPrecioTotal());
            nuevaConfig.setComponentes(convertirComponentesAMapas());

            configuracionDAO.guardar(nuevaConfig);

            logger.info("Configuración guardada exitosamente: {}", nombre);

            JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                "Configuración guardada exitosamente con el nombre: " + nombre,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );


        } catch (Exception e) {
            logger.error("Error al guardar configuración", e);
            JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                "Error al guardar la configuración: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private List<Map<String, Object>> convertirComponentesAMapas() {
        List<Map<String, Object>> componentes = new ArrayList<>();
        if (ensamblajActual != null && ensamblajActual.getComponentes() != null) {
            ensamblajActual.getComponentes().forEach((key, comp) -> {
                Map<String, Object> mapa = new HashMap<>();
                mapa.put("id", comp.getId());
                mapa.put("nombre", comp.getNombre());
                mapa.put("precio", comp.getPrecio());
                mapa.put("categoria", comp.getCategoria());
                componentes.add(mapa);
            });
        }
        return componentes;
    }

    private String obtenerClienteIdDefecto() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        UsuarioEntidad usuario = usuarioDAO.obtenerPorEmail("cliente_default@local");
        if (usuario != null && usuario.getId() != null) {
            return usuario.getId().toString();
        }
        throw new IllegalStateException("Usuario por defecto no encontrado");
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
