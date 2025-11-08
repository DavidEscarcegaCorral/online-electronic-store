package presentacion.panels;

import controlPresentacionVista.ControlDeNavegacion;
import estilos.Boton;
import estilos.BotonHighSpecs;
import estilos.Estilos;
import estilos.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BarraNavegacion extends JPanel {
    private JPanel panelSup;
    private JPanel panelInf;

    private BotonHighSpecs boton;
    private JLabel armarPcLbl;
    private JLabel pedidosLbl;
    private JLabel carritoLbl;
    private JLabel cuentaLbl;
    private JLabel equiposLbl;
    private JLabel componentesLbl;
    private JLabel almacenamientoLbl;
    private JLabel redesLbl;
    private JLabel accesoriosLbl;

    public BarraNavegacion() {
        setPreferredSize(new Dimension(900, 160));
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        setOpaque(false);
        Font font = FontUtil.loadFont(28, "Iceland-Regular");
        Font font2 = FontUtil.loadFont(18, "IBMPlexSans-Regular");

        panelSup = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Estilos.COLOR_NAV_SUP);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2d.dispose();
            }
        };
        panelSup.setLayout(new FlowLayout(FlowLayout.CENTER, 70, 0));
        panelSup.setOpaque(false);

        panelInf = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Estilos.COLOR_NAV_INF);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2d.dispose();
            }
        };
        panelInf.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 8));
        panelInf.setPreferredSize(new Dimension(900, 40));
        panelInf.setOpaque(false);

        boton = new BotonHighSpecs();
        armarPcLbl = new JLabel("Armar Pc");
        armarPcLbl.setFont(font);
        armarPcLbl.setForeground(Color.white);
        pedidosLbl = new JLabel("Pedidos");
        pedidosLbl.setFont(font);
        pedidosLbl.setForeground(Color.white);
        carritoLbl = new JLabel("Carrito");
        carritoLbl.setFont(font);
        carritoLbl.setForeground(Color.white);
        cuentaLbl = new JLabel("Cuenta");
        cuentaLbl.setFont(font);
        cuentaLbl.setForeground(Color.white);

        equiposLbl = new JLabel("Equipos");
        equiposLbl.setFont(font2);
        equiposLbl.setForeground(Color.white);
        componentesLbl = new JLabel("Componentes");
        componentesLbl.setFont(font2);
        componentesLbl.setForeground(Color.white);
        almacenamientoLbl = new JLabel("Almacenamiento");
        almacenamientoLbl.setFont(font2);
        almacenamientoLbl.setForeground(Color.white);
        redesLbl = new JLabel("Redes");
        redesLbl.setFont(font2);
        redesLbl.setForeground(Color.white);
        accesoriosLbl = new JLabel("Accesorios");
        accesoriosLbl.setFont(font2);
        accesoriosLbl.setForeground(Color.white);

        Cursor cursorMano = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        armarPcLbl.setCursor(cursorMano);
        pedidosLbl.setCursor(cursorMano);
        carritoLbl.setCursor(cursorMano);
        cuentaLbl.setCursor(cursorMano);

        //Menu superior
        panelSup.add(boton);
        panelSup.add(armarPcLbl);
        panelSup.add(pedidosLbl);
        panelSup.add(carritoLbl);
        panelSup.add(cuentaLbl);

        //Menu inferior
        panelInf.add(equiposLbl);
        panelInf.add(componentesLbl);
        panelInf.add(almacenamientoLbl);
        panelInf.add(redesLbl);
        panelInf.add(accesoriosLbl);

        add(panelSup);
        add(panelInf);

    }

    public BotonHighSpecs getBoton() {
        return boton;
    }

    public JLabel getArmarPcLbl() {
        return armarPcLbl;
    }

    public JLabel getPedidosLbl() {
        return pedidosLbl;
    }

    public JLabel getCarritoLbl() {
        return carritoLbl;
    }

    public JLabel getCuentaLbl() {
        return cuentaLbl;
    }

    public JLabel getEquiposLbl() {
        return equiposLbl;
    }

    public JLabel getComponentesLbl() {
        return componentesLbl;
    }

    public JLabel getAlmacenamientoLbl() {
        return almacenamientoLbl;
    }

    public JLabel getRedesLbl() {
        return redesLbl;
    }

    public JLabel getAccesoriosLbl() {
        return accesoriosLbl;
    }
}
