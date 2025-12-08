package armadoPC;

import compartido.estilos.Boton;
import compartido.estilos.Estilos;
import compartido.estilos.FontUtil;
import compartido.estilos.scroll.ScrollPaneCustom;

import javax.swing.*;
import java.awt.*;

public class MenuComponentesPanel extends JPanel {
    private JPanel panelMenu;
    private JPanel panelCentro;
    private JLabel titulo;
    private Boton categoriasBtn;
    private Boton marcaProcesadorBtn;
    private Boton procesadorBtn;
    private Boton tarjetaMadreBtn;
    private Boton memoriaRAMBtn;
    private Boton almacenamientoBtn;
    private Boton unidadSSDBtn;
    private Boton tarjetaDeVideoBtn;
    private Boton fuenteDePoderBtn;
    private Boton gabineteBtn;
    private Boton disipadorBtn;
    private Boton VentiladorBtn;
    private Boton MonitorBtn;
    private Boton kitTecladoRatonBtn;
    private Boton redBtn;
    private Boton resumenConfiguracionBtn;

    public MenuComponentesPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(240, 610));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        iniciarBotones();

        // Panel Menu
        panelMenu = new JPanel();
        panelMenu.setOpaque(false);
        panelMenu.add(titulo);

        // Panel Centro
        panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setPreferredSize(new Dimension(230, 745));
        panelCentro.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));
        panelCentro.add(categoriasBtn);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(marcaProcesadorBtn);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(procesadorBtn);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(tarjetaMadreBtn);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(memoriaRAMBtn);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(almacenamientoBtn);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(unidadSSDBtn);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(tarjetaDeVideoBtn);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(fuenteDePoderBtn);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(gabineteBtn);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(disipadorBtn);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(VentiladorBtn);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(MonitorBtn);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(kitTecladoRatonBtn);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(redBtn);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(resumenConfiguracionBtn);

        add(panelMenu);

        ScrollPaneCustom scrollPane = new ScrollPaneCustom(panelCentro);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(230, 610));
        add(scrollPane);
    }

    public void iniciarBotones() {
        titulo = new JLabel("Menú");
        titulo.setFont(FontUtil.loadFont(32, "Iceland-Regular"));
        titulo.setForeground(Color.white);

        categoriasBtn = new Boton("Categorías", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        categoriasBtn.setNewFont();

        marcaProcesadorBtn = new Boton("Marca Procesador", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        marcaProcesadorBtn.setNewFont();

        procesadorBtn = new Boton("Procesador", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        procesadorBtn.setNewFont();

        tarjetaMadreBtn = new Boton("Tarjeta Madre", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        tarjetaMadreBtn.setNewFont();

        memoriaRAMBtn = new Boton("Memoria RAM", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        memoriaRAMBtn.setNewFont();

        almacenamientoBtn = new Boton("Almacenamiento", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        almacenamientoBtn.setNewFont();

        unidadSSDBtn = new Boton("Unidad SSD", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        unidadSSDBtn.setNewFont();

        tarjetaDeVideoBtn = new Boton("Tarjeta de video", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        tarjetaDeVideoBtn.setNewFont();

        fuenteDePoderBtn = new Boton("Fuente de poder", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        fuenteDePoderBtn.setNewFont();

        gabineteBtn = new Boton("Gabinete", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        gabineteBtn.setNewFont();

        disipadorBtn = new Boton("Disipador", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        disipadorBtn.setNewFont();

        VentiladorBtn = new Boton("Ventiladores", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        VentiladorBtn.setNewFont();

        MonitorBtn = new Boton("Monitor", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        MonitorBtn.setNewFont();

        kitTecladoRatonBtn = new Boton("Kit de teclado y mouse", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        kitTecladoRatonBtn.setNewFont();

        redBtn = new Boton("Redes e internet", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        redBtn.setNewFont();

        resumenConfiguracionBtn = new Boton("Resumen Configuración", 200, 30, 16, 12, Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
        resumenConfiguracionBtn.setNewFont();

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

    public Boton getProcesadorBtn() {
        return procesadorBtn;
    }

    public Boton getTarjetaMadreBtn() {
        return tarjetaMadreBtn;
    }

    public Boton getCategoriasBtn() {
        return categoriasBtn;
    }

    public Boton getMarcaProcesadorBtn() {
        return marcaProcesadorBtn;
    }

    public Boton getMemoriaRAMBtn() {
        return memoriaRAMBtn;
    }

    public Boton getAlmacenamientoBtn() {
        return almacenamientoBtn;
    }

    public Boton getUnidadSSDBtn() {
        return unidadSSDBtn;
    }

    public Boton getTarjetaDeVideoBtn() {
        return tarjetaDeVideoBtn;
    }

    public Boton getFuenteDePoderBtn() {
        return fuenteDePoderBtn;
    }

    public Boton getGabineteBtn() {
        return gabineteBtn;
    }

    public Boton getDisipadorBtn() {
        return disipadorBtn;
    }

    public Boton getVentiladorBtn() {
        return VentiladorBtn;
    }

    public Boton getMonitorBtn() {
        return MonitorBtn;
    }

    public Boton getKitTecladoRatonBtn() {
        return kitTecladoRatonBtn;
    }

    public Boton getRedBtn() {
        return redBtn;
    }

    public Boton getResumenConfiguracionBtn() {
        return resumenConfiguracionBtn;
    }

}
