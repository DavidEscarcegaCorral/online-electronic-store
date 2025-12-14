package compartido;

import compartido.estilos.BotonHighSpecs;
import compartido.estilos.Estilos;
import compartido.estilos.FontUtil;
import compartido.estilos.Boton;
import javax.swing.*;
import java.awt.*;

public class BarraNavegacion extends JPanel {
    private JPanel panelSup;
    private JPanel panelInf;

    private BotonHighSpecs boton;
    private Boton armarPcBtn;
    private Boton pedidosBtn;
    private Boton carritoBtn;
    private Boton cuentaBtn;
    private Boton equiposBtn;
    private Boton componentesBtn;
    private Boton almacenamientoBtn;
    private Boton redesBtn;
    private Boton accesoriosBtn;

    public BarraNavegacion() {
        setPreferredSize(new Dimension(900, 160));
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        setOpaque(false);
        Font btnSupLbl = FontUtil.loadFont(28, "Iceland-Regular");
        Font btnInfLbl = FontUtil.loadFont(18, "Inter_Light");

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
        panelSup.setLayout(new FlowLayout(FlowLayout.CENTER, 35, 0));
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
        panelInf.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 8));
        panelInf.setPreferredSize(new Dimension(900, 45));
        panelInf.setOpaque(false);

        boton = new BotonHighSpecs();

        armarPcBtn = new Boton("Armar Pc", 140, 45, 20, 10, Color.WHITE, Estilos.COLOR_NAV_SUP, Estilos.COLOR_NAV_SUP_HOOVER);
        pedidosBtn = new Boton("Pedidos", 140, 45, 20, 10, Color.WHITE, Estilos.COLOR_NAV_SUP, Estilos.COLOR_NAV_SUP_HOOVER);
        carritoBtn = new Boton("Carrito", 140, 45, 20, 10, Color.WHITE, Estilos.COLOR_NAV_SUP, Estilos.COLOR_NAV_SUP_HOOVER);
        cuentaBtn = new Boton("Cuenta", 140, 45, 20, 10, Color.WHITE, Estilos.COLOR_NAV_SUP, Estilos.COLOR_NAV_SUP_HOOVER);

        equiposBtn = new Boton("Equipos", 160, 30, 16, 10, Color.WHITE, Estilos.COLOR_NAV_INF,  Estilos.COLOR_NAV_INF_HOOVER);
        componentesBtn = new Boton("Componentes", 160, 30, 16, 10, Color.WHITE, Estilos.COLOR_NAV_INF,  Estilos.COLOR_NAV_INF_HOOVER);
        almacenamientoBtn = new Boton("Almacenamiento", 180, 30, 16, 10, Color.WHITE, Estilos.COLOR_NAV_INF,  Estilos.COLOR_NAV_INF_HOOVER);
        redesBtn = new Boton("Redes", 120, 30, 16, 10, Color.WHITE, Estilos.COLOR_NAV_INF,  Estilos.COLOR_NAV_INF_HOOVER);
        accesoriosBtn = new Boton("Accesorios", 140, 30, 16, 10, Color.WHITE, Estilos.COLOR_NAV_INF,  Estilos.COLOR_NAV_INF_HOOVER);

        armarPcBtn.setFont(btnSupLbl);
        pedidosBtn.setFont(btnSupLbl);
        carritoBtn.setFont(btnSupLbl);
        cuentaBtn.setFont(btnSupLbl);

        equiposBtn.setFont(btnInfLbl);
        componentesBtn.setFont(btnInfLbl);
        almacenamientoBtn.setFont(btnInfLbl);
        redesBtn.setFont(btnInfLbl);
        accesoriosBtn.setFont(btnInfLbl);

        //Menu superior: usar botones en lugar de labels
        panelSup.add(boton);
        panelSup.add(armarPcBtn);
        panelSup.add(pedidosBtn);
        panelSup.add(carritoBtn);
        panelSup.add(cuentaBtn);

        //Menu inferior: ahora con botones
        panelInf.add(equiposBtn);
        panelInf.add(componentesBtn);
        panelInf.add(almacenamientoBtn);
        panelInf.add(redesBtn);
        panelInf.add(accesoriosBtn);

        add(panelSup);
        add(panelInf);

    }

    public BotonHighSpecs getBoton() {
        return boton;
    }

    public Boton getArmarPcBtn() {
        return armarPcBtn;
    }

    public Boton getCarritoBtn() {
        return carritoBtn;
    }

}
