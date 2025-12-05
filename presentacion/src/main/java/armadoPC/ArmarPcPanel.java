package armadoPC;

import compartido.estilos.Boton;
import compartido.estilos.Estilos;
import compartido.estilos.FontUtil;
import compartido.estilos.TituloLabel;
import compartido.PanelBase;
import fachada.ArmadoFacade;
import fachada.IArmadoFacade;

import javax.swing.*;
import java.awt.*;

public class ArmarPcPanel extends PanelBase {
    private static String titulo = "Armar PC";

    private CategoriasPanel categoriasPanel;
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
    private String tipoUsoSeleccionado;

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
        // Titulo label
        tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(FontUtil.loadFont(28, "Inter_SemiBold"));
        tituloLbl.setForeground(Color.white);
        listaPasosArmado = new String[15];

        catalogos = new java.util.HashMap<>();

        // Card de categorias
        categoriasPanel = new CategoriasPanel();
        // registrar callbacks para seleccionar categoria
        // las CategoriaCard son estáticas dentro de CategoriasPanel, así que accedemos
        // por reflexión no es ideal;
        // alternativa: asignar consumer desde CategoriasPanel
        categoriasPanel = new CategoriasPanel();

        // Card de Marcas
        marcasPanel = new MarcaProcesadorPanel();

        // Card de Evaluar configuracion
        evaluarConfiguracionPanel = new EvaluarConfiguracionPanel();

        // Menu lateral de los componentes
        menuComponentesPanel = new MenuComponentesPanel();

        // Menu lateral de resumen de configuracion
        sideMenuResumenPanel = new SideMenuResumenPanel();

        continuarBtn = new Boton("→", 55, 40, 22, 20, Color.white, Estilos.COLOR_BACKGROUND,
                Estilos.COLOR_ATRAS_BOTON_HOOVER);
        continuarBtn.setNewFont();
        retrocederBtn = new Boton("←", 55, 40, 22, 20, Color.white, Estilos.COLOR_BACKGROUND,
                Estilos.COLOR_ATRAS_BOTON_HOOVER);
        retrocederBtn.setNewFont();

        cargarMenu();
        cargarCards();

        // registrar callback a las categorias: avanzar si hay stock
        categoriasPanel.getComponents();
        for (Component c : categoriasPanel.getComponents()) {
            if (c instanceof CategoriaCard) {
                ((CategoriaCard) c).setOnCategoriaSelected(cat -> {
                    IArmadoFacade fachada = ArmadoFacade.getInstance();

                    // 1. Verificar Stock Suficiente para el tipo de uso (cat es el tipo de uso
                    // aquí: GAMER, OFFICE, etc.)
                    if (!fachada.verificarStockSuficiente(cat)) {
                        JOptionPane.showMessageDialog(this, "No hay stock suficiente para armar una PC tipo: " + cat);
                        return;
                    }

                    fachada.iniciarNuevoEnsamblaje();

                    // navegar al siguiente paso (Marca del procesador si aplica)
                    // aquí asumimos que el siguiente paso es el índice 1 (Marca del procesador)
                    this.getCardLayout().show(this.getCardsPanel(), listaPasosArmado[1]);
                    this.setIndiceActual(1);
                    this.getSubTItuloLabel().setTitulo(listaPasosArmado[1]);
                    // Al entrar a un nuevo paso, bloquear continuar hasta que seleccione
                    this.continuarBtn.setEnabled(false);
                    this.updateButtonState();
                });
            }
        }

        // registrar callback a las marcas de procesador: filtrar procesadores por marca
        // y mostrar catalogo
        for (Component c : marcasPanel.getComponents()) {
            if (c instanceof MarcaProcesadorCard) {
                ((MarcaProcesadorCard) c).setOnMarcaSelected(marca -> {
                    // mostrar catalogo de procesadores filtrado por marca
                    this.mostrarCatalogoFiltrado("Procesador",
                            dto -> dto.getNombre().toLowerCase().contains(marca.toLowerCase()));
                    // navegar al catalogo de procesador
                    this.getCardLayout().show(this.getCardsPanel(), listaPasosArmado[2]);
                    this.setIndiceActual(2);
                    this.getSubTItuloLabel().setTitulo(listaPasosArmado[2]);
                    // bloquear continuar hasta seleccionar
                    this.continuarBtn.setEnabled(false);
                    this.updateButtonState();
                });
            }
        }

        // configurar callbacks de los catalogos para que al seleccionar id se agregue
        // el componente
        for (String key : catalogos.keySet()) {
            CatalagoPanel cp = catalogos.get(key);
            cp.setOnProductoSelected(id -> handleProductoSeleccionado(id, key));
        }

    }

    public void cargarCards() {
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setOpaque(false);
        cardsPanel.add(categoriasPanel, listaPasosArmado[0]);
        cardsPanel.add(marcasPanel, listaPasosArmado[1]);

        for (int i = 2; i < listaPasosArmado.length - 1; i++) {
            CatalagoPanel cp = new CatalagoPanel(listaPasosArmado[i]);
            catalogos.put(listaPasosArmado[i], cp);
            cardsPanel.add(cp, listaPasosArmado[i]);
        }

        cardsPanel.add(evaluarConfiguracionPanel, listaPasosArmado[14]);

        subTItuloLabel = new TituloLabel(listaPasosArmado[indiceActual]);
        subTItuloLabel.setForeground(Color.white);
    }

    public void mostrarCatalogoFiltrado(String categoria, java.util.function.Predicate<dto.ComponenteDTO> filtro) {
        CatalagoPanel cp = catalogos.get(categoria);
        if (cp == null)
            return;
        IArmadoFacade fachada = ArmadoFacade.getInstance();

        // Usar el nuevo método que filtra por compatibilidad y tipo de uso
        String tipoUso = this.tipoUsoSeleccionado;
        java.util.List<dto.ComponenteDTO> lista = fachada.obtenerComponentesCompatibles(categoria, tipoUso);

        java.util.List<dto.ComponenteDTO> filtrados = new java.util.ArrayList<>();
        for (dto.ComponenteDTO c : lista) {
            if (filtro == null || filtro.test(c))
                filtrados.add(c);
        }
        // limpiar y cargar en el panel (CatalagoPanel no tiene API para pasar datos,
        // usar cargarLista modificada)
        // implementaremos una nueva sobrecarga en CatalagoPanel para aceptar lista de
        // DTOs
        cp.cargarLista(filtrados);
        // al cargar catálogo, asegurarse de que los productos reporten al handler
        cp.setOnProductoSelected(id -> handleProductoSeleccionado(id, categoria));
    }

    // Maneja la selección de un producto (id) en un catálogo: intenta agregar al
    // ensamblaje y actualiza resumen
    private void handleProductoSeleccionado(String componenteId, String categoria) {
        IArmadoFacade fachada = ArmadoFacade.getInstance();

        // Usar el nuevo método que filtra por compatibilidad y tipo de uso
        String tipoUso = this.tipoUsoSeleccionado;
        dto.ComponenteDTO dto = fachada.obtenerComponentesCompatibles(categoria, tipoUso).stream()
                .filter(c -> c.getId().equals(componenteId))
                .findFirst()
                .orElse(null);
        if (dto == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el componente seleccionado o no es compatible.");
            return;
        }

        java.util.List<String> errores = fachada.agregarComponente(dto);
        if (!errores.isEmpty()) {
            JOptionPane.showMessageDialog(this, String.join("\n", errores));
            // no habilitar continuar
            this.continuarBtn.setEnabled(false);
            this.updateButtonState();
            return;
        }

        // actualizar resumen lateral
        sideMenuResumenPanel.updateFrom(fachada.getEnsamblajeActual());

        // marcar paso como completado y habilitar continuar
        this.pasoCompletado = true;
        this.continuarBtn.setEnabled(true);
        this.updateButtonState();
    }

    public void updateButtonState() {
        retrocederBtn.setEnabled(indiceActual > 0);
        // Continuar solo si el paso actual está completado y no es el último paso
        boolean enUltimo = indiceActual >= listaPasosArmado.length - 1;
        continuarBtn.setEnabled(!enUltimo && pasoCompletado);
    }

    public void añadirMenusNavegacion() {
        panelOeste.add(menuComponentesPanel);
        panelEste.add(sideMenuResumenPanel);
    }

    public void eliminarMenusNavegacion() {
        panelOeste.remove(menuComponentesPanel);
        panelEste.remove(sideMenuResumenPanel);
    }

    public void cargarMenu() {
        listaPasosArmado[0] = "Tipo de PC";
        listaPasosArmado[1] = "Marca del procesador";
        listaPasosArmado[2] = "Procesador";
        listaPasosArmado[3] = "Tarjeta Madre";
        listaPasosArmado[4] = "Memoria RAM";
        listaPasosArmado[5] = "Almacenamiento";
        listaPasosArmado[6] = "Unidad SSD";
        listaPasosArmado[7] = "Tarjeta de video";
        listaPasosArmado[8] = "Fuente de poder";
        listaPasosArmado[9] = "Disipador";
        listaPasosArmado[10] = "Ventilador";
        listaPasosArmado[11] = "Monitor";
        listaPasosArmado[12] = "Kit de telcado/raton";
        listaPasosArmado[13] = "Redes e internet";
        listaPasosArmado[14] = "Resumen";

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
