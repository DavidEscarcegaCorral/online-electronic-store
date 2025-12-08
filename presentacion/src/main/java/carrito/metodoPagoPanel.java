package carrito;

import compartido.estilos.Boton;
import compartido.estilos.CustomRadioButton;
import compartido.estilos.Estilos;
import compartido.estilos.FontUtil;

import javax.swing.*;
import java.awt.*;

public class metodoPagoPanel extends JPanel {
    private JPanel panelNorte;
    private JPanel panelCentro;
    private JPanel panelSur;

    private CustomRadioButton rd1;
    private CustomRadioButton rd2;
    private ButtonGroup grupoMetodoPago;

    private Boton realziarPedidoBtn;
    private Boton vaciarCarritoBtn;

    public metodoPagoPanel() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(220, 260));

        panelNorte = new JPanel();
        panelNorte.setOpaque(false);
        panelNorte.setPreferredSize(new Dimension(220, 40));
        panelNorte.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        panelCentro.setPreferredSize(new Dimension(220, 80));

        panelSur = new JPanel();
        panelSur.setOpaque(false);
        panelSur.setPreferredSize(new Dimension(220, 80));
        panelSur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));

        JLabel titulo = new JLabel("Método de pago");
        titulo.setFont(FontUtil.loadFont(16, "Inter_SemiBold"));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelNorte.add(titulo);

        rd1 = new CustomRadioButton("En efectivo en sucursal");
        rd2 = new CustomRadioButton("Tarjeta de creido/debito");

        grupoMetodoPago = new ButtonGroup();
        grupoMetodoPago.add(rd1);
        grupoMetodoPago.add(rd2);

        realziarPedidoBtn = new Boton("Realizar pedido", 145, 30, 14, 10, Color.white, Estilos.COLOR_ENFASIS, Estilos.COLOR_ENFASIS_HOOVER);
        vaciarCarritoBtn = new Boton("Vaciar Carrito", 145, 30, 14, 10, Color.white, Estilos.COLOR_VACIAR_CARITO, Estilos.COLOR_VACIAR_CARRITO_HOOVER);

        panelCentro.add(rd1);
        panelCentro.add(rd2);

        panelSur.add(realziarPedidoBtn);
        panelSur.add(vaciarCarritoBtn);

        add(panelNorte);
        add(panelCentro);
        add(panelSur);

    }

    public Boton getVaciarCarritoBtn() {
        return vaciarCarritoBtn;
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
