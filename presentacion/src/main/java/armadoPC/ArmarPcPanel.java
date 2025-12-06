package armadoPC;

import compartido.estilos.Boton;
import compartido.estilos.Estilos;
import compartido.estilos.FontUtil;
import compartido.estilos.TituloLabel;
import compartido.PanelBase;
import controlpresentacion.ControlConfiguracion;
import fachada.ArmadoFacade;
import fachada.IArmadoFacade;

import javax.swing.*;
import java.awt.*;

public class ArmarPcPanel extends PanelBase {
    private static final String titulo = "Armar PC";

    private CategoriasPanel categoriasPanel;
    private MarcasPanel marcasGenericoPanel;
    private MarcaProcesadorPanel marcasPanel;
    private EvaluarConfiguracionPanel evaluarConfiguracionPanel;

    private MenuComponentesPanel menuComponentesPanel;
    private SideMenuResumenPanel sideMenuResumenPanel;

    private JPanel cardsPanel;
    private CardLayout cardLayout;

    private JLabel tituloLbl;
    private TituloLabel subTItuloLabel;

    private Boton continuarBtn;
    private Boton retrocederBtn;

    private String[] listaPasosArmado;
    private int indiceActual = 0;
    private boolean pasoCompletado = false;

    private java.util.Map<String, CatalagoPanel> catalogos;
    private ControlConfiguracion controlConfiguracion;

    public ArmarPcPanel() {
        super();
        iniciarComponentes();

        // Panel Norte
        panelNorte.add(retrocederBtn);
        panelNorte.add(subTItuloLabel);
        panelNorte.add(continuarBtn);

        // Panel Centro
        panelCentro.add(cardsPanel);
    }

    public void iniciarComponentes() {
        tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(FontUtil.loadFont(28, "Inter_SemiBold"));
        tituloLbl.setForeground(Color.white);
        listaPasosArmado = new String[15];

        catalogos = new java.util.HashMap<>();
        controlConfiguracion = ControlConfiguracion.getInstance();

        categoriasPanel = new CategoriasPanel();
        marcasGenericoPanel = new MarcasPanel();
        marcasPanel = new MarcaProcesadorPanel();
        evaluarConfiguracionPanel = new EvaluarConfiguracionPanel();
        menuComponentesPanel = new MenuComponentesPanel();
        sideMenuResumenPanel = new SideMenuResumenPanel();

        continuarBtn = new Boton("→", 55, 40, 22, 20, Color.white, Estilos.COLOR_BACKGROUND,
                Estilos.COLOR_ATRAS_BOTON_HOOVER);
        continuarBtn.setNewFont();
        retrocederBtn = new Boton("←", 55, 40, 22, 20, Color.white, Estilos.COLOR_BACKGROUND,
                Estilos.COLOR_ATRAS_BOTON_HOOVER);
        retrocederBtn.setNewFont();

        cargarMenu();
        cargarCards();

        configurarCallbacksCategorias();
        configurarCallbacksMarcas();
        configurarBotonesNavegacion();
    }

    private void configurarCallbacksCategorias() {
        for (Component c : categoriasPanel.getComponents()) {
            if (c instanceof CategoriaCard) {
                ((CategoriaCard) c).setOnCategoriaSelected(cat -> {
                    controlConfiguracion.iniciarNuevoEnsamblaje();

                    CatalagoPanel catalogo = catalogos.get("Procesador");
                    if (catalogo != null) {
                        catalogo.setOnProductoSelected(id -> handleProductoSeleccionado(id));
                        catalogo.cargarLista("Procesador");
                    }

                    avanzarAPaso(2);
                    pasoCompletado = false;
                    updateButtonState();
                });
            }
        }
    }

    private void configurarCallbacksMarcas() {
        marcasGenericoPanel.setOnMarcaSelected(marca -> {
            controlConfiguracion.seleccionarMarca(marca);

            if (!controlConfiguracion.hayProductosDisponibles()) {
                JOptionPane.showMessageDialog(this,
                    "No hay productos disponibles para " +
                    controlConfiguracion.getCategoriaActual() + " de marca " + marca,
                    "No hay productos disponibles",
                    JOptionPane.INFORMATION_MESSAGE);
                retrocederUnPaso();
                return;
            }

            java.util.List<dto.ComponenteDTO> productos = controlConfiguracion.obtenerProductos();
            String categoria = controlConfiguracion.getCategoriaActual();

            int indiceCatalogo = obtenerIndiceCatalogo(categoria);
            if (indiceCatalogo != -1) {
                CatalagoPanel catalogo = catalogos.get(listaPasosArmado[indiceCatalogo]);
                if (catalogo != null) {
                    catalogo.cargarLista(productos);
                    catalogo.setOnProductoSelected(id -> handleProductoSeleccionado(id));
                    avanzarAPaso(indiceCatalogo);
                }
            }
        });
    }

    private void configurarBotonesNavegacion() {
        continuarBtn.addActionListener(e -> {
            if (!pasoCompletado) {
                return;
            }

            if (controlConfiguracion.getComponenteSeleccionado() != null) {
                java.util.List<String> errores = controlConfiguracion.avanzarConComponenteSeleccionado();

                if (!errores.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        String.join("\n", errores),
                        "Error de compatibilidad",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                sideMenuResumenPanel.updateFrom(controlConfiguracion.getEnsamblajeActual());

                String siguienteCategoria = obtenerSiguienteCategoria();
                if (siguienteCategoria != null) {
                    controlConfiguracion.seleccionarCategoria(siguienteCategoria);
                    avanzarAPaso(1);
                    cargarMarcasParaCategoriaActual();
                } else {
                    avanzarAPaso(14);
                }
            }
        });

        retrocederBtn.addActionListener(e -> {
            if (indiceActual == 0) return;

            if (indiceActual == 2) {
                String categoriaActual = listaPasosArmado[2];
                int indiceCategoriaAnterior = obtenerIndiceCategoriaAnterior(categoriaActual);

                if (indiceCategoriaAnterior >= 2) {
                    controlConfiguracion.cambiarComponente(listaPasosArmado[indiceCategoriaAnterior]);

                    java.util.List<String> errores = controlConfiguracion.revalidarEnsamblaje();
                    if (!errores.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                            "Al cambiar este componente:\n" + String.join("\n", errores),
                            "Advertencia",
                            JOptionPane.WARNING_MESSAGE);
                    }

                    sideMenuResumenPanel.updateFrom(controlConfiguracion.getEnsamblajeActual());
                }
            }
            retrocederUnPaso();
        });
    }

    private void handleProductoSeleccionado(String productoId) {
        try {
            dao.ProductoDAO productoDAO = new dao.ProductoDAO();
            entidades.ProductoEntidad productoEntidad = productoDAO.obtenerPorId(productoId);

            if (productoEntidad != null) {
                dto.ComponenteDTO componenteDTO = new dto.ComponenteDTO();
                componenteDTO.setId(productoEntidad.getId().toString());
                componenteDTO.setNombre(productoEntidad.getNombre());
                componenteDTO.setPrecio(productoEntidad.getPrecio());
                componenteDTO.setCategoria(productoEntidad.getCategoria());
                componenteDTO.setMarca(productoEntidad.getMarca());

                IArmadoFacade armadoFacade = ArmadoFacade.getInstance();
                java.util.List<String> errores = armadoFacade.agregarComponente(componenteDTO);

                if (!errores.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        String.join("\n", errores),
                        "Error de compatibilidad",
                        JOptionPane.ERROR_MESSAGE);
                    pasoCompletado = false;
                } else {
                    sideMenuResumenPanel.updateFrom(armadoFacade.getEnsamblajeActual());
                    pasoCompletado = true;
                }

                updateButtonState();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error al seleccionar producto: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMarcasParaCategoriaActual() {
        java.util.List<String> marcas = controlConfiguracion.obtenerMarcasParaCategoriaActual();

        if (marcas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay marcas disponibles para " + controlConfiguracion.getCategoriaActual(),
                "Sin marcas disponibles",
                JOptionPane.INFORMATION_MESSAGE);
            retrocederUnPaso();
            return;
        }

        marcasGenericoPanel.cargarMarcas(marcas);
        pasoCompletado = false;
        updateButtonState();
    }

    private void avanzarAPaso(int paso) {
        this.indiceActual = paso;
        this.cardLayout.show(cardsPanel, listaPasosArmado[paso]);
        this.subTItuloLabel.setTitulo(listaPasosArmado[paso]);
        this.pasoCompletado = false;
        updateButtonState();
    }

    private void retrocederUnPaso() {
        if (indiceActual > 0) {
            indiceActual--;
            this.cardLayout.show(cardsPanel, listaPasosArmado[indiceActual]);
            this.subTItuloLabel.setTitulo(listaPasosArmado[indiceActual]);
            updateButtonState();
        }
    }

    private int obtenerIndiceCatalogo(String categoria) {
        for (int i = 0; i < listaPasosArmado.length; i++) {
            if (listaPasosArmado[i].equals(categoria)) {
                return i;
            }
        }
        return -1;
    }

    private String obtenerSiguienteCategoria() {
        String[] categorias = {"Procesador", "Tarjeta Madre", "RAM", "Tarjeta de video",
                              "Almacenamiento", "Fuente de poder", "Gabinete"};
        String actual = controlConfiguracion.getCategoriaActual();

        for (int i = 0; i < categorias.length - 1; i++) {
            if (categorias[i].equals(actual)) {
                return categorias[i + 1];
            }
        }
        return null;
    }

    private int obtenerIndiceCategoriaAnterior(String categoriaActual) {
        for (int i = listaPasosArmado.length - 1; i >= 2; i--) {
            if (listaPasosArmado[i].equals(categoriaActual) && i > 2) {
                return i - 1;
            }
        }
        return 2;
    }

    public void cargarCards() {
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setOpaque(false);
        cardsPanel.add(categoriasPanel, listaPasosArmado[0]);
        cardsPanel.add(marcasGenericoPanel, listaPasosArmado[1]);

        for (int i = 2; i < listaPasosArmado.length - 1; i++) {
            final String paso = listaPasosArmado[i];
            CatalagoPanel cp = new CatalagoPanel(paso);
            catalogos.put(paso, cp);
            cardsPanel.add(cp, paso);
        }

        cardsPanel.add(evaluarConfiguracionPanel, listaPasosArmado[14]);

        subTItuloLabel = new TituloLabel(listaPasosArmado[indiceActual]);
        subTItuloLabel.setForeground(Color.white);
    }

    public void cargarMenu() {
        listaPasosArmado[0] = "Tipo de PC";
        listaPasosArmado[1] = "Seleccionar marca";
        listaPasosArmado[2] = "Procesador";
        listaPasosArmado[3] = "Tarjeta Madre";
        listaPasosArmado[4] = "RAM";
        listaPasosArmado[5] = "Tarjeta de video";
        listaPasosArmado[6] = "Almacenamiento";
        listaPasosArmado[7] = "Fuente de poder";
        listaPasosArmado[8] = "Gabinete";
        listaPasosArmado[9] = "Disipador";
        listaPasosArmado[10] = "Ventilador";
        listaPasosArmado[11] = "Monitor";
        listaPasosArmado[12] = "Kit de teclado/raton";
        listaPasosArmado[13] = "Redes e internet";
        listaPasosArmado[14] = "Resumen";
    }

    public void updateButtonState() {
        retrocederBtn.setEnabled(indiceActual > 0);
        boolean enUltimo = indiceActual >= listaPasosArmado.length - 1;
        continuarBtn.setEnabled(!enUltimo && pasoCompletado);
    }

    public void addMenusNavegacion() {
        panelOeste.add(menuComponentesPanel);
        panelEste.add(sideMenuResumenPanel);
    }

    public void removeMenusNavegacion() {
        panelOeste.remove(menuComponentesPanel);
        panelEste.remove(sideMenuResumenPanel);
    }

    @Deprecated
    public void añadirMenusNavegacion() {
        addMenusNavegacion();
    }

    @Deprecated
    public void eliminarMenusNavegacion() {
        removeMenusNavegacion();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.white);

        g2d.dispose();
    }

    public Boton getContinuarBtn() {
        return continuarBtn;
    }

    public Boton getRetrocederBtn() {
        return retrocederBtn;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getCardsPanel() {
        return cardsPanel;
    }

    public String[] getListaPasosArmado() {
        return listaPasosArmado;
    }

    public int getIndiceActual() {
        return indiceActual;
    }

    public void setIndiceActual(int indiceActual) {
        this.indiceActual = indiceActual;
    }

    public TituloLabel getSubTItuloLabel() {
        return subTItuloLabel;
    }

    public MenuComponentesPanel getMenuComponentesPanel() {
        return menuComponentesPanel;
    }
}
