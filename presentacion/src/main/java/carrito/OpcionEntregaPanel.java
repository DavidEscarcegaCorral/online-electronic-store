package carrito;

import compartido.estilos.CustomRadioButton;
import compartido.estilos.Estilos;
import compartido.estilos.FontUtil;
import Sesion.SesionManager;
import dto.TipoEntregaDTO;
import compartido.TotalPanel;
import javax.swing.*;
import java.awt.*;

public class OpcionEntregaPanel extends JPanel {
    private JLabel tituloLbl;
    private JLabel rb1Label;
    private JLabel rb2Label;
    private JPanel panelTitulo;
    private JPanel panelOpciones;
    private CustomRadioButton rb1;
    private CustomRadioButton rb2;
    private ButtonGroup grupo;
    private SesionManager sesion;
    private TotalPanel totalPanel;

    public OpcionEntregaPanel(){
        sesion=SesionManager.getInstance();
        this.totalPanel = new TotalPanel();
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(320,350));

        tituloLbl = new JLabel("Opciones de entrega");
        tituloLbl.setForeground(Color.white);
        tituloLbl.setFont(FontUtil.loadFont(30, "Iceland-Regular"));

        // Panel Titulo
        panelTitulo = new JPanel();
        panelTitulo.setOpaque(false);
        panelTitulo.setPreferredSize(new Dimension(250, 45));

        // Panel de opciones
        panelOpciones = new JPanel();
        panelOpciones.setOpaque(false);
        panelOpciones.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 25));
        panelOpciones.setPreferredSize(new Dimension(250, 500));

        rb1 = new CustomRadioButton("Recoger en sucursal: Gratis");
        rb2 = new CustomRadioButton("Envio estandar: $224.00 mxn");

        rb1.setSelected(true);

        grupo = new ButtonGroup();
        grupo.add(rb1);
        grupo.add(rb2);


        panelTitulo.add(tituloLbl);
        panelOpciones.add(rb1);
        panelOpciones.add(rb2);

        add(panelTitulo);
        add(panelOpciones);

        configurarListeners();
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

    private void configurarListeners(){
        rb1.addActionListener(e -> {
            rb1.setSelected(true);
            rb2.setSelected(false);
            TipoEntregaDTO tipoEntrega = new TipoEntregaDTO(
                    0.00,
                    TipoEntregaDTO.Tipo.RETIRO_TIENDA);
            SesionManager.getInstance().setTipoEntrega(tipoEntrega);

            if(totalPanel != null){
                totalPanel.actualizarTotales();
            }
        });

        rb2.addActionListener(e -> {
            rb2.setSelected(true);
            rb1.setSelected(false);
            sesion.setTipoEntrega(new TipoEntregaDTO(
                    224.00,
                    TipoEntregaDTO.Tipo.ENVIO_ESTANDAR
            ));

            if(totalPanel != null){
                totalPanel.actualizarTotales();
            }
        });
    }
}
