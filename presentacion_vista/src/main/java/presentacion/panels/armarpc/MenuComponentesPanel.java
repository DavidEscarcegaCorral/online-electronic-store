package presentacion.panels.armarpc;

import estilos.Boton;
import estilos.Estilos;
import estilos.FontUtil;

import javax.swing.*;
import java.awt.*;

public class MenuComponentesPanel extends JPanel {
    private JPanel panelMenu;
    private JPanel panelCentro;
    private JLabel titulo;
    private Boton procesadorBtn;
    private Boton tarjetaMadreLbl;
    private Boton memoriaRAMLbl;
    private Boton almacenamientoLbl;
    private Boton unidadSSDLbl;
    private Boton tarjetaDeVideoLbl;
    private Boton fuenteDePoderLbl;
    private Boton disipadorLbl;
    private Boton VentiladorLbl;
    private Boton MonitorLbl;
    private Boton kitTecladoRatonLbl;
    private Boton redLbl;

    public MenuComponentesPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(250, 700));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        iniciarLabels();

        // Panel Menu
        panelMenu = new JPanel();
        panelMenu.setOpaque(false);
        panelMenu.add(titulo);

        // Panel Centro
        panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setPreferredSize(new Dimension(250, 600));
        panelCentro.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 15));
        panelCentro.add(procesadorBtn);
        panelCentro.add(Box.createVerticalStrut(15));
        panelCentro.add(tarjetaMadreLbl);
        panelCentro.add(Box.createVerticalStrut(15));
        panelCentro.add(memoriaRAMLbl);
        panelCentro.add(Box.createVerticalStrut(15));
        panelCentro.add(almacenamientoLbl);
        panelCentro.add(Box.createVerticalStrut(15));
        panelCentro.add(unidadSSDLbl);
        panelCentro.add(Box.createVerticalStrut(15));
        panelCentro.add(tarjetaDeVideoLbl);
        panelCentro.add(Box.createVerticalStrut(15));
        panelCentro.add(fuenteDePoderLbl);
        panelCentro.add(Box.createVerticalStrut(15));
        panelCentro.add(disipadorLbl);
        panelCentro.add(Box.createVerticalStrut(15));
        panelCentro.add(VentiladorLbl);
        panelCentro.add(Box.createVerticalStrut(15));
        panelCentro.add(MonitorLbl);
        panelCentro.add(Box.createVerticalStrut(15));
        panelCentro.add(kitTecladoRatonLbl);
        panelCentro.add(Box.createVerticalStrut(15));
        panelCentro.add(redLbl);

        add(panelMenu);
        add(panelCentro);
    }

    public void iniciarLabels(){
        titulo = new JLabel("Menú");
        titulo.setFont(FontUtil.loadFont(32, "Iceland-Regular"));
        titulo.setForeground(Color.white);

        procesadorBtn = new Boton("Procesador", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        procesadorBtn.setNewFont();

        tarjetaMadreLbl = new Boton("Tarjeta Madre", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        tarjetaMadreLbl.setNewFont();

        memoriaRAMLbl = new Boton("Memoria RAM", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        memoriaRAMLbl.setNewFont();

        almacenamientoLbl = new Boton("Almacenamiento", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        almacenamientoLbl.setNewFont();

        unidadSSDLbl = new Boton("Unidad SSD", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        unidadSSDLbl.setNewFont();

        tarjetaDeVideoLbl = new Boton("Tarjeta de video", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        tarjetaDeVideoLbl.setNewFont();

        fuenteDePoderLbl = new Boton("Fuente de poder", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        fuenteDePoderLbl.setNewFont();

        disipadorLbl = new Boton("Disipador", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        disipadorLbl.setNewFont();

        VentiladorLbl = new Boton("Ventiladores", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        VentiladorLbl.setNewFont();

        MonitorLbl = new Boton("Monitor", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        MonitorLbl.setNewFont();

        kitTecladoRatonLbl = new Boton("Kit de teclado y mouse", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        kitTecladoRatonLbl.setNewFont();

        redLbl = new Boton("Redes e internet", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        redLbl.setNewFont();

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
