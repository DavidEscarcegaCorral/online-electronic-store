package presentacion.panels.armarpc;

import estilos.Estilos;
import estilos.FontUtil;

import javax.swing.*;
import java.awt.*;

public class MenuComponentesPanel extends JPanel {
    private JLabel titulo;
    private JLabel procesadorLbl;
    private JLabel tarjetaMadreLbl;
    private JLabel memoriaRAMLbl;
    private JLabel almacenamientoLbl;
    private JLabel unidadSSDLbl;
    private JLabel tarjetaDeVideoLbl;
    private JLabel fuenteDePoderLbl;
    private JLabel disipadorLbl;
    private JLabel VentiladorLbl;
    private JLabel MonitorLbl;
    private JLabel kitTecladoRatonLbl;
    private JLabel redLbl;

    public MenuComponentesPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(250, 700));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 60));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        iniciarLabels();

        add(Box.createVerticalStrut(10));
        add(titulo);
        add(Box.createVerticalStrut(20));
        add(procesadorLbl);
        add(Box.createVerticalStrut(15));
        add(tarjetaMadreLbl);
        add(Box.createVerticalStrut(15));
        add(memoriaRAMLbl);
        add(Box.createVerticalStrut(15));
        add(almacenamientoLbl);
        add(Box.createVerticalStrut(15));
        add(unidadSSDLbl);
    }

    public void iniciarLabels(){
        titulo = new JLabel("Menú");
        titulo.setFont(FontUtil.loadFont(32, "Iceland-Regular"));
        titulo.setForeground(Color.white);

        procesadorLbl = new JLabel("Processador");
        procesadorLbl.setFont(FontUtil.loadFont(14, "Inter_Regular"));
        procesadorLbl.setForeground(Color.white);

        tarjetaMadreLbl = new JLabel("Tarjeta Madre");
        tarjetaMadreLbl.setFont(FontUtil.loadFont(14, "Inter_Regular"));
        tarjetaMadreLbl.setForeground(Color.white);

        memoriaRAMLbl  = new JLabel("Memoria RAM");
        memoriaRAMLbl.setFont(FontUtil.loadFont(14, "Inter_Regular"));
        memoriaRAMLbl.setForeground(Color.white);

        almacenamientoLbl  = new JLabel("Almacenamiento");
        almacenamientoLbl.setFont(FontUtil.loadFont(14, "Inter_Regular"));
        almacenamientoLbl.setForeground(Color.white);

        unidadSSDLbl  = new JLabel("Unidad SSD");
        unidadSSDLbl.setFont(FontUtil.loadFont(14, "Inter_Regular"));
        unidadSSDLbl.setForeground(Color.white);

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
