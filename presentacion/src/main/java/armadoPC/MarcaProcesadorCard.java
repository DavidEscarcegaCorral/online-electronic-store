package armadoPC;

import compartido.estilos.Estilos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.function.Consumer;

public class MarcaProcesadorCard extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(MarcaProcesadorCard.class);
    private static final int ANCHO_PANEL = 260;
    private static final int ALTO_PANEL = 220;
    private static final int ANCHO_IMAGEN = 220;
    private static final int RADIO_ESQUINAS = 25;
    private static final int GROSOR_BORDE = 4;

    private JLabel imagenMarcaLbl;
    private String nombreMarca;
    private String imagenUrl;
    private Consumer<String> onMarcaSelected;
    private boolean seleccionado = false;

    public MarcaProcesadorCard(String nombreMarca, String imagenUrl) {
        this.nombreMarca = nombreMarca;
        this.imagenUrl = imagenUrl;

        initComponentes();
        setupListeners();
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
        repaint();
    }

    private void initComponentes() {
        setOpaque(false);
        setPreferredSize(new Dimension(ANCHO_PANEL, ALTO_PANEL));
        setMaximumSize(new Dimension(ANCHO_PANEL, ALTO_PANEL));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        imagenMarcaLbl = new JLabel();
        imagenMarcaLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        cargarImagen(this.imagenUrl);

        add(imagenMarcaLbl, gbc);
    }

    private void cargarImagen(String path) {
        try {
            ImageIcon icon = null;
            URL location = null;

            if (path != null && !path.trim().isEmpty()) {
                location = getClass().getResource(path);
                if (location != null) {
                    icon = new ImageIcon(location);
                }
            }

            if (icon == null || icon.getIconWidth() == -1) {
                logger.debug("Imagen de marca no encontrada en: {}. Usando imagen por defecto.", path);
                location = getClass().getResource("/img/productos/default.png");
                if (location != null) {
                    icon = new ImageIcon(location);
                }
            }

            if (icon != null && icon.getIconWidth() != -1) {
                Image originalImage = icon.getImage();
                Image scaledImage = originalImage.getScaledInstance(ANCHO_IMAGEN, -1, Image.SCALE_SMOOTH);
                imagenMarcaLbl.setIcon(new ImageIcon(scaledImage));
                imagenMarcaLbl.setText("");
            } else {
                imagenMarcaLbl.setIcon(null);
            }

        } catch (Exception e) {
            logger.error("Error al cargar la imagen para marca: {}", nombreMarca, e);
            imagenMarcaLbl.setIcon(null);
        }
    }

    private void setupListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onMarcaSelected != null) {
                    onMarcaSelected.accept(nombreMarca);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), RADIO_ESQUINAS, RADIO_ESQUINAS);
        if (seleccionado) {
            g2d.setColor(Estilos.COLOR_ENFASIS);
            g2d.setStroke(new BasicStroke(GROSOR_BORDE));
            g2d.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, RADIO_ESQUINAS, RADIO_ESQUINAS);
        }
        g2d.dispose();
    }
}
