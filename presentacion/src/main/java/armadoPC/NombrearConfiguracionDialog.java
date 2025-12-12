package armadoPC;


import compartido.estilos.Boton;
import compartido.estilos.Estilos;
import compartido.estilos.FontUtil;
import compartido.estilos.textfields.TxtFieldPh;
import dto.EnsamblajeDTO;
import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class NombrearConfiguracionDialog extends JDialog {
    private JLabel tituloLbl;
    private TxtFieldPh txtNombreConfiguracion;
    private Boton guardarBtn;

    private JPanel p1;
    private JPanel p2;
    private JPanel p3;

    private Consumer<String> onGuardarConfiguracion;
    private EnsamblajeDTO ensamblaje;

    public NombrearConfiguracionDialog(Frame parent) {
        super(parent, "Guardar Configuración", true);
        setPreferredSize(new Dimension(450, 250));
        setMaximumSize(new Dimension(450, 250));
        setMinimumSize(new Dimension(450, 250));
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
        add(p1);
        add(Box.createVerticalStrut(15));
        add(p2);
        add(Box.createVerticalStrut(15));
        add(p3);
    }

    private void initComponents() {
        p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p1.setOpaque(false);
        p1.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

        tituloLbl = new JLabel("Nombre de la Configuración");
        tituloLbl.setFont(FontUtil.loadFont(16, "IBMPlexSans-Bold"));
        tituloLbl.setForeground(Color.WHITE);
        p1.add(tituloLbl);

        p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p2.setOpaque(false);
        p2.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        txtNombreConfiguracion = new TxtFieldPh("Ingresa el nombre aquí...", 300, 35, 14, 10);
        txtNombreConfiguracion.setFont(FontUtil.loadFont(14, "Inter_Regular"));
        p2.add(txtNombreConfiguracion);

        p3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p3.setOpaque(false);
        p3.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        guardarBtn = new Boton(
            "Guardar",
            150,
            40,
            14,
            15,
            Color.WHITE,
            Estilos.COLOR_BOTON_MORADO,
            Estilos.COLOR_BOTON_MORADO_HOVER
        );
        guardarBtn.addActionListener(e -> guardarConfiguracion());
        p3.add(guardarBtn);
    }

    private void guardarConfiguracion() {
        String nombre = txtNombreConfiguracion.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor ingresa un nombre para la configuración.",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (onGuardarConfiguracion != null) {
            onGuardarConfiguracion.accept(nombre);
        }

        dispose();
    }

    public void setOnGuardarConfiguracion(Consumer<String> callback) {
        this.onGuardarConfiguracion = callback;
    }

    public void setEnsamblaje(EnsamblajeDTO ensamblaje) {
        this.ensamblaje = ensamblaje;
    }


}
