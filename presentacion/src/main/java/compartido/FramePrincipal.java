package compartido;


import compartido.estilos.Estilos;
import compartido.estilos.scroll.ScrollPaneCustom;

import javax.swing.*;
import java.awt.*;

public class FramePrincipal extends JFrame {
    private JPanel panelContenedor;
    private BarraNavegacion barraNavegacion;
    private JPanel panelContenido;
    private ScrollPaneCustom scrollCustom;
    private JPanel headerPanel;
    private String usuarioNombre = "Invitado";
    private String usuarioEmail = "";

    public FramePrincipal() {
        setTitle("Electronic store");
        getContentPane().setBackground(Estilos.COLOR_BACKGROUND);
        setSize(1380, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel contenedor
        panelContenedor = new JPanel();
        panelContenedor.setLayout(new BorderLayout());
        panelContenedor.setOpaque(false);
        barraNavegacion = new BarraNavegacion();

        headerPanel = new JPanel(new BorderLayout()) {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int paddingX = 10;
                int paddingY = 12;
                g2.setColor(Color.WHITE);
                g2.setFont(compartido.estilos.FontUtil.loadFont(14, "Inter_SemiBold"));
                g2.drawString(usuarioNombre, paddingX, paddingY + g2.getFontMetrics().getAscent());
                if (usuarioEmail != null && !usuarioEmail.isEmpty()) {
                    g2.setFont(compartido.estilos.FontUtil.loadFont(12, "Inter_Regular"));
                    g2.drawString(usuarioEmail, paddingX, paddingY + g2.getFontMetrics().getAscent() + g2.getFontMetrics().getHeight());
                }

                g2.dispose();
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.add(Box.createRigidArea(new Dimension(260, 0)));
        headerPanel.add(barraNavegacion, BorderLayout.CENTER);

        // ScrollPane
        scrollCustom = new ScrollPaneCustom(panelContenedor);

        // Añadir componentes
        panelContenedor.add(headerPanel, BorderLayout.NORTH);

        add(scrollCustom, BorderLayout.CENTER);
    }

    public BarraNavegacion getBarraNavegacion() {
        return barraNavegacion;
    }

    /**
     * Establece la barra de navegación con los listeners ya configurados.
     * @param barraNavegacion La barra de navegación configurada
     */
    public void setBarraDeNavegacion(BarraNavegacion barraNavegacion) {
        if (this.barraNavegacion != null && headerPanel != null) {
            headerPanel.remove(this.barraNavegacion);
        }
        this.barraNavegacion = barraNavegacion;
        if (headerPanel != null && barraNavegacion != null) {
            headerPanel.add(this.barraNavegacion, BorderLayout.CENTER);
            headerPanel.revalidate();
            headerPanel.repaint();
        }
    }

    /**
     * Actualiza los datos del usuario mostrados en el header.
     * El controlador es responsable de proporcionar estos datos.
     *
     * @param nombre Nombre del usuario a mostrar
     * @param email  Email del usuario a mostrar
     */
    public void actualizarUsuario(String nombre, String email) {
        this.usuarioNombre = nombre != null && !nombre.trim().isEmpty() ? nombre : "Invitado";
        this.usuarioEmail = email != null ? email : "";
        if (headerPanel != null) {
            headerPanel.repaint();
        }
    }

    /**
     * Cambia el panel de contenido principal mostrado en el frame.
     *
     * @param panelContenido El nuevo panel a mostrar
     */
    public void cambiarPanel(JPanel panelContenido) {
        if (this.panelContenido != null) {
            panelContenedor.remove(this.panelContenido);
        }

        this.panelContenido = panelContenido;
        panelContenedor.add(this.panelContenido, BorderLayout.CENTER);
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }
}
