package compartido.cards;

import compartido.estilos.Boton;
import compartido.estilos.Estilos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductoFotosCard extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(ProductoFotosCard.class);
    private static final String[] EXTENSIONES = {".jpg", ".png", ".jpeg"};
    private static final String[] CARPETAS_BUSQUEDA = {
        "/img/productos/",
        "/img/productos/procesadores/"
    };

    private String productoId;
    private List<String> rutasImagenes;
    private int indiceActual = 0;

    private JLabel imagenProductoLbl;
    private Boton irDerechaBtn;
    private Boton irIzquierdaBtn;
    private JPanel p1;
    private JPanel p2;

    public ProductoFotosCard(String productoId) {
        this.productoId = productoId;
        this.rutasImagenes = new ArrayList<>();

        buscarImg();
        initComponents();
        añadirComponentes();
        setupListeners();
    }

    private void buscarImg() {
        logger.debug("Buscando imágenes para productoId: {}", productoId);

        if (productoId == null || productoId.trim().isEmpty()) {
            logger.warn("productoId es null o vacío");
            rutasImagenes.add("/img/productos/default.png");
            return;
        }

        String nombreNormalizado = productoId.toLowerCase()
            .replaceAll("\\s+", "-")
            .replaceAll("[^a-z0-9-]", "");

        int numeroImagen = 1;
        boolean encontrada = true;

        while (encontrada) {
            encontrada = false;
            String nombreArchivo = nombreNormalizado + "-" + numeroImagen;

            for (String carpeta : CARPETAS_BUSQUEDA) {
                for (String extension : EXTENSIONES) {
                    String ruta = carpeta + nombreArchivo + extension;
                    URL location = getClass().getResource(ruta);

                    if (location != null) {
                        rutasImagenes.add(ruta);
                        logger.debug("Imagen encontrada: {}", ruta);
                        encontrada = true;
                        break;
                    }
                }

                if (encontrada) break;
            }

            numeroImagen++;
        }

        if (rutasImagenes.isEmpty()) {
            logger.debug("No se encontraron imágenes numeradas. Buscando imagen base...");

            for (String carpeta : CARPETAS_BUSQUEDA) {
                for (String extension : EXTENSIONES) {
                    String ruta = carpeta + nombreNormalizado + extension;
                    URL location = getClass().getResource(ruta);

                    if (location != null) {
                        rutasImagenes.add(ruta);
                        logger.debug("Imagen base encontrada: {}", ruta);
                        break;
                    }
                }

                if (!rutasImagenes.isEmpty()) break;
            }
        }

        if (rutasImagenes.isEmpty()) {
            logger.info("No se encontraron imágenes para productoId: {}. Usando imagen por defecto.", productoId);
            rutasImagenes.add("/img/productos/default.png");
        } else {
            logger.info("Se encontraron {} imagen(es) para productoId: {}", rutasImagenes.size(), productoId);
        }
    }

    private void initComponents() {
        setOpaque(false);
        setPreferredSize(new Dimension(500, 600));
        setMaximumSize(new Dimension(600, 400));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        imagenProductoLbl = new JLabel();
        imagenProductoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagenProductoLbl.setAlignmentY(Component.CENTER_ALIGNMENT);
        cargarImagenActual();

        p1 = new JPanel();
        p1.setOpaque(false);
        GridBagLayout gbl = new GridBagLayout();
        p1.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        p1.add(imagenProductoLbl, gbc);

        p2 = new JPanel();
        p2.setOpaque(false);

        irIzquierdaBtn = new Boton(
                "<",
                50,
                50,
                24,
                15,
                Color.white,
                Estilos.COLOR_NAV_SUP,
                Estilos.COLOR_NAV_SUP_HOOVER
        );

        irDerechaBtn = new Boton(
                ">",
                50,
                50,
                24,
                15,
                Color.white,
                Estilos.COLOR_NAV_SUP,
                Estilos.COLOR_NAV_SUP_HOOVER
        );
    }

    private void cargarImagenActual() {
        if (indiceActual < 0 || indiceActual >= rutasImagenes.size()) {
            logger.warn("Índice de imagen inválido: {}. Usando imagen 0.", indiceActual);
            indiceActual = 0;
        }

        String rutaActual = rutasImagenes.get(indiceActual);
        cargarImagen(rutaActual);
    }

    private void cargarImagen(String path) {
        try {
            if (path == null || path.trim().isEmpty()) {
                path = "/img/productos/default.png";
            }

            URL location = getClass().getResource(path);
            if (location == null) {
                logger.warn("No se pudo cargar imagen desde: {}. Usando default.", path);
                location = getClass().getResource("/img/productos/default.png");
            }

            if (location != null) {
                ImageIcon icon = new ImageIcon(location);
                if (icon.getIconWidth() != -1) {
                    Image originalImage = icon.getImage();
                    Image scaledImage = originalImage.getScaledInstance(300, -1, Image.SCALE_SMOOTH);
                    imagenProductoLbl.setIcon(new ImageIcon(scaledImage));
                    imagenProductoLbl.setText("");
                } else {
                    imagenProductoLbl.setIcon(null);
                }
            } else {
                imagenProductoLbl.setIcon(null);
            }

        } catch (Exception e) {
            logger.error("Error al cargar la imagen: {}", path, e);
            imagenProductoLbl.setIcon(null);
        }
    }

    private void setupListeners() {
        irIzquierdaBtn.addActionListener(e -> navegarIzquierda());
        irDerechaBtn.addActionListener(e -> navegarDerecha());
    }

    private void navegarIzquierda() {
        if (rutasImagenes.size() > 1) {
            indiceActual = (indiceActual - 1 + rutasImagenes.size()) % rutasImagenes.size();
            cargarImagenActual();
            logger.debug("Navegando izquierda. Imagen actual: {}/{}", indiceActual + 1, rutasImagenes.size());
        }
    }

    private void navegarDerecha() {
        if (rutasImagenes.size() > 1) {
            indiceActual = (indiceActual + 1) % rutasImagenes.size();
            cargarImagenActual();
            logger.debug("Navegando derecha. Imagen actual: {}/{}", indiceActual + 1, rutasImagenes.size());
        }
    }

    private void añadirComponentes() {
        p2.add(irIzquierdaBtn);
        p2.add(Box.createHorizontalStrut(20));
        p2.add(irDerechaBtn);

        add(p1);
        add(Box.createVerticalStrut(10));
        add(p2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
        g2d.dispose();
    }

}
