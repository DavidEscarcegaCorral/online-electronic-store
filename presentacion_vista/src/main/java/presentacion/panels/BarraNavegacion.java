package presentacion.panels;

import controlPresentacionVista.ControlDeNavegacion;
import estilos.Estilos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BarraNavegacion extends JPanel {
    private JPanel panelSup;
    private JPanel panelInf;

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
        setPreferredSize(new Dimension(950, 200));
        setOpaque(false);
        Font font = new Font("Arial", Font.PLAIN, 16);

        panelSup = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Estilos.COLOR_NAV_SUP);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2d.dispose();
            }
        };
        panelSup.setLayout(new FlowLayout(FlowLayout.CENTER, 70, 8));
        panelSup.setMaximumSize(new Dimension(200, 40));
        panelSup.setOpaque(false);

        panelInf = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Estilos.COLOR_NAV_INF);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2d.dispose();
            }
        };
        panelInf.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 8));
        panelInf.setOpaque(false);

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
        equiposLbl.setFont(font);
        equiposLbl.setForeground(Color.white);
        componentesLbl = new JLabel("Componentes");
        componentesLbl.setFont(font);
        componentesLbl.setForeground(Color.white);
        almacenamientoLbl = new JLabel("Almacenamiento");
        almacenamientoLbl.setFont(font);
        almacenamientoLbl.setForeground(Color.white);
        redesLbl = new JLabel("Redes");
        redesLbl.setFont(font);
        redesLbl.setForeground(Color.white);
        accesoriosLbl = new JLabel("Accesorios");
        accesoriosLbl.setFont(font);
        accesoriosLbl.setForeground(Color.white);

        Cursor cursorMano = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        armarPcLbl.setCursor(cursorMano);
        pedidosLbl.setCursor(cursorMano);
        carritoLbl.setCursor(cursorMano);
        cuentaLbl.setCursor(cursorMano);

        //Menu superior
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
